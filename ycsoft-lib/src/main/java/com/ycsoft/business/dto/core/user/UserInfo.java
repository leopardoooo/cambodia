package com.ycsoft.business.dto.core.user;

public class UserInfo {
	private String user_type;
	private String device_buy_mode;
	private String device_model;
	private String fee_id;
	private Integer fee;
	private Integer user_count;
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getDevice_buy_mode() {
		return device_buy_mode;
	}
	public void setDevice_buy_mode(String device_buy_mode) {
		this.device_buy_mode = device_buy_mode;
	}
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public Integer getUser_count() {
		return user_count;
	}
	public void setUser_count(Integer user_count) {
		this.user_count = user_count;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	
	
	
}
