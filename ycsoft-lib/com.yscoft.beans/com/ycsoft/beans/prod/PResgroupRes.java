/**
 * PResgroupRes.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PResgroupRes -> P_RESGROUP_RES mapping
 */
@POJO(tn = "P_RESGROUP_RES", sn = "", pk = "")
public class PResgroupRes implements Serializable {

	// PResgroupRes all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4839811483446833402L;
	private String group_id;
	private String res_id;

	/**
	 * default empty constructor
	 */
	public PResgroupRes() {
	}

	// group_id getter and setter
	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

}