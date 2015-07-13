/**
 * RStockType.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RStockType -> R_STOCK_TYPE mapping
 */
@POJO(tn = "R_STOCK_TYPE", sn = "", pk = "")
public class RStockType implements Serializable {

	// RStockType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 138152398239915458L;
	private String type_id;
	private String type_name;
	private String type_code;
	private String prod_type;
	private String prod_code;
	private String type_unit;
	private Integer base_price;
	private String remark;

	/**
	 * default empty constructor
	 */
	public RStockType() {
	}

	// type_id getter and setter
	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	// type_name getter and setter
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	// type_code getter and setter
	public String getType_code() {
		return type_code;
	}

	public void setType_code(String type_code) {
		this.type_code = type_code;
	}

	// prod_type getter and setter
	public String getProd_type() {
		return prod_type;
	}

	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
	}

	// prod_code getter and setter
	public String getProd_code() {
		return prod_code;
	}

	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}

	// type_unit getter and setter
	public String getType_unit() {
		return type_unit;
	}

	public void setType_unit(String type_unit) {
		this.type_unit = type_unit;
	}

	// base_price getter and setter
	public Integer getBase_price() {
		return base_price;
	}

	public void setBase_price(Integer base_price) {
		this.base_price = base_price;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}