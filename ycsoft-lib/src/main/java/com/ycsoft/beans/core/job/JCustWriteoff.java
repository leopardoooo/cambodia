/**
 * JCustWriteOff.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JCustWriteOff -> J_CUST_WRITE_OFF mapping
 */
@POJO(tn = "J_CUST_WRITEOFF", sn = "", pk = "JOB_ID")
public class JCustWriteoff extends BusiBase implements Serializable {

	// JCustWriteOff all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2353264118600012866L;
	private Integer job_id;
	private String cust_id;
	private String writeoff;

	/**
	 * default empty constructor
	 */
	public JCustWriteoff() {
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

	public String getWriteoff() {
		return writeoff;
	}

	public void setWriteoff(String writeoff) {
		this.writeoff = writeoff;
	}


}