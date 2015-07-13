/**
 * JCustCreditExec.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JCustCreditExec -> J_CUST_CREDIT_EXEC mapping
 */
@POJO(tn = "J_CUST_CREDIT_EXEC", sn = "", pk = "")
public class JCustCreditExec extends BusiBase implements Serializable {

	// JCustCreditExec all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7901150594149372952L;
	private Integer job_id;
	private String cust_id;
	private String busi;

	/**
	 * default empty constructor
	 */
	public JCustCreditExec() {
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

	public String getBusi() {
		return busi;
	}

	public void setBusi(String busi) {
		this.busi = busi;
	}

}