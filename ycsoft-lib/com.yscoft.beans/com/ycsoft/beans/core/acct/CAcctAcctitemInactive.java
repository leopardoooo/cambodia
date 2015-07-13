/**
 * CAcctAcctitemInactive.java	2010/07/12
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemInactive -> C_ACCT_ACCTITEM_INACTIVE mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_INACTIVE", sn = "", pk = "")
public class CAcctAcctitemInactive extends BusiBase implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 9064403844060535404L;
	// CAcctAcctitemInactive all properties
	private String promotion_sn;
	private String fee_sn;
	private String acct_id;
	private String acctitem_id;
	private Integer init_amount;
	private Integer use_amount;
	private Integer balance;
	private Integer cycle;
	private Integer active_amount;
	private Date last_active_time;
	private Date next_active_time;
	private Date create_time;
	private String cust_id;


	/**
	 * default empty constructor
	 */
	public CAcctAcctitemInactive() {
	}

	public String getPromotion_sn() {
		return promotion_sn;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
	}

	// fee_sn getter and setter
	public String getFee_sn() {
		return fee_sn;
	}

	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
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

	// init_amount getter and setter
	public Integer getInit_amount() {
		return init_amount;
	}

	public void setInit_amount(Integer init_amount) {
		this.init_amount = init_amount;
	}

	// use_amount getter and setter
	public Integer getUse_amount() {
		return use_amount==null?0:use_amount;
	}

	public void setUse_amount(Integer use_amount) {
		this.use_amount = use_amount;
	}

	// balance getter and setter
	public Integer getBalance() {
		return balance==null?0:balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	// cycle getter and setter
	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	// active_amount getter and setter
	public Integer getActive_amount() {
		return active_amount;
	}

	public void setActive_amount(Integer active_amount) {
		this.active_amount = active_amount;
	}

	// last_active_time getter and setter
	public Date getLast_active_time() {
		return last_active_time;
	}

	public void setLast_active_time(Date last_active_time) {
		this.last_active_time = last_active_time;
	}

	// next_active_time getter and setter
	public Date getNext_active_time() {
		return next_active_time;
	}

	public void setNext_active_time(Date next_active_time) {
		this.next_active_time = next_active_time;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}





}