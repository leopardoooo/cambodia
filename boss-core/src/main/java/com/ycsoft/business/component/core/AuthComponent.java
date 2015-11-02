package com.ycsoft.business.component.core;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.ycsoft.beans.core.job.BusiCmdParam;
import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.beans.core.job.SmsxCmd;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.dao.core.job.JBandCommandDao;
import com.ycsoft.business.dao.core.job.JCaCommandDao;
import com.ycsoft.business.dao.core.job.JVodCommandDao;
import com.ycsoft.business.dao.prod.PProdStaticResDao;
import com.ycsoft.business.dao.prod.TServerResDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;

@Component
public class AuthComponent extends BaseComponent{
	@Autowired
	private JCaCommandDao jCaCommandDao;
	@Autowired
	private JVodCommandDao jVodCommandDao;
	@Autowired
	private JBandCommandDao jBandCommandDao;
	@Autowired
	private PProdStaticResDao pProdStaticResDao;
	@Autowired
	private TServerResDao tServerResDao;
	
	public void sendAuth(CUser user,List<CProdOrder> orderList,String authCmdType,Integer doneCode) throws Exception{
	
			if (user.getUser_type().equals(SystemConstants.USER_TYPE_OTT) || 
					user.getUser_type().equals(SystemConstants.USER_TYPE_OTT_MOBILE)){
				sendOttAuth(user,orderList,authCmdType,doneCode);
			} else if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)) {
				sendBandAuth(user,orderList,authCmdType,doneCode);
			} else if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTT)) {
				sendDttAuth(user,orderList,authCmdType,doneCode);
			}
		
	}
	
	
	private void sendOttAuth(CUser user, List<CProdOrder> orderList, String authCmdType, Integer doneCode) throws Exception{
		if(user.getUser_type().equals(SystemConstants.USER_TYPE_OTT)&&
				StringHelper.isEmpty(user.getStb_id())){
			//OTT用户无设备，则忽略指令
			return;
		}
		if (authCmdType.equals(BusiCmdConstants.CREAT_USER) || //创建用户
				authCmdType.equals(BusiCmdConstants.CHANGE_USER) || //变更用户
				authCmdType.equals(BusiCmdConstants.PASSVATE_USER) ||//钝化用户
				authCmdType.equals(BusiCmdConstants.ACCTIVATE_USER)||//激活用户
				authCmdType.equals(BusiCmdConstants.REFRESH_TERMINAL)){//刷新终端
			this.editOttUser(user, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.DEL_USER)){//删除用户
			this.deleteOttUser(user,orderList, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.PASSVATE_PROD) ||//产品授权
				authCmdType.equals(BusiCmdConstants.ACCTIVATE_PROD)){
			this.authOttProd(user, orderList, doneCode);
		} else {
			throw new ComponentException(ErrorCode.CmdTypeUnDefined,authCmdType);
		}
		
	}
	
	private void sendDttAuth(CUser user, List<CProdOrder> orderList, String authCmdType, Integer doneCode) throws Exception{
		if(StringHelper.isEmpty(user.getCard_id())){
			//DTT用户无智能卡号，则忽略授权
			return;
		}
		if (authCmdType.equals(BusiCmdConstants.CREAT_USER)||//创建用户
				authCmdType.equals(BusiCmdConstants.REFRESH_TERMINAL)){//刷新终端
			this.openTerminal(user, doneCode);
		}else if (authCmdType.equals(BusiCmdConstants.PASSVATE_USER)||//激活和钝化用户
				authCmdType.equals(BusiCmdConstants.ACCTIVATE_USER)){
			//DTT忽略钝化和激活用户指令
			return;
		}else if (authCmdType.equals(BusiCmdConstants.PASSVATE_PROD)){//减授权
			this.authDttCancelProd(user, orderList, doneCode);
		}else if(authCmdType.equals(BusiCmdConstants.ACCTIVATE_PROD)){//加授权
			this.authDttProd(user, orderList, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.DEL_USER)){//删除用户
			this.StopTerminal(user, doneCode);
		}else {
			throw new ComponentException(ErrorCode.CmdTypeUnDefined,authCmdType);
		}
	}
	
	private void sendBandAuth(CUser user,List<CProdOrder> orderList, String authCmdType, Integer doneCode) throws Exception{
		if (authCmdType.equals(BusiCmdConstants.CREAT_USER)){
			this.createBandUser(user, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.DEL_USER)){
			this.deleteBandUser(user, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.PASSVATE_USER)){
			this.stopBandUser(user, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.ACCTIVATE_USER)){
			this.openBandUser(user, doneCode);
		} else if (authCmdType.equals(BusiCmdConstants.PASSVATE_PROD) ||
				authCmdType.equals(BusiCmdConstants.ACCTIVATE_PROD)){
			this.refreshBandUserAuth(user, doneCode);
		}else if(authCmdType.equals(BusiCmdConstants.REFRESH_TERMINAL)){
			//宽带刷新设备要发销户、开户、2个指令
			//this.deleteBandUser(user, doneCode);
			this.createBandUser(user, doneCode);
			//如果用户是报停状态，则钝化用户
			if(user.getStatus().equals(StatusConstants.REQSTOP)){
				this.stopBandUser(user, doneCode);
			}else{
				this.openBandUser(user, doneCode);
			}
		}else if(authCmdType.equals(BusiCmdConstants.CHANGE_USER)){//修改密码...
			this.updatePassword(user,doneCode);
		}else {
			throw new ComponentException(ErrorCode.CmdTypeUnDefined,authCmdType);
		} 
	}

	private void updatePassword(CUser user, Integer doneCode) throws Exception {
		JBandCommand bandCmd = gBandCmd(user,doneCode);
		bandCmd.setCmd_type(BusiCmdConstants.BAND_EDIT_PWD);	
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		params.addProperty(BusiCmdParam.login_password.name(), user.getPassword());
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
	}


	/**===================================FOR OTT USER=========================================**/
	/**
	 * 创建修改OTT用户
	 * @param user
	 * @param doneCode
	 * @throws Exception
	 */
	private void editOttUser(CUser user,Integer doneCode) throws Exception{
		JVodCommand ottCmd = gOttCmd(user,doneCode);
		ottCmd.setCmd_type(BusiCmdConstants.CHANGE_USER);
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		params.addProperty(BusiCmdParam.login_password.name(), user.getPassword());
		params.addProperty(BusiCmdParam.stb_id.name(), user.getStb_id());
		params.addProperty(BusiCmdParam.stb_mac.name(), user.getModem_mac());
		params.addProperty(BusiCmdParam.user_status.name(), user.getStatus());
		ottCmd.setDetail_param(params.toString());
		jVodCommandDao.save(ottCmd);
	}
	
	/**
	 * ott产品加减授权
	 * @param user
	 * @param order
	 * @param busiCmdType
	 * @param doneCode
	 * @throws Exception
	 */
	private void authOttProd(CUser user,List<CProdOrder> orderList,Integer doneCode) throws Exception{
		//获取用户所有资源的所有到期日
		Map<String,Date> userResMap = this.getUserResExpDate(user.getUser_id());
		//获取订单包含的资源
		String[] orderResIds = getOrderProdRes(orderList);
		
		for (String orderResId:orderResIds){
			JVodCommand ottCmd = gOttCmd(user,doneCode);
			ottCmd.setRes_id(orderResId);
			Date expDate = userResMap.get(orderResId);
			if (expDate == null || expDate.before(new Date())){
				//发送减授权
				JsonObject params = new JsonObject();
				params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
				ottCmd.setDetail_param(params.toString());
				ottCmd.setCmd_type(BusiCmdConstants.PASSVATE_PROD);
			} else {
				ottCmd.setCmd_type(BusiCmdConstants.ACCTIVATE_PROD);
				JsonObject params = new JsonObject();
				params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
				params.addProperty(BusiCmdParam.prod_exp_date.name(), DateHelper.format(expDate, DateHelper.FORMAT_TIME_END));
				ottCmd.setDetail_param(params.toString());
			}
			jVodCommandDao.save(ottCmd);
		}
	}

	
	
	
	/**
	 * 删除OTT用户
	 * @param user
	 * @param doneCode
	 * @throws Exception
	 */
	private void deleteOttUser(CUser user,List<CProdOrder> orderList,Integer doneCode) throws Exception{
		//删除授权
		String[] orderResIds = getOrderProdRes(orderList);
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		for (String orderResId:orderResIds){
			JVodCommand ottCmd = gOttCmd(user,doneCode);
			ottCmd.setRes_id(orderResId);
			//发送减授权
			
			ottCmd.setDetail_param(params.toString());
			ottCmd.setCmd_type(BusiCmdConstants.PASSVATE_PROD);
			jVodCommandDao.save(ottCmd);
		}
		//销户
		JVodCommand ottCmd = gOttCmd(user,doneCode);
		ottCmd.setDetail_param(params.toString());
		ottCmd.setCmd_type(BusiCmdConstants.DEL_USER);		
		jVodCommandDao.save(ottCmd);
	}
	
	/**=================================FOR DTT=========================================**/
	
	//开户
	private void openTerminal(CUser user,Integer doneCode) throws Exception{
		JCaCommand caCommand = gDttCmd(user, doneCode);
		caCommand.setCmd_type(SmsxCmd.OpenICC.name());
		jCaCommandDao.save(caCommand);
	}
		
	//销户
	private void StopTerminal(CUser user,Integer doneCode) throws Exception{
		JCaCommand dttCommand = gDttCmd(user, doneCode);
		dttCommand.setCmd_type(SmsxCmd.StopICC.name());
		jCaCommandDao.save(dttCommand);
	}
	
	//发送DTT减授权
	private void authDttCancelProd(CUser user,List<CProdOrder> orderList,Integer doneCode) throws Exception{
		//获取用户所有资源的所有到期日
		//Map<String,Date> userResMap = this.getUserResExpDate(user.getUser_id());
		//获取订单包含的资源
		String[] orderResIds = getOrderProdRes(orderList);
		
		for (String orderResId:orderResIds){
			JCaCommand dttCmd = gDttCmd(user, doneCode);
			dttCmd.setBoss_res_id(orderResId);
			dttCmd.setControl_id(orderResId);
		//	Date expDate = userResMap.get(orderResId);
			//发送减授权
			dttCmd.setCmd_type(SmsxCmd.CancelProduct.name());
			jCaCommandDao.save(dttCmd);
			
		}
	}
	//发送DTT授权
	private void authDttProd(CUser user,List<CProdOrder> orderList,Integer doneCode) throws Exception{
		//获取用户所有资源的所有到期日
		Map<String,Date> userResMap = this.getUserResExpDate(user.getUser_id());
		//获取订单包含的资源
		String[] orderResIds = getOrderProdRes(orderList);
		
		for (String orderResId:orderResIds){
			JCaCommand dttCmd = gDttCmd(user, doneCode);
			dttCmd.setBoss_res_id(orderResId);
			dttCmd.setControl_id(orderResId);
			Date expDate = userResMap.get(orderResId);
			//发送减授权
			dttCmd.setCmd_type(SmsxCmd.CancelProduct.name());
			jCaCommandDao.save(dttCmd);
			if (expDate != null && expDate.after(new Date())){
				dttCmd.setTransnum(gTransnum());
				dttCmd.setCmd_type(SmsxCmd.AddProduct.name());
				dttCmd.setAuth_begin_date(DateHelper.format(new Date(), DateHelper.FORMAT_TIME_VOD));
				dttCmd.setAuth_end_date( DateHelper.format(expDate, DateHelper.FORMAT_TIME_VOD));
				jCaCommandDao.save(dttCmd);
			}
		}
	}
	
	/**
	 * 重发Dtt用户授权
	 * @param user
	 * @param doneCode
	 * @throws Exception
	 */
	private void refreshDttUserAuth(CUser user,Integer doneCode) throws Exception{
		//获取用户所有资源的所有到期日
		Map<String,Date> userResMap = this.getUserResExpDate(user.getUser_id());
		//先全部取消授权
		for (Entry<String,Date> entry:userResMap.entrySet()){
			JCaCommand dttCmd = gDttCmd(user, doneCode);
			dttCmd.setCmd_type(SmsxCmd.CancelProduct.name());
			dttCmd.setControl_id(entry.getKey());
			jCaCommandDao.save(dttCmd);
			
			dttCmd.setTransnum(gTransnum());
			dttCmd.setCmd_type(SmsxCmd.CancelProduct.name());
			dttCmd.setAuth_begin_date(DateHelper.formatNow());
			dttCmd.setAuth_end_date( DateHelper.format(entry.getValue(), DateHelper.FORMAT_TIME));
			jCaCommandDao.save(dttCmd);
		}
	}
	
	
	/**===============================Band========================================*/
	private void createBandUser(CUser user,Integer doneCode)  throws Exception{
		JBandCommand bandCmd = gBandCmd(user,doneCode);
		bandCmd.setCmd_type(BusiCmdConstants.CREAT_USER);	
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		params.addProperty(BusiCmdParam.login_password.name(), user.getPassword());
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
		
	}
	
	private void stopBandUser(CUser user,Integer doneCode) throws Exception{
		JBandCommand bandCmd = gBandCmd(user,doneCode);
		bandCmd.setCmd_type(BusiCmdConstants.PASSVATE_USER);	
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
	}
	
	private void openBandUser(CUser user,Integer doneCode) throws Exception{
		JBandCommand bandCmd = gBandCmd(user,doneCode);
		bandCmd.setCmd_type(BusiCmdConstants.ACCTIVATE_USER);	
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
	}
	
	private void deleteBandUser(CUser user,Integer doneCode) throws Exception{
		//取消授权
		//cancelOrder(user,doneCode);
		JBandCommand bandCmd = gBandCmd(user,doneCode);
		bandCmd.setCmd_type(BusiCmdConstants.DEL_USER);	
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
	}
	
	private void refreshBandUserAuth(CUser user,Integer doneCode) throws Exception{
		//获取用户所有资源的所有到期日
		//Map<String,Date> userResMap = this.getUserResExpDate(user.getUser_id());
		List<Entry<String, Date>> mappingList =this.getUserResMappingListOrderByExpDate(user.getUser_id());
		
		if (mappingList!= null && mappingList.size()>0){
			
			String resId = mappingList.get(0).getKey();
			Date expDate = mappingList.get(mappingList.size()-1).getValue();
			
			JBandCommand bandCmd = gBandCmd(user, doneCode);
			bandCmd.setCmd_type(BusiCmdConstants.BAND_ADD_AUTH);
			JsonObject params = new JsonObject();
			params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
			params.addProperty(BusiCmdParam.band_policy_id.name(), resId);
			params.addProperty(BusiCmdParam.prod_eff_date.name(),
						DateHelper.format(DateHelper.addDate(DateHelper.today(),-1), DateHelper.FORMAT_TIME_VOD));
			params.addProperty(BusiCmdParam.prod_exp_date.name(), DateHelper.format(expDate, DateHelper.FORMAT_TIME_VOD_END));
			bandCmd.setDetail_param(params.toString());
			jBandCommandDao.save(bandCmd);
			
		}else{
			//发删除产品指令
			cancelOrder(user,doneCode);
		}
		
	}


	private void cancelOrder(CUser user, Integer doneCode) throws Exception, JDBCException {
		JBandCommand bandCmd = gBandCmd(user,doneCode);
		bandCmd.setCmd_type(BusiCmdConstants.BAND_CLEAR_AUTH);	
		JsonObject params = new JsonObject();
		params.addProperty(BusiCmdParam.login_name.name(), user.getLogin_name());
		bandCmd.setDetail_param(params.toString());
		jBandCommandDao.save(bandCmd);
	}
	
	
	private JVodCommand gOttCmd(CUser user,Integer doneCode) throws Exception{
		JVodCommand ottCmd = new JVodCommand();
		ottCmd.setDone_code(doneCode);
		ottCmd.setTransnum(gTransnum());
		ottCmd.setCust_id(user.getCust_id());
		ottCmd.setUser_id(user.getUser_id());
		ottCmd.setCreate_time(new Date());
		ottCmd.setIs_send(SystemConstants.BOOLEAN_FALSE);
		ottCmd.setCounty_id(user.getCounty_id());
		ottCmd.setArea_id(user.getArea_id());
		return ottCmd;
	}
	
	private JCaCommand gDttCmd(CUser user,Integer doneCode) throws Exception{
		JCaCommand dttCmd = new JCaCommand();
		dttCmd.setDone_code(doneCode);
		dttCmd.setTransnum(gTransnum());
		dttCmd.setCust_id(user.getCust_id());
		dttCmd.setUser_id(user.getUser_id());
		dttCmd.setCard_id(user.getCard_id());
		dttCmd.setStb_id(user.getStb_id());
		dttCmd.setCas_type("SMSX");
		dttCmd.setCas_id("SMSX");
		dttCmd.setCreate_time(new Date());
		dttCmd.setIs_sent("N");
		dttCmd.setCounty_id(user.getCounty_id());
		dttCmd.setArea_id(user.getArea_id());
		return dttCmd;
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


	private Long gTransnum() throws Exception{
		return Long.parseLong(jCaCommandDao.findSequence().toString());
	}
	
	
	//获取订单产品对应的资源id
	private String[] getOrderProdRes(List<CProdOrder> orderList) throws JDBCException {
		if (orderList == null || orderList.size()==0)
			return new String[0];
		Set<String> orderProdList = new HashSet<>();
		for (CProdOrder order:orderList){
			orderProdList.add(order.getProd_id());
		}
		
		String[] orderResIds = pProdStaticResDao.queryResByProdIds(orderProdList);
		return orderResIds;
	}

}
