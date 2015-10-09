package com.ycsoft.beans.task;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "W_TASK_LOG", sn = "seq_task_log_sn", pk = "log_sn")
public class WTaskLog  extends BusiBase  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2596878257039088702L;
	private int log_sn;
	private String task_id;
	private Date log_time;
	private String log_detail;
	private String syn_status;
	private Date syn_time;
	private String error_code;
	private String error_remark;
	private String syn_status_text;
	private Date delay_time;
	
	
	
	public Date getDelay_time() {
		return delay_time;
	}
	public void setDelay_time(Date delay_time) {
		this.delay_time = delay_time;
	}
	public int getLog_sn() {
		return log_sn;
	}
	public void setLog_sn(int log_sn) {
		this.log_sn = log_sn;
	}
	public String getSyn_status_text() {
		return syn_status_text;
	}
	public void setSyn_status_text(String syn_status_text) {
		this.syn_status_text = syn_status_text;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getLog_detail() {
		return log_detail;
	}
	public void setLog_detail(String log_detail) {
		this.log_detail = log_detail;
	}
	
	public void setLog_time(Date log_time) {
		this.log_time = log_time;
	}
	public String getSyn_status() {
		return syn_status;
	}
	public void setSyn_status(String syn_status) {
		this.syn_status = syn_status;
		syn_status_text = MemoryDict.getDictName(DictKey.STATUS, syn_status);
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
