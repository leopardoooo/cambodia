/**
 * TBusiCodeTask.java	2010/03/04
 */

package com.ycsoft.beans.task;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TBusiCodeTask -> T_BUSI_CODE_TASK mapping
 */
@POJO(tn = "T_BUSI_CODE_TASK", sn = "", pk = "")
public class TBusiCodeTask implements Serializable {

	// TBusiCodeTask all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1682195361285897169L;
	private String template_id;
	private String busi_code;
	private String detail_type_id;

	private String busi_name;
	private String detail_type_name;

	public String getBusi_name() {
		return busi_name;
	}

	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}

	public String getDetail_type_name() {
		return detail_type_name;
	}

	public void setDetail_type_name(String detail_type_name) {
		this.detail_type_name = detail_type_name;
	}

	/**
	 * default empty constructor
	 */
	public TBusiCodeTask() {
	}

	// template_id getter and setter
	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	// busi_code getter and setter
	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	// detail_type_id getter and setter
	public String getDetail_type_id() {
		return detail_type_id;
	}

	public void setDetail_type_id(String detail_type_id) {
		this.detail_type_id = detail_type_id;
	}

}