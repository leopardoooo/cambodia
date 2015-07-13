/**
 * TBusiCodeFee.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TBusiCodeFee -> T_BUSI_CODE_FEE mapping
 */
@POJO(tn = "T_BUSI_CODE_FEE", sn = "", pk = "")
public class TBusiCodeFee implements Serializable {

	// TBusiCodeFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1272826033899303633L;
	private String busi_code;
	private String fee_id;

	/**
	 * default empty constructor
	 */
	public TBusiCodeFee() {
	}

	// busi_code getter and setter
	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	// fee_id getter and setter
	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}


}