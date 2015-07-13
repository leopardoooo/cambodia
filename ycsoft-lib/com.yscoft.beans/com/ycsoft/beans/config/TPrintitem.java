/**
 * TPrintitem.java	2010/04/12
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TPrintitem -> T_PRINTITEM mapping
 */
@POJO(tn = "T_PRINTITEM", sn = "SEQ_PRINTITEM", pk = "printitem_id")
public class TPrintitem implements Serializable {

	// TPrintitem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2009850577603511926L;
	private String printitem_id;
	private String printitem_name;

	/**
	 * default empty constructor
	 */
	public TPrintitem() {
	}

	// printitem_id getter and setter
	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}

	// printitem_name getter and setter
	public String getPrintitem_name() {
		return printitem_name;
	}

	public void setPrintitem_name(String printitem_name) {
		this.printitem_name = printitem_name;
	}

}