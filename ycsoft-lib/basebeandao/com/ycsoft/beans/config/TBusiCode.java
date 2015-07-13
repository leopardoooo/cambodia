/**
 * TBusiCode.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TBusiCode -> T_BUSI_CODE mapping
 */
@POJO(tn = "T_BUSI_CODE", sn = "", pk = "BUSI_CODE")
public class TBusiCode implements Serializable {

	// TBusiCode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -774561741633724108L;
	private String busi_code;
	private String busi_name;
	private String busi_type;
	private String busi_model;
	private String cancel;
	private String ignore;
	private String table_name;

	/**
	 * default empty constructor
	 */
	public TBusiCode() {
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String tableName) {
		table_name = tableName;
	}

	// busi_code getter and setter
	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	// busi_name getter and setter
	public String getBusi_name() {
		return busi_name;
	}

	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}

	// busi_type getter and setter
	public String getBusi_type() {
		return busi_type;
	}

	public void setBusi_type(String busi_type) {
		this.busi_type = busi_type;
	}

	// busi_model getter and setter
	public String getBusi_model() {
		return busi_model;
	}

	public void setBusi_model(String busi_model) {
		this.busi_model = busi_model;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public String getIgnore() {
		return ignore;
	}

	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

}