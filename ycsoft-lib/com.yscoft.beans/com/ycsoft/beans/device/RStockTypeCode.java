/**
 * RStockTypeCode.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RStockTypeCode -> R_STOCK_TYPE_CODE mapping
 */
@POJO(tn = "R_STOCK_TYPE_CODE", sn = "", pk = "")
public class RStockTypeCode implements Serializable {

	// RStockTypeCode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1946247337425550330L;
	private String type_code;
	private String type_name;
	private String type_m_code;

	/**
	 * default empty constructor
	 */
	public RStockTypeCode() {
	}

	// type_code getter and setter
	public String getType_code() {
		return type_code;
	}

	public void setType_code(String type_code) {
		this.type_code = type_code;
	}

	// type_name getter and setter
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	// type_m_code getter and setter
	public String getType_m_code() {
		return type_m_code;
	}

	public void setType_m_code(String type_m_code) {
		this.type_m_code = type_m_code;
	}

}