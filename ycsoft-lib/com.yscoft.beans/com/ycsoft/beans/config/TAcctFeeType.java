/**
 * TAcctFeeType.java	2010/07/18
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TAcctFeeType -> T_ACCT_FEE_TYPE mapping
 */
@POJO(tn = "T_ACCT_FEE_TYPE", sn = "", pk = "fee_type")
public class TAcctFeeType implements Serializable {

	// TAcctFeeType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4814933407174912415L;
	private String fee_type;
	private String type_name;
	private Integer priority;
	private String can_refund;
	private String can_trans;
	private String is_cash;

	private String can_refund_text;
	private String can_trans_text;
	private String is_cash_text;

	public String getIs_cash() {
		return is_cash;
	}

	public void setIs_cash(String is_cash) {
		this.is_cash = is_cash;
		is_cash_text = MemoryDict.getDictName("BOOLEAN", is_cash);
	}

	public String getIs_cash_text() {
		return is_cash_text;
	}

	/**
	 * default empty constructor
	 */
	public TAcctFeeType() {
	}

	// fee_type getter and setter
	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	// type_name getter and setter
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	// priority getter and setter
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	// can_refund getter and setter
	public String getCan_refund() {
		return can_refund;
	}

	public void setCan_refund(String can_refund) {
		this.can_refund = can_refund;
		can_refund_text = MemoryDict.getDictName("BOOLEAN", can_refund);
	}

	// can_trans getter and setter
	public String getCan_trans() {
		return can_trans;
	}

	public void setCan_trans(String can_trans) {
		this.can_trans = can_trans;
		can_trans_text = MemoryDict.getDictName("BOOLEAN", can_trans);
	}

	public String getCan_refund_text() {
		return can_refund_text;
	}

	public String getCan_trans_text() {
		return can_trans_text;
	}

}