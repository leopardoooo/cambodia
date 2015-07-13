/**
 * CAcct.java	2010/06/08
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CAcct -> C_ACCT mapping
 */
@POJO(tn = "C_ACCT", sn = "SEQ_ACCT_ID", pk = "ACCT_ID")
public class CAcct extends BusiBase implements Serializable {

	// CAcct all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3016492631349894708L;
	private String acct_id;
	private String cust_id;
	private String user_id;
	private String acct_type;
	private String pay_type;
	private Date open_time;

	private String acct_type_text;

	/**
	 * default empty constructor
	 */
	public CAcct() {
	}

	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// acct_type getter and setter
	public String getAcct_type() {
		return acct_type;
	}

	public void setAcct_type(String acct_type) {
		acct_type_text = MemoryDict.getDictName(DictKey.ACCT_TYPE, acct_type);
		this.acct_type = acct_type;
	}

	// pay_type getter and setter
	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	// open_time getter and setter
	public Date getOpen_time() {
		return open_time;
	}

	public void setOpen_time(Date open_time) {
		this.open_time = open_time;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the acct_type_text
	 */
	public String getAcct_type_text() {
		return acct_type_text;
	}

	/**
	 * @param acct_type_text
	 *            the acct_type_text to set
	 */
	public void setAcct_type_text(String acct_type_text) {
		this.acct_type_text = acct_type_text;
	}

}