package com.yaochen.boss.job.component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.ycsoft.beans.core.job.BusiCmdParam;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.dao.config.TServerOttauthProdDao;
import com.ycsoft.business.dao.core.job.JBandCommandDao;
import com.ycsoft.business.dao.core.job.JCaCommandDao;
import com.ycsoft.business.dao.core.job.JVodCommandDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.daos.core.JDBCException;

@Component
public class AuthComponent extends BaseComponent {
	@Autowired
	private JVodCommandDao jVodCommandDao;
	@Autowired
	private JBandCommandDao jBandCommandDao;
	@Autowired
	private  CUserDao cUserDao;
	@Autowired
	private JCaCommandDao jCaCommandDao;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private TServerOttauthProdDao tServerOttauthProdDao;
	
	/**
	 * 查询ott授权资源
	 * @throws Exception 
	 * @throws JDBCException 
	 */
	public Map<String,TServerOttauthProd> queryOttAuthProdMap() throws Exception{
		return tServerOttauthProdDao.queryAllMap();
	}
	private Long gTransnum() throws Exception{
		return Long.parseLong(jCaCommandDao.findSequence().toString());
	}
	
	private JBandCommand gBandCmd(CUser user,Integer doneCode) throws Exception{
		JBandCommand bandCmd = new JBandCommand();
		bandCmd.setDone_code(doneCode);
		bandCmd.setTransnum(gTransnum());
		bandCmd.setCust_id(user.getCust_id());
		bandCmd.setUser_id(user.getUser_id());
		bandCmd.setCreate_time(new Date());
		bandCmd.setIs_send(SystemConstants.BOOLEAN_FALSE);
		bandCmd.setCounty_id(user.getCounty_id());
		bandCmd.setArea_id(user.getArea_id());
		return bandCmd;
	}
	
	/**
	 * 宽带所有订单到期日后，对宽带补发清除授权指令，因为宽带在汇聚系统发的长授权
	 * @param order
	 */
	public void clearBandAuth(List<CProdOrder> orders) throws Exception{
		for(CProdOrder order:orders){
			String userId=order.getUser_id();
			CUser user=cUserDao.findByKey(userId);
			if(user==null)
				continue;
			JBandCommand bandCmd = gBandCmd(user, -12);
			bandCmd.setCmd_type(BusiCmdConstants.BAND_CLEAR_AUTH);
			JsonObject params = new JsonObject();
			params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
			bandCmd.setDetail_param(params.toString());
			jBandCommandDao.save(bandCmd);
		}
	}
	/**
	 * 生成修改宽带带宽的指令
	 * @param userId
	 * @throws Exception 
	 */
	public void changeBandWidth(CProdOrder order) throws Exception{
		//修改订单check_time
		cProdOrderDao.updateCheckTime(order.getOrder_sn());
		//插入指令
		String userId=order.getUser_id();
		List<Entry<String, Date>> list=this.getUserResMappingListOrderByExpDate(userId);
		
		if(list==null||list.size()==0)
			return;
		CUser user=cUserDao.findByKey(userId);
		if(user==null){
			return;
		}
		String resId=list.get(0).getKey();
		Date expDate=list.get(list.size()-1).getValue();
		
		JBandCommand bandCmd = gBandCmd(user, -13);
		bandCmd.setCmd_type(BusiCmdConstants.BAND_ADD_AUTH);
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		params.addProperty(BusiCmdParam.band_policy_id.name(), resId);
		params.addProperty(BusiCmdParam.prod_eff_date.name(),
					DateHelper.format(DateHelper.today(), DateHelper.FORMAT_TIME_VOD));
		params.addProperty(BusiCmdParam.prod_exp_date.name(), DateHelper.format(expDate, DateHelper.FORMAT_TIME_VOD_END));
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
	}
	
	public List<JVodCommand> queryOttCmd() throws Exception{
		return jVodCommandDao.queryCmd();
	}
	
	public List<JBandCommand> queryBandCmd() throws Exception{
		return jBandCommandDao.queryCmd();
	} 
	
	
	
	public void saveOttSendResult(JVodCommand cmd,Result result) throws Exception{
		if(result.isSuccess()||result.isBossError()||
				(!result.isUndefinedError()&&!result.isConnectionError())){
			cmd.setIs_send(SystemConstants.BOOLEAN_TRUE);
		}else{
			//网络错误或者未知严重错误需要重发,所有不设置已发状态
			cmd.setIs_send(SystemConstants.BOOLEAN_FALSE);
		}
		cmd.setSend_time(new Date());
		cmd.setIs_success(result.isSuccess()?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
		cmd.setError_info(result.getReason());
		try{
			cmd.setReturn_code(Integer.parseInt(result.getStatus()));
		}catch(Exception e){}
		jVodCommandDao.updateByCmd(cmd.getTransnum(), cmd.getIs_send(), cmd.getIs_success(), cmd.getError_info(), cmd.getReturn_code());
	}
	
	public void saveBandSendResult(JBandCommand cmd,Result result) throws Exception{
		if(result.isSuccess()||result.isBossError()||
				(!result.isUndefinedError()&&!result.isConnectionError())){
			cmd.setIs_send(SystemConstants.BOOLEAN_TRUE);
		}else{
			//网络错误或者未知严重错误需要重发,所有不设置已发状态
			cmd.setIs_send(SystemConstants.BOOLEAN_FALSE);
		}
		cmd.setSend_time(new Date());
		cmd.setIs_success(result.isSuccess()?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
		cmd.setError_info(result.getReason());
		if(result.getStatus()!=null){
			try{
			cmd.setReturn_code(Integer.parseInt(result.getStatus()));
			}catch(Exception e){}
		}
		//jBandCommandDao.update(cmd);
		jBandCommandDao.updateByCmd(cmd.getTransnum(), cmd.getIs_send(), cmd.getIs_success(), cmd.getError_info(), cmd.getReturn_code());
	}
	
	
}
