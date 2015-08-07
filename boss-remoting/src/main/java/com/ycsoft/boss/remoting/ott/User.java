package com.ycsoft.boss.remoting.ott;

import java.util.Date;
import java.util.List;

public class User {
	private String user_id;
	private String user_name;
	private String user_passwd;
	private String state;
	private String email;
	private String telephone;
	private String address;
	private String user_rank;
	private Date begin_time;
	private Date end_time;
	private String user_permission;
	
	private List<DeviceInfo> device_info;

	

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_passwd() {
		return user_passwd;
	}

	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUser_rank() {
		return user_rank;
	}

	public void setUser_rank(String user_rank) {
		this.user_rank = user_rank;
	}

	public Date getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getUser_permission() {
		return user_permission;
	}

	public void setUser_permission(String user_permission) {
		this.user_permission = user_permission;
	}

	public List<DeviceInfo> getDevice_info() {
		return device_info;
	}

	public void setDevice_info(List<DeviceInfo> device_info) {
		this.device_info = device_info;
	}

	
	
	

}
