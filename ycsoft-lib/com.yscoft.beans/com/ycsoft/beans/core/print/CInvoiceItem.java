/**
 * CInvoiceItem.java	2010/04/15
 */

package com.ycsoft.beans.core.print;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CInvoiceItem -> C_INVOICE_ITEM mapping
 */
@POJO(tn = "C_INVOICE_ITEM", sn = "", pk = "")
public class CInvoiceItem implements Serializable {

	// CInvoiceItem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6522374161657255681L;
	private String docitem_sn;
	private String invoice_code;
	private String invoice_id;

	/**
	 * default empty constructor
	 */
	public CInvoiceItem() {
	}

	// docitem_sn getter and setter
	public String getDocitem_sn() {
		return docitem_sn;
	}

	public void setDocitem_sn(String docitem_sn) {
		this.docitem_sn = docitem_sn;
	}

	// invoice_code getter and setter
	public String getInvoice_code() {
		return invoice_code;
	}

	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	// invoice_id getter and setter
	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

}