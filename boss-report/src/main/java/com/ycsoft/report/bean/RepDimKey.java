/**
 * RepDimKey.java	2010/07/16
 */

package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepDimKey -> REP_DIM_KEY mapping
 */
@POJO(tn = "REP_DIM_KEY", sn = "", pk = "KEY")
public class RepDimKey implements Serializable {

	// RepDimKey all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1529869634880852316L;
	private String key;
	private String dim_type;
	private String valuedefine;
	private String fkey;
	private String remark;
	private String name;
	private String database;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * default empty constructor
	 */
	public RepDimKey() {
	}

	// key getter and setter
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// dim_type getter and setter
	public String getDim_type() {
		return dim_type;
	}

	public void setDim_type(String dim_type) {
		this.dim_type = dim_type;
	}

	// valuedefine getter and setter
	public String getValuedefine() {
		return valuedefine;
	}

	public void setValuedefine(String valuedefine) {
		this.valuedefine = valuedefine;
	}

	// fkey getter and setter
	public String getFkey() {
		return fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	// name getter and setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}