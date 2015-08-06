package com.ycsoft.boss.remoting.ott;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ycsoft.http.HttpUtils;
import com.ycsoft.http.ResponseBody;

public class OttClient {
	private static String UNDEFINED_ERROR_STATUS="10000";
	
	/**
	 * 创建用户
	 * @return
	 */
	public Result createUser(Integer userId,String password,String custName,String address,
			String email,String telephone,String deviceId,String deviceMac){
		User user = generateUser(userId,password,custName,address,email,telephone,deviceId,deviceMac);
		String url = URLBuilder.getUrl(URLBuilder.Method.CREATE_USER); 
		Result result =null;
		
		try {
			String jsonData = new Gson().toJson(user);
			ResponseBody response = HttpUtils.doPost(url, jsonData);
			result = new Gson().fromJson(response.getBody(), Result.class);
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
		}
		
		return result;
		
	}
	
	

	/**
	 * 修改用户
	 * @return
	 */
	public Result editUser(Integer userId,String password,String custName,String address,
			String email,String telephone,String deviceId,String deviceMac){
		return this.createUser(userId, password, custName, address, email,
				telephone, deviceId, deviceMac);
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public Result deleteUser(Integer userId){
		String url = URLBuilder.getUrl(URLBuilder.Method.DELETE_USER); 
		Result result =null;
		
		try {
			JsonObject jsonData = new JsonObject();
			jsonData.addProperty("user_id", userId.toString());
			ResponseBody response = HttpUtils.doPost(url, jsonData.toString());
			result = new Gson().fromJson(response.getBody(), Result.class);
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 发送产品加授权
	 * @return
	 */
	public Result openUserProduct(Integer userId,String productId){
		String url = URLBuilder.getUrl(URLBuilder.Method.OPEN_USER_PRODCT); 
		Result result =null;
		try {
			Auth auth = new Auth(userId.toString(),productId);
			List<Auth> authList = new ArrayList<>();
			authList.add(auth);
			ResponseBody response = HttpUtils.doPost(url, new Gson().toJson(authList));
			result = new Gson().fromJson(response.getBody(), Result.class);
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
		}
		
		return result;
	}
	/**
	 * 发生产品减授权
	 * @return
	 */
	
	public Result stopUserProduct(Integer userId,String productId){
		String url = URLBuilder.getUrl(URLBuilder.Method.STOP_USER_PRODCT); 
		Result result =null;
		try {
			JsonObject jsonData = new JsonObject();
			jsonData.addProperty("user_Id", userId.toString());
			jsonData.addProperty("product_id", productId);
			ResponseBody response = HttpUtils.doPost(url, jsonData.toString());
			result = new Gson().fromJson(response.getBody(), Result.class);
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 增加或者修改产品
	 * @return
	 */
	public Result addOrUpdateProduct(String productId,String productName){
		String url = URLBuilder.getUrl(URLBuilder.Method.ADD_UPDATE_PRODUCT); 
		Result result =null;
		
		try {
			Product product = new Product(productId,productName);
			ResponseBody response = HttpUtils.doPost(url, new Gson().toJson(product));
			result = new Gson().fromJson(response.getBody(), Result.class);
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 删除产品
	 * @return
	 */
	public Result deleteProduct(String productId){
		String url = URLBuilder.getUrl(URLBuilder.Method.DELETE_PRODUCT); 
		Result result =null;
		
		try {
			JsonObject jsonData = new JsonObject();
			jsonData.addProperty("ids", productId);
			ResponseBody response = HttpUtils.doPost(url, jsonData.toString());
			result = new Gson().fromJson(response.getBody(), Result.class);
		} catch (Throwable e) {
			result = new Result();
			result.setErr("1");
			result.setStatus(UNDEFINED_ERROR_STATUS);
			result.setReason(e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * ���user bean
	 * @param userId
	 * @param custName
	 * @param address
	 * @param email
	 * @param telephone
	 * @param deviceId
	 * @param deviceMac
	 * @return
	 */
	private User generateUser(Integer userId,String password, String custName, String address, String email, String telephone,
			String deviceId, String deviceMac) {
		User user = new User();
		user.setUser_id(userId);
		user.setUser_passwd(password);
		user.setUser_name(custName);
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
