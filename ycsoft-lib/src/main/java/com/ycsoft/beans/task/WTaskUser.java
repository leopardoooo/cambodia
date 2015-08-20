package com.ycsoft.beans.task;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "W_TASK_USER", sn = "", pk = "task_id,user_id")
public class WTaskUser {
	private String task_id;
	private String user_id;
	private String divice_model;
	private String device_id;
	private String is_valid;
	private String user_type;
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDivice_model() {
		return divice_model;
	}
	public void setDivice_model(String divice_model) {
		this.divice_model = divice_model;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getIs_valid() {
		return is_valid;
	}
	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	
	

}
