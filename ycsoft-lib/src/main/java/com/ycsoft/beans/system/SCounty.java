/**
 * SCounty.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SCounty -> S_COUNTY mapping
 */
@POJO(tn = "S_COUNTY", sn = "", pk = "")
public class SCounty implements Serializable {

	// SCounty all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1025585696730654415L;
	private String county_id;
	private String county_name;
	private String area_id;

	/**
	 * default empty constructor
	 */
	public SCounty() {
	}

	// county_id getter and setter
	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	// county_name getter and setter
	public String getCounty_name() {
		return county_name;
	}

	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

	// area_id getter and setter
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

}