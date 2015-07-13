/**
 * CFeePayDetail.java	2010/04/08
 */

package com.ycsoft.beans.core.fee;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CFeePayDetail -> C_FEE_PAY_DETAIL mapping
 */
@POJO(tn = "C_FEE_PAY_DETAIL", sn = "", pk = "")
public class CFeePayDetail implements Serializable {

	// CFeePayDetail all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -9057647628815577738L;
	private String pay_sn;
	private String fee_sn;

	/**
	 * default empty constructor
	 */
	public CFeePayDetail() {
	}

	// pay_sn getter and setter
	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	// fee_sn getter and setter
	public String getFee_sn() {
		return fee_sn;
	}

	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}

}