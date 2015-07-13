/**
 * RBigType.java	2010/06/12
 */

package com.ycsoft.beans.depot;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RBigType -> R_BIG_TYPE mapping
 */
@POJO(tn = "R_BIG_TYPE", sn = "", pk = "")
public class RBigType implements Serializable {

	// RBigType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6326072217695316784L;
	private String big_type_id;
	private String big_type_name;

	/**
	 * default empty constructor
	 */
	public RBigType() {
	}

	// big_type_id getter and setter
	public String getBig_type_id() {
		return big_type_id;
	}

	public void setBig_type_id(String big_type_id) {
		this.big_type_id = big_type_id;
	}

	// big_type_name getter and setter
	public String getBig_type_name() {
		return big_type_name;
	}

	public void setBig_type_name(String big_type_name) {
		this.big_type_name = big_type_name;
	}

}