/**
 * JCustWriteoffAcct.java	2010/11/03
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * JCustWriteoffAcct -> J_CUST_WRITEOFF_ACCT mapping
 */
@POJO(
	tn="J_CUST_WRITEOFF_ACCT",
	sn="",
	pk="")
public class JCustWriteoffAcct implements Serializable {

	// JCustWriteoffAcct all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6541551506473364351L;
	private Integer job_id ;
	private String acct_id ;
	private String acctitem_id ;
	private Integer done_code;

	/**
	 * default empty constructor
	 */
	public JCustWriteoffAcct() {}


	// job_id getter and setter
	public Integer getJob_id(){
		return job_id ;
	}

	public void setJob_id(Integer job_id){
		this.job_id = job_id ;
	}

	// acct_id getter and setter
	public String getAcct_id(){
		return acct_id ;
	}

	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return acctitem_id ;
	}

	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}


	/**
	 * @return the done_code
	 */
	public Integer getDone_code() {
		return done_code;
	}


	/**
	 * @param doneCode the done_code to set
	 */
	public void setDone_code(Integer doneCode) {
		done_code = doneCode;
	}

}