/**
 * TCustUpdateCfg.java	2010/03/24
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TUpdateCfg -> T_UPDATE_CFG mapping
 */
@POJO(tn = "T_UPDATE_CFG", sn = "", pk = "")
public class TUpdateCfg extends TTemplate  implements Serializable {

	// TUpdateCfg all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4442650730193785384L;
	private String busi_code;
	private String field_name;

	private String busi_name;
	private String table_name;

	/**
	 * default empty constructor
	 */
	public TUpdateCfg() {
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

	// field_name getter and setter
	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getBusi_name() {
		return busi_name;
	}

	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}

}