package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "R_DEVICE_TYPE", sn = "", pk = "DEVICE_TYPE")
public class RDeviceType  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4096370741967818064L;
	private String device_type;
	private String type_name;
	private String manage_detail;
	
	
	
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getManage_detail() {
		return manage_detail;
	}
	public void setManage_detail(String manage_detail) {
		this.manage_detail = manage_detail;
	}
	


}
