package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yaochen.boss.job.component.AuthComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.job.BusiCmdParam;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.boss.remoting.aaa.AAAException;
import com.ycsoft.boss.remoting.aaa.AAAInterfaceBusinessMgrServiceStub.ResultHeader;
import com.ycsoft.boss.remoting.aaa.BOSSBandServiceAdapter;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.commons.constants.BusiCmdConstants;

@Service
public class BandAuthJob implements Job2 {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AuthComponent authComponent;
	
	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		//读取要发送的指令
		List<JBandCommand> cmdList = null;
		try{
			cmdList = authComponent.queryBandCmd();
		} catch(Exception e){
			logger.error("读取指令失败"+e.getMessage());
			return;
		}
		
		BOSSBandServiceAdapter bandClient = new BOSSBandServiceAdapter();
		
		for (JBandCommand cmd:cmdList){
			Result result = new Result();
			JsonObject params =new JsonParser().parse(cmd.getDetail_param()).getAsJsonObject();
			String loginName = getJsonValue(params,BusiCmdParam.login_name.name());
			try {
				if ((cmd.getCmd_type().equals(BusiCmdConstants.CREAT_USER))){
					String loginPassword = getJsonValue(params,BusiCmdParam.login_name.name());
					bandClient.create(cmd.getDone_code(), loginName, loginPassword, null);
				} else if ((cmd.getCmd_type().equals(BusiCmdConstants.DEL_USER))) {
					bandClient.delete(cmd.getDone_code(), loginName);
				} else if ((cmd.getCmd_type().equals(BusiCmdConstants.PASSVATE_USER))) {
					bandClient.pause(cmd.getDone_code(), loginName);
				} else if ((cmd.getCmd_type().equals(BusiCmdConstants.ACCTIVATE_PROD))) {
					bandClient.resume(cmd.getDone_code(), loginName);
				} else if ((cmd.getCmd_type().equals(BusiCmdConstants.BAND_CLEAR_AUTH))) {
					bandClient.cancelOrder(cmd.getDone_code(), loginName);
				} else if ((cmd.getCmd_type().equals(BusiCmdConstants.BAND_ADD_AUTH))) {
					Integer policyId = Integer.parseInt(getJsonValue(params,BusiCmdParam.band_policy_id.name()));
					String effDate = getJsonValue(params,BusiCmdParam.prod_eff_date.name());
					String expDate = getJsonValue(params,BusiCmdParam.prod_exp_date.name());
					bandClient.orderService(cmd.getDone_code(), loginName, policyId, effDate, expDate);
				} 
				result.setStatus("0");
			} catch (AAAException e){
				ResultHeader rh = e.getResult();
				if (rh == null){
					//系统异常
					result.setErr(Result.UNDEFINED_ERROR_STATUS);
					result.setReason(e.getMessage());
				} else {
					result.setErr(rh.getResultCode());
					result.setReason(rh.getResultDesc());
				}
			}
			
			if (result.isConnectionError()){
				logger.error("网络无法连接，暂停发送!"+result.getReason());
				break; 
			} else if (result.isUndefinedError()){
				logger.error("未知严重错误，暂停发送!"+result.getReason());
				break;
			} else {
				//保存发送结果
				try{
					authComponent.saveBandSendResult(cmd, result);
				} catch(Exception e){
					e.printStackTrace();
					logger.error("保存指令发送结果失败"+e.getMessage());
					return;
				}
			}
		}
	}
	
	private String getJsonValue(JsonObject jo,String key){
		return jo.get(key).isJsonNull()?null:jo.get(key).getAsString();
	}

}
