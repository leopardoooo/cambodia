package com.ycsoft.beans.task;

import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "W_TASK_LOG", sn = "", pk = "task_id")
public class WTaskLog {
	private String task_id;
	private String busi_code;
	private String optr_id;
	private Date log_time;
	private String log_detail;
	private int done_code;
	private String syn_status;
	private Date syn_time;
	private String error_code;
	private String error_remark;
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
	public String getLog_detail() {
		return log_detail;
	}
	public void setLog_detail(String log_detail) {
		this.log_detail = log_detail;
	}
	
	public int getDone_code() {
		return done_code;
	}
	public void setDone_code(int done_code) {
		this.done_code = done_code;
	}
	public void setLog_time(Date log_time) {
		this.log_time = log_time;
	}
	public String getSyn_status() {
		return syn_status;
	}
	public void setSyn_status(String syn_status) {
		this.syn_status = syn_status;
	}
	public Date getSyn_time() {
		return syn_time;
	}
	public void setSyn_time(Date syn_time) {
		this.syn_time = syn_time;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError_remark() {
		return error_remark;
	}
	public void setError_remark(String error_remark) {
		this.error_remark = error_remark;
	}
	public Date getLog_time() {
		return log_time;
	}
	
	

}
