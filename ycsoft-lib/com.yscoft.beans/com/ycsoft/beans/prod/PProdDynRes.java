/**
 * PProdDynRes.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PProdDynRes -> P_PROD_DYN_RES mapping
 */
@POJO(tn = "P_PROD_DYN_RES", sn = "", pk = "")
public class PProdDynRes implements Serializable {

	// PProdDynRes all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2899719524012315807L;
	private String prod_id;
	private String group_id;
	private Integer res_number;


	/**
	 * default empty constructor
	 */
	public PProdDynRes() {
	}
	public PProdDynRes(String prodId, String groupId, Integer resNumber) {
		prod_id = prodId;
		group_id = groupId;
		res_number =resNumber;
	}
	// prod_id getter and setter
	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	// group_id getter and setter
	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	// res_number getter and setter
	public Integer getRes_number() {
		return res_number;
	}

	public void setRes_number(Integer res_number) {
		this.res_number = res_number;
	}

}