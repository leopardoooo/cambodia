/**
 * PResPrice.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PResPrice -> P_RES_PRICE mapping
 */
@POJO(tn = "P_RES_PRICE", sn = "", pk = "")
public class PResPrice implements Serializable {

	// PResPrice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3858602215065368830L;
	private String res_id;
	private String tariff_id;
	private String billing_type;
	private String unit;
	private Integer price;
	private String remark;

	/**
	 * default empty constructor
	 */
	public PResPrice() {
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// billing_type getter and setter
	public String getBilling_type() {
		return billing_type;
	}

	public void setBilling_type(String billing_type) {
		this.billing_type = billing_type;
	}

	// unit getter and setter
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	// price getter and setter
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}