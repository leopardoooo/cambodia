/**
 * TExtendGroup.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TExtendGroup -> T_EXTEND_GROUP mapping
 */
@POJO(tn = "T_EXTEND_GROUP", sn = "", pk = "")
public class TExtendGroup implements Serializable {

	// TExtendGroup all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7991821207208412849L;
	private String extend_id;
	private String group_id;
	private String group_name;

	/**
	 * default empty constructor
	 */
	public TExtendGroup() {
	}

	// extend_id getter and setter
	public String getExtend_id() {
		return extend_id;
	}

	public void setExtend_id(String extend_id) {
		this.extend_id = extend_id;
	}

	// group_id getter and setter
	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	// group_name getter and setter
	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

}