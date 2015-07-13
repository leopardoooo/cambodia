package com.ycsoft.business.dto.core.acct;

import java.io.Serializable;

public class QueryAcctitemThresholdDto implements Serializable {

	private String acctitem_id;
	private String task_code;			//任务类型
	private String user_stop_type;		//用户催停标示
	private String user_status;			//用户状态
	private String user_class;			//用户等级
	public String getAcctitem_id() {
		return acctitem_id;
	}
	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}
	public String getTask_code() {
		return task_code;
	}
	public void setTask_code(String task_code) {
		this.task_code = task_code;
	}
	public String getUser_stop_type() {
		return user_stop_type;
	}
	public void setUser_stop_type(String user_stop_type) {
		this.user_stop_type = user_stop_type;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getUser_class() {
		return user_class;
	}
	public void setUser_class(String user_class) {
		this.user_class = user_class;
	}
}
