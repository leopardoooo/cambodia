/**
 * CAcctAcctitemMerge.java	2011/01/25
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemMerge -> C_ACCT_ACCTITEM_MERGE mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_MERGE", sn = "", pk = "")
public class CAcctAcctitemMerge extends CountyBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2464289642659594282L;
	// CAcctAcctitemMerge all properties
	private Integer done_code;
	private String from_cust_id;
	private String from_acct_id;
	private String from_acctitem_id;
	private String to_cust_id;
	private String to_acct_id;
	private String to_acctitem_id;
	private String fee_type;
	private Integer balance;
	private Date create_time;

	public Integer getDone_code() {
		return done_code;
	}

	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}

	public String getFrom_cust_id() {
		return from_cust_id;
	}

	public void setFrom_cust_id(String from_cust_id) {
		this.from_cust_id = from_cust_id;
	}

	public String getFrom_acct_id() {
		return from_acct_id;
	}

	public void setFrom_acct_id(String from_acct_id) {
		this.from_acct_id = from_acct_id;
	}

	public String getFrom_acctitem_id() {
		return from_acctitem_id;
	}

	public void setFrom_acctitem_id(String from_acctitem_id) {
		this.from_acctitem_id = from_acctitem_id;
	}

	public String getTo_cust_id() {
		return to_cust_id;
	}

	public void setTo_cust_id(String to_cust_id) {
		this.to_cust_id = to_cust_id;
	}

	public String getTo_acct_id() {
		return to_acct_id;
	}

	public void setTo_acct_id(String to_acct_id) {
		this.to_acct_id = to_acct_id;
	}

	public String getTo_acctitem_id() {
		return to_acctitem_id;
	}

	public void setTo_acctitem_id(String to_acctitem_id) {
		this.to_acctitem_id = to_acctitem_id;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemMerge() {
	}

}