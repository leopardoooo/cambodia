/**
 * JRecordChange.java	2013/05/07
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO ;


/**
 * JSmsRecord -> J_SMS_RECORD mapping 
 */
@POJO(
	tn="J_SMS_RECORD",
	sn="",
	pk="")
public class JSmsRecord extends CountyBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6064434452750530191L;
	// JSmsRecord all properties 
	private String cust_id;
	private String mobile;
	private Date send_date;
	/**
	 * 
	 */
	
	
	/**
	 * default empty constructor
	 */
	public JSmsRecord() {}


	public String getCust_id() {
		return cust_id;
	}


	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public Date getSend_date() {
		return send_date;
	}


	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	
}