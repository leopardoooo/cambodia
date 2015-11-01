package com.ycsoft.boss.remoting.ott;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.http.HttpUtils;
import com.ycsoft.http.ResponseBody;

/**
 * 
 * 该类只能通过spring容器获得实例，因为需要依赖注入Builder
 */
public class OttClient {
	
	private URLBuilder builder;
	
	/**
	 * 创建用户
	 * @return
	 */
	public Result createUser(String loginName,String password,String userName,String address,
			String email,String telephone,String stbId,String deviceMac,String status){
		if(StringHelper.isEmpty(loginName)){
			return this.getBossErrorResult("loginName账户为空");
		}
		if(StringHelper.isEmpty(password)){
			return this.getBossErrorResult("密码为空");
		}
		User user = generateUser(loginName,password,userName,address,email,telephone,stbId,deviceMac,status);
		String url = builder.getUrl(URLBuilder.Method.CREATE_USER); 
		String jsonData = new Gson().toJson(user);
		//System.out.println(jsonData);
		return sendOttCmdOnHttp(url, jsonData);
	}

	/**
	 * 修改用户
	 * @return
	 */
	public Result editUser(String loginName,String password,String userName,String address,
			String email,String telephone,String stbId,String deviceMac,String status){
		return this.createUser(loginName, password, userName, address, email,
				telephone, stbId, deviceMac,status);
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public Result deleteUser(String loginName){
		if(StringHelper.isEmpty(loginName)){
			return this.getBossErrorResult("loginName账户为空");
		}
		String url = builder.getUrl(URLBuilder.Method.DELETE_USER); 
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("user_id", loginName);
		return sendOttCmdOnHttp(url, jsonData.toString());
	}
	
	public Result getBossErrorResult(String message){
		Result result = new Result();
		result.setErr("1");
		result.setStatus(Result.BOSS_ERROR_STATUS);
		result.setReason(message);
		return result;
	}
	/**
	 * 发送产品加授权
	 * @return
	 * @throws ComponentException 
	 */
	public Result openUserProduct(String loginName,String externalResId,String expDate,Map<String,TServerOttauthProd> ottAuthMap) {
		String url = builder.getUrl(URLBuilder.Method.OPEN_USER_PRODCT); 
		
		if(StringHelper.isEmpty(loginName)){
			return this.getBossErrorResult("loginName账户为空");
		}
		if(externalResId==null){
			return this.getBossErrorResult("授权控制字为空");
		}
		if(ottAuthMap==null){
			return this.getBossErrorResult("TServerOttauthProd数据为空");
		}
		//String[] spiltRess=externalResId.split(",");
		TServerOttauthProd ottauth=ottAuthMap.get(externalResId);
		if(ottauth==null){
			return this.getBossErrorResult("控制字"+externalResId+" 未在t_server_ottauth_prod表定义");
		}
		
		Auth auth = new Auth(loginName.toString(),ottauth.getId());
		auth.setEnd_time(expDate);
		auth.setProduct_fee_id(ottauth.getFee_id());
		List<Auth> authList = new ArrayList<>();
		authList.add(auth);
		
		return sendOttCmdOnHttp(url, new Gson().toJson(authList));
		
	}
	
	/**
	 * 发产品减授权
	 * @return
	 * @throws ComponentException 
	 */
	
	public Result stopUserProduct(String loginName,String externalResId,Map<String,TServerOttauthProd> ottAuthMap){
		String url = builder.getUrl(URLBuilder.Method.STOP_USER_PRODCT); 
		if(StringHelper.isEmpty(loginName)){
			return this.getBossErrorResult("loginName账户为空");
		}
		if(externalResId==null){
			return this.getBossErrorResult("授权控制字为空");
		}
		if(ottAuthMap==null){
			return this.getBossErrorResult("TServerOttauthProd数据为空");
		}
		//String[] spiltRess=externalResId.split(",");
		TServerOttauthProd ottauth=ottAuthMap.get(externalResId);
		if(ottauth==null){
			return this.getBossErrorResult("控制字"+externalResId+" 未在t_server_ottauth_prod表定义");
		}
		
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("user_id", loginName);
		jsonData.addProperty("product_id", ottauth.getId());
		jsonData.addProperty("product_fee_id",ottauth.getFee_id());
		
		String param = StringHelper.append("[",jsonData.toString(),"]");
		//System.out.println(param);
		
		return sendOttCmdOnHttp(url, param);
		
	}
	
	/**
	 * 增加或者修改产品
	 * @return
	 */
	public Result addOrUpdateProduct(String prodFeeInfo){
		String url = builder.getUrl(URLBuilder.Method.ADD_UPDATE_PRODUCT); 
		//Product product = new Product(productId,productName);
		return sendOttCmdOnHttp(url, prodFeeInfo);
	}
	
	/**
	 * 删除产品
	 * @return
	 */
	public Result deleteProduct(String productId){
		String url = builder.getUrl(URLBuilder.Method.DELETE_PRODUCT); 
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("ids", productId);
		
		return sendOttCmdOnHttp(url,  jsonData.toString());
		
	}
	
	//发送指令
	private Result sendOttCmdOnHttp(String url,String param){
		Result result =null;
		try {
			ResponseBody response = HttpUtils.doPost(url, param);
			try{
				result = new Gson().fromJson(response.getBody(), Result.class);
			}catch(Throwable e1){
				String info=response.getBody();
				LoggerHelper.debug(this.getClass(), info);
				info=info.substring(info.indexOf("{"));
				result = new Gson().fromJson(info, Result.class);
			}
			
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(Result.UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * user bean
	 * @param userId
	 * @param custName
	 * @param address
	 * @param email
	 * @param telephone
	 * @param deviceId
	 * @param deviceMac
	 * @return
	 */
	private User generateUser(String userId,String password, String userName, String address, String email, String telephone,
			String deviceId, String deviceMac,String status) {
		User user = new User();
		user.setUser_id(userId);
		user.setUser_passwd(password);
		user.setUser_name(userName);
		user.setAddress(address);
		user.setEmail(email);
		user.setTelephone(telephone);
		user.setDevice_info(new ArrayList<DeviceInfo>());
		if (deviceId != null || deviceMac != null){
			DeviceInfo device = new DeviceInfo();
			device.setId(deviceId);
			device.setMac(deviceMac);
			user.getDevice_info().add(device);
		}
		if (status != null && (status.equals(StatusConstants.ACTIVE)||status.equals(StatusConstants.INSTALL))){
			user.setState("0");
			//user.setEnd_time(Da);
		}else if(status.equals(StatusConstants.PREOPEN)){
			user.setState("9");//"9表示未激活 用于OTT_MOBILE注册接口使用"
		}else {
			user.setState("2");
			//user.setEnd_time(new Date());
		}
		user.setUser_rank("0");
		user.setUser_permission("0");
		user.setBegin_time(DateHelper.today());
		
		return user;
	}

	public void setBuilder(URLBuilder builder) {
		this.builder = builder;
	}
}
