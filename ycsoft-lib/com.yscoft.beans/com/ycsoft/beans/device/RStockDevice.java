/**
 * RStockDevice.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RStockDevice -> R_STOCK_DEVICE mapping
 */
@POJO(tn = "R_STOCK_DEVICE", sn = "", pk = "")
public class RStockDevice implements Serializable {

	// RStockDevice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6979583189414393684L;
	private String device_id;
	private String type_id;
	private String depot_id;
	private Integer depot_stock;
	private String county_id;
	private String area_id;
	private String remark;

	/**
	 * default empty constructor
	 */
	public RStockDevice() {
	}

	// device_id getter and setter
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	// type_id getter and setter
	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	// depot_id getter and setter
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}

	// depot_stock getter and setter
	public Integer getDepot_stock() {
		return depot_stock;
	}

	public void setDepot_stock(Integer depot_stock) {
		this.depot_stock = depot_stock;
	}

	// county_id getter and setter
	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	// area_id getter and setter
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}