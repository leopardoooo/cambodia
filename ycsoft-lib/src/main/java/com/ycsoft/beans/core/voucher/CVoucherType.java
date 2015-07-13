package com.ycsoft.beans.core.voucher;

/**
 * CVoucher.java	2011/01/25
 */
 

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * CVoucher -> C_VOUCHER mapping 
 */
@POJO(
	tn="C_VOUCHER_TYPE",
	sn="",
	pk="VOUCHER_TYPE")
public class CVoucherType implements Serializable {
	private String voucher_type;
	private String voucher_type_name;
	private String rule_id;
	
	private String rule_name;
	
	public String getVoucher_type() {
		return voucher_type;
	}
	public void setVoucher_type(String voucher_type) {
		this.voucher_type = voucher_type;
	}
	public String getVoucher_type_name() {
		return voucher_type_name;
	}
	public void setVoucher_type_name(String voucher_type_name) {
		this.voucher_type_name = voucher_type_name;
	}
	public String getRule_id() {
		return rule_id;
	}
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}
	/**
	 * default empty constructor
	 */
	public CVoucherType() {}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	
}