/**
 * SArea.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SArea -> S_AREA mapping
 */
@POJO(tn = "S_AREA", sn = "", pk = "")
public class SArea implements Serializable {

	// SArea all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3117193286977127747L;
	private String area_id;
	private String area_name;

	/**
	 * default empty constructor
	 */
	public SArea() {
	}

	// area_id getter and setter
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	// area_name getter and setter
	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

}