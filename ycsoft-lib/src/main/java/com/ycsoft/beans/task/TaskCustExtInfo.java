package com.ycsoft.beans.task;

import com.ycsoft.beans.system.SOptr;

public class TaskCustExtInfo {
	private String cust_no;
	private String area_code;
	private SOptr custManager;
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public SOptr getCustManager() {
		return custManager;
	}
	public void setCustManager(SOptr custManager) {
		this.custManager = custManager;
	}
	public TaskCustExtInfo(String cust_no, String area_code, SOptr custManager) {
		super();
		this.cust_no = cust_no;
		this.area_code = area_code;
		this.custManager = custManager;
	}
	public TaskCustExtInfo() {
		super();
	}
	

}
