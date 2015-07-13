/**
 * RStockTypeMCode.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RStockTypeMCode -> R_STOCK_TYPE_M_CODE mapping
 */
@POJO(tn = "R_STOCK_TYPE_M_CODE", sn = "", pk = "")
public class RStockTypeMCode implements Serializable {

	// RStockTypeMCode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1764082802857092911L;
	private String type_m_code;
	private String type_m_name;
	private String big_type_id;

	/**
	 * default empty constructor
	 */
	public RStockTypeMCode() {
	}

	// type_m_code getter and setter
	public String getType_m_code() {
		return type_m_code;
	}

	public void setType_m_code(String type_m_code) {
		this.type_m_code = type_m_code;
	}

	// type_m_name getter and setter
	public String getType_m_name() {
		return type_m_name;
	}

	public void setType_m_name(String type_m_name) {
		this.type_m_name = type_m_name;
	}

	// big_type_id getter and setter
	public String getBig_type_id() {
		return big_type_id;
	}

	public void setBig_type_id(String big_type_id) {
		this.big_type_id = big_type_id;
	}

}