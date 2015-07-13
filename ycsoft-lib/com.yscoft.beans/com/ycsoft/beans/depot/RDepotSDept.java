/**
 * RDepotSDept.java	2010/06/12
 */

package com.ycsoft.beans.depot;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RDepotSDept -> R_DEPOT_S_DEPT mapping
 */
@POJO(tn = "R_DEPOT_S_DEPT", sn = "", pk = "DEPT_ID")
public class RDepotSDept implements Serializable {

	// RDepotSDept all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6065825829157157562L;
	private String depot_id;
	private String dept_id;

	/**
	 * default empty constructor
	 */
	public RDepotSDept() {
	}

	// depot_id getter and setter
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}

	// dept_id getter and setter
	public String getDept_id() {
		return dept_id;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

}