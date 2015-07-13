/**
 * TCustColonyCfg.java	2012/10/11
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * TNonresCustApproval -> T_NONRES_CUST_APPROVAL mapping 
 */
@POJO(
	tn="T_NONRES_CUST_APPROVAL",
	sn="SEQ_APP_ID",
	pk="APP_CODE")
public class TNonresCustApproval implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4633248231250445390L;
	private String app_id;
	private String app_code;
	private String app_name;
	private String status;
	private String remark;
	
	private String status_text;

	public String getApp_code() {
		return app_code;
	}

	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * default empty constructor
	 */
	public TNonresCustApproval() {}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	public String getStatus_text() {
		return status_text;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

}