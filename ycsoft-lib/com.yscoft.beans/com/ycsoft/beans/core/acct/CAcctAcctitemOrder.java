/**
 * CAcctAcctitemOrder.java	2010/07/14
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemOrder -> C_ACCT_ACCTITEM_ORDER mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_ORDER", sn = "", pk = "")
public class CAcctAcctitemOrder extends CountyBase implements Serializable {

	// CAcctAcctitemOrder all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4305906064966293606L;
	private String bill_sn;
	private String cust_id;
	private String acct_id;
	private String acctitem_id;
	private String src_acct_id;
	private String src_acctitem_id;
	private Integer amount;
	private Date order_time;
	
	private String acctitem_name;
	private String src_acctitem_name;
	/**
	 * default empty constructor
	 */
	public CAcctAcctitemOrder() {
	}

	// bill_sn getter and setter
	public String getBill_sn() {
		return bill_sn;
	}

	public void setBill_sn(String bill_sn) {
		this.bill_sn = bill_sn;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	// src_acct_id getter and setter
	public String getSrc_acct_id() {
		return src_acct_id;
	}

	public void setSrc_acct_id(String src_acct_id) {
		this.src_acct_id = src_acct_id;
	}

	// src_acctitem_id getter and setter
	public String getSrc_acctitem_id() {
		return src_acctitem_id;
	}

	public void setSrc_acctitem_id(String src_acctitem_id) {
		this.src_acctitem_id = src_acctitem_id;
	}

	// amount getter and setter
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	// order_time getter and setter
	public Date getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Date order_time) {
		this.order_time = order_time;
	}

	public String getAcctitem_name() {
		return acctitem_name;
	}

	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}

	public String getSrc_acctitem_name() {
		return src_acctitem_name;
	}

	public void setSrc_acctitem_name(String src_acctitem_name) {
		this.src_acctitem_name = src_acctitem_name;
	}

}