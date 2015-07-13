/**
 * RDevice.java	2013/01/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RDeviceUseRecords -> R_DEVICE_USE_RECORDS mapping
 */
@POJO(tn = "R_DEVICE_USE_RECORDS", sn = "", pk = "")
public class RDeviceUseRecords extends OptrBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 192137922276462709L;
	// RDeviceUseRecords all properties

	private Integer done_code;
	private String device_id;
	private String device_type;
	private String device_code;
	private String busi_code;
	private String cust_id;
	private String cust_no;
	private Date done_date;
	
	private String busi_name;
	private String cust_name;
	
	public Integer getDone_code() {
		return done_code;
	}
	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDevice_code() {
		return device_code;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
		this.busi_name = MemoryDict.getDictName(DictKey.BUSI_CODE, busi_code);
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public Date getDone_date() {
		return done_date;
	}
	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getBusi_name() {
		return busi_name;
	}

}