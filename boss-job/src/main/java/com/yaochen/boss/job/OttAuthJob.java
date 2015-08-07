package com.yaochen.boss.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.job.BusiCmdParam;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.boss.remoting.ott.OttClient;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.helper.DateHelper;

public class OttAuthJob implements Job2 {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void execute(Job2ExecutionContext arg0) throws JobExecutionException {
		//读取要发送的指令
		List<JVodCommand> cmdList = new ArrayList();
		OttClient client = new OttClient();
		for (JVodCommand cmd:cmdList){
			Result result = null;
			if ((cmd.getCmd_type().equals(BusiCmdConstants.CHANGE_USER))){
				JsonObject params = new Gson().fromJson(cmd.getDetail_param(), JsonObject.class);
				result = client.editUser(cmd.getUser_id(), 
						params.get(BusiCmdParam.login_password.name()).getAsString(), 
						params.get(BusiCmdParam.login_password.name()).getAsString(),
						null, null,null,
						params.get(BusiCmdParam.stb_id.name()).getAsString(),
						params.get(BusiCmdParam.stb_mac.name()).getAsString());
			} else if ((cmd.getCmd_type().equals(BusiCmdConstants.DEL_USER))){
				result = client.deleteUser(cmd.getUser_id());
			} else if ((cmd.getCmd_type().equals(BusiCmdConstants.PASSVATE_PROD))){
				result = client.stopUserProduct(cmd.getUser_id(), cmd.getRes_id());
			} else if  ((cmd.getCmd_type().equals(BusiCmdConstants.ACCTIVATE_PROD))){
				JsonObject params = new Gson().fromJson(cmd.getDetail_param(), JsonObject.class);
				result = client.openUserProduct(cmd.getUser_id(), cmd.getRes_id(),
						params.get(BusiCmdParam.prod_exp_date.name()).getAsString());
			}
			
			if (result.isConnectionError()){
				logger.error("网络无法连接，暂停发送!"+result.getReason());
				break; 
			} else if (result.isUndefinedError()){
				logger.error("未知严重错误，暂停发送!"+result.getReason());
			} else {
				//保存发送结果
			}
		}
	}
	

}
