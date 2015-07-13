/**
 * JCustCreateBill.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JCustCreateBill -> J_CUST_CREATE_BILL mapping
 */
@POJO(tn = "J_CUST_CREATE_BILL", sn = "", pk = "")
public class JCustCreateBill extends BusiBase implements Serializable {

	// JCustCreateBill all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6900451521077523947L;
	private Integer job_id;
	private String cust_id;
	private String acct_id;
	private String acctitem_id;

	/**
	 * default empty constructor
	 */
	public JCustCreateBill() {
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

}