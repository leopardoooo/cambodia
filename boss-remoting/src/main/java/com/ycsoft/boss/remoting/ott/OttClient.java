package com.ycsoft.boss.remoting.ott;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ycsoft.http.HttpUtils;
import com.ycsoft.http.ResponseBody;

public class OttClient {
	public static String UNDEFINED_ERROR_STATUS="10000";
	
	/**
	 * 创建用户
	 * @return
	 */
	public Result createUser(String userId,String password,String userName,String address,
			String email,String telephone,String deviceId,String deviceMac){
		User user = generateUser(userId,password,userName,address,email,telephone,deviceId,deviceMac);
		String url = URLBuilder.getUrl(URLBuilder.Method.CREATE_USER); 
		String jsonData = new Gson().toJson(user);
		return sendOttCmdOnHttp(url, jsonData);
	}

	/**
	 * 修改用户
	 * @return
	 */
	public Result editUser(String userId,String password,String userName,String address,
			String email,String telephone,String deviceId,String deviceMac){
		return this.createUser(userId, password, userName, address, email,
				telephone, deviceId, deviceMac);
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public Result deleteUser(String userId){
		String url = URLBuilder.getUrl(URLBuilder.Method.DELETE_USER); 
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("user_id", userId);
		return sendOttCmdOnHttp(url, jsonData.toString());
	}
	
	/**
	 * 发送产品加授权
	 * @return
	 */
	public Result openUserProduct(String userId,String productId,String expDate){
		String url = URLBuilder.getUrl(URLBuilder.Method.OPEN_USER_PRODCT); 
		Auth auth = new Auth(userId.toString(),productId);
		auth.setEnd_time(expDate);
		List<Auth> authList = new ArrayList<>();
		authList.add(auth);
		return sendOttCmdOnHttp(url, new Gson().toJson(authList));
		
	}
	/**
	 * 发产品减授权
	 * @return
	 */
	
	public Result stopUserProduct(String userId,String productId){
		String url = URLBuilder.getUrl(URLBuilder.Method.STOP_USER_PRODCT); 
		JsonObject jsonData = new JsonObject();
		jsonData.addProperty("user_Id", userId);
		jsonData.addProperty("product_id", productId);
		return sendOttCmdOnHttp(url, jsonData.toString());
		
	}
	
	/**
	 * 增加或者修改产品
	 * @return
	 */
	public Result addOrUpdateProduct(String productId,String productName){
		String url = URLBuilder.getUrl(URLBuilder.Method.ADD_UPDATE_PRODUCT); 
		Product product = new Product(productId,productName);
		return sendOttCmdOnHttp(url, new Gson().toJson(product));
	}
	
	/**
	 * 删除产品
	 * @return
	 */
	public Result deleteProduct(String productId){
		String url = URLBuilder.getUrl(URLBuilder.Method.DELETE_PRODUCT); 
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
			result.setStatus(UNDEFINED_ERROR_STATUS);
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
			String deviceId, String deviceMac) {
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
		}
		
		user.setState("0");
		user.setUser_rank("0");
		user.setUser_permission("0");
		user.setBegin_time(new Date());
		
		return user;
	}
	

}
