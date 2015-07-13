/**
 * TExtend.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TExtend -> T_EXTEND mapping
 */
@POJO(tn = "T_EXTEND", sn = "SEQ_EXTTABLE_SN", pk = "EXTEND_ID")
public class TExtend implements Serializable {

	// TExtend all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7193868078828452871L;
	private String extend_id;
	private String extend_name;
	private String extend_type;
	private String extend_table;
	private String extend_table_pk;
	private String busi_code;

	/**
	 * default empty constructor
	 */
	public TExtend() {
	}

	// extend_id getter and setter
	public String getExtend_id() {
		return extend_id;
	}

	public void setExtend_id(String extend_id) {
		this.extend_id = extend_id;
	}

	// extend_name getter and setter
	public String getExtend_name() {
		return extend_name;
	}

	public void setExtend_name(String extend_name) {
		this.extend_name = extend_name;
	}

	// extend_table getter and setter
	public String getExtend_table() {
		return extend_table;
	}

	public void setExtend_table(String extend_table) {
		this.extend_table = extend_table;
	}

	public String getExtend_table_pk() {
		return extend_table_pk;
	}

	public void setExtend_table_pk(String extend_table_pk) {
		this.extend_table_pk = extend_table_pk;
	}

	/**
	 * @return the busi_code
	 */
	public String getBusi_code() {
		return busi_code;
	}

	/**
	 * @param busi_code
	 *            the busi_code to set
	 */
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	/**
	 * @return the extend_type
	 */
	public String getExtend_type() {
		return extend_type;
	}

	/**
	 * @param extend_type
	 *            the extend_type to set
	 */
	public void setExtend_type(String extend_type) {
		this.extend_type = extend_type;
	}

}