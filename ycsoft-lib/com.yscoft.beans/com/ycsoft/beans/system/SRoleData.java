/**
 * SRoleData.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SRoleData -> S_ROLE_DATA mapping
 */
@POJO(tn = "S_ROLE_DATA", sn = "", pk = "")
public class SRoleData implements Serializable {

	// SRoleData all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -647312332526694083L;
	private String rule_id;
	private String data_type;
	private String value_id;

	/**
	 * default empty constructor
	 */
	public SRoleData() {
	}

	// rule_id getter and setter
	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	// data_type getter and setter
	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	/**
	 * @return the value_id
	 */
	public String getValue_id() {
		return value_id;
	}

	/**
	 * @param value_id the value_id to set
	 */
	public void setValue_id(String value_id) {
		this.value_id = value_id;
	}



}