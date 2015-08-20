package com.ycsoft.beans.task;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "W_TASK_LOG", sn = "", pk = "task_id")
public class WTaskLog {
	private String task_id;
	private String busi_code;
	private String optr_id;
	private String log_time;
	private String log_detail;
	private String done_code;
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getLog_time() {
		return log_time;
	}
	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}
	public String getLog_detail() {
		return log_detail;
	}
	public void setLog_detail(String log_detail) {
		this.log_detail = log_detail;
	}
	public String getDone_code() {
		return done_code;
	}
	public void setDone_code(String done_code) {
		this.done_code = done_code;
	}
	
	 

}
