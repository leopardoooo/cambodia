/**
 * SOptrResource.java	2010/04/23
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SOptrResource -> S_OPTR_RESOURCE mapping
 */
@POJO(tn = "S_OPTR_RESOURCE", sn = "", pk = "")
public class SOptrResource implements Serializable {

	// SOptrResource all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6076950056328724867L;
	private String optr_id;
	private String optr_name;
	private String res_name;
	private String res_id;
	private Integer more_or_less;

	/**
	 * default empty constructor
	 */
	public SOptrResource() {
	}

	// optr_id getter and setter
	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	// more_or_less getter and setter
	public Integer getMore_or_less() {
		return more_or_less;
	}

	public void setMore_or_less(Integer more_or_less) {
		this.more_or_less = more_or_less;
	}

	public String getOptr_name() {
		return optr_name;
	}

	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}

	public String getRes_name() {
		return res_name;
	}

	public void setRes_name(String resName) {
		res_name = resName;
	}

}