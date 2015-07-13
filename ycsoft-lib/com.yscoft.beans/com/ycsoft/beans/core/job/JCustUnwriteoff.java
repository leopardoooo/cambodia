/**
 * JCustUnwriteoff.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JCustUnwriteoff -> J_CUST_UNWRITEOFF mapping
 */
@POJO(tn = "J_CUST_UNWRITEOFF", sn = "", pk = "")
public class JCustUnwriteoff extends BusiBase implements Serializable {

	// JCustUnwriteoff all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1306458492332026306L;
	private Integer job_id;
	private String cust_id;
	private String acctitem_id;
	private String acct_id;
	private String fee_type;
	private Integer amount;

	/**
	 * default empty constructor
	 */
	public JCustUnwriteoff() {
	}

	// job_id getter and setter
	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}



}