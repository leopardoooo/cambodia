/**
 * TPublicAcctitem.java	2010/06/18
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TPublicAcctitem -> T_PUBLIC_ACCTITEM mapping
 */
@POJO(tn = "T_PUBLIC_ACCTITEM", sn = "", pk = "acctitem_id")
public class TPublicAcctitem implements Serializable {

	// TPublicAcctitem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8964303643874203855L;
	private String acctitem_id;
	private String acctitem_name;
	private String acctitem_type;
	private String printitem_id;

	/**
	 * default empty constructor
	 */
	public TPublicAcctitem() {
	}

	// acctitem_id getter and setter
	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	// acctitem_name getter and setter
	public String getAcctitem_name() {
		return acctitem_name;
	}

	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}

	// acctitem_type getter and setter
	public String getAcctitem_type() {
		return acctitem_type;
	}

	public void setAcctitem_type(String acctitem_type) {
		this.acctitem_type = acctitem_type;
	}

	// printitem_id getter and setter
	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}

}