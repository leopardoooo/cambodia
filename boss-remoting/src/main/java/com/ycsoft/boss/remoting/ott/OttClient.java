package com.ycsoft.boss.remoting.ott;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.DateHelper;
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
	public Result openUserProduct(String loginName,String externalResId,String expDate) {
		String url = builder.getUrl(URLBuilder.Method.OPEN_USER_PRODCT); 
		
		if(StringHelper.isEmpty(loginName)){
			return this.getBossErrorResult("loginName账户为空");
		}
		if(externalResId==null){
			return this.getBossErrorResult("授权控制字为空");
		}
		String[] spiltRess=externalResId.split(",");
		if(spiltRess.length!=2){
			return this.getBossErrorResult("Ott的授权控子格式错误：OTT平台产品包ID,OTT平台资费ID");
		}
		
		Auth auth = new Auth(loginName.toString(),spiltRess[0]);
		auth.setEnd_time(expDate);
		auth.setProduct_fee_id(spiltRess[1]);
		List<Auth> authList = new ArrayList<>();
		authList.add(auth);
		
		return sendOttCmdOnHttp(url, new Gson().toJson(authList));
		
	}
	
	/**
	 * 发产品减授权
	 * @return
	 * @throws ComponentException 
	 */
	
	public Result stopUserProduct(String loginName,String externalResId){
		String url = builder.getUrl(URLBuilder.Method.STOP_USER_PRODCT); 
		if(StringHelper.isEmpty(loginName)){
			return this.getBossErrorResult("loginName账户为空");
		}
		if(externalResId==null){
			return this.getBossErrorResult("授权控制字为空");
		}
		String[] spiltRess=externalResId.split(",");
		if(spiltRess.length!=2){
			return this.getBossErrorResult("Ott的授权控子格式错误：OTT平台产品包ID,OTT平台资费ID");
		}
		
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("user_id", loginName);
		jsonData.addProperty("product_id", spiltRess[0]);
		jsonData.addProperty("product_fee_id",spiltRess[1]);
		
		String param = StringHelper.append("[",jsonData.toString(),"]");
		System.out.println(param);
		
		return sendOttCmdOnHttp(url, param);
		
	}
	
	/**
	 * 增加或者修改产品
	 * @return
	 */
	public Result addOrUpdateProduct(String productId,String productName){
		String url = builder.getUrl(URLBuilder.Method.ADD_UPDATE_PRODUCT); 
		Product product = new Product(productId,productName);
		return sendOttCmdOnHttp(url, new Gson().toJson(product));
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
			result = new Gson().fromJson(response.getBody(), Result.class);
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
		if (status != null && status.equals(StatusConstants.ACTIVE)){
			user.setState("0");
		} else {
			user.setState("2");
			user.setEnd_time(new Date());
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
