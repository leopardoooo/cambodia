/**
 * TInvoicePrintitem.java	2010/04/12
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TInvoicePrintitem -> T_INVOICE_PRINTITEM mapping
 */
@POJO(tn = "T_INVOICE_PRINTITEM", sn = "", pk = "")
public class TInvoicePrintitem implements Serializable {

	// TInvoicePrintitem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2166573302666990634L;
	private String template_id;
	private String doc_type;
	private String printitem_id;

	private String printitem_name;
	private String doc_name;

	/**
	 * default empty constructor
	 */
	public TInvoicePrintitem() {
	}

	// template_id getter and setter
	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	// doc_type getter and setter
	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}

	// printitem_id getter and setter
	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}

	public String getPrintitem_name() {
		return printitem_name;
	}

	public void setPrintitem_name(String printitem_name) {
		this.printitem_name = printitem_name;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

}