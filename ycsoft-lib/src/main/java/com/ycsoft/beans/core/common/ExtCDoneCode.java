/**
 * ExtCDoneCode.java	2010/05/24
 */

package com.ycsoft.beans.core.common;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * ExtCDoneCode -> EXT_C_DONE_CODE mapping
 */
@POJO(tn = "EXT_C_DONE_CODE", sn = "", pk = "")
public class ExtCDoneCode implements Serializable {

	// ExtCDoneCode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1600351954490162903L;
	private Integer done_code;
	private String attribute_id;
	private String attribute_value;

	/**
	 * default empty constructor
	 */
	public ExtCDoneCode() {
	}



	public Integer getDone_code() {
		return done_code;
	}



	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}



	// attribute_id getter and setter
	public String getAttribute_id() {
		return attribute_id;
	}

	public void setAttribute_id(String attribute_id) {
		this.attribute_id = attribute_id;
	}

	// attribute_value getter and setter
	public String getAttribute_value() {
		return attribute_value;
	}

	public void setAttribute_value(String attribute_value) {
		this.attribute_value = attribute_value;
	}

}