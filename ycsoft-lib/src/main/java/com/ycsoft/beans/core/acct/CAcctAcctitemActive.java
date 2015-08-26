/**
 * CAcctAcctitemActive.java	2010/07/12
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemActive -> C_ACCT_ACCTITEM_ACTIVE mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_ACTIVE", sn = "SEQ_ACCT_ACTIVE_SN", pk = "ACCT_ACTIVE_SN")
public class CAcctAcctitemActive extends CountyBase implements Serializable {

	// CAcctAcctitemActive all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4955870017082207777L;
	private String acct_id;
	private String acctitem_id;
	private String fee_type;
	private Integer balance;
	private String acct_active_sn;
	
	private String fee_type_text;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemActive() {
	}

	
	public String getAcct_active_sn() {
		return acct_active_sn;
	}


	public void setAcct_active_sn(String acct_active_sn) {
		this.acct_active_sn = acct_active_sn;
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

	// fee_type getter and setter
	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
		this.fee_type = fee_type;
	}

	// balance getter and setter
	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	/**
	 * @return the fee_type_text
	 */
	public String getFee_type_text() {
		return fee_type_text;
	}

	/**
	 * @param fee_type_text
	 *            the fee_type_text to set
	 */
	public void setFee_type_text(String fee_type_text) {
		this.fee_type_text = fee_type_text;
	}

}