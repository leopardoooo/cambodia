/**
 * CInvoice.java	2010/04/15
 */

package com.ycsoft.beans.core.print;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CInvoice -> C_INVOICE mapping
 */
@POJO(tn = "C_INVOICE", sn = "", pk = "")
public class CInvoice extends BusiBase implements Serializable {

	// CInvoice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8591584088994396393L;
	private String doc_sn;
	private String invoice_code;
	private String invoice_id;
	private String invoice_book_id;
	private String status;
	private Date print_date;
	private Integer amount;
	private String docitem_data;

	private String status_text;

	/**
	 * default empty constructor
	 */
	public CInvoice() {
	}

	// doc_sn getter and setter
	public String getDoc_sn() {
		return doc_sn;
	}

	public void setDoc_sn(String doc_sn) {
		this.doc_sn = doc_sn;
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

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
		this.status = status;
	}

	// docitem_data getter and setter
	public String getDocitem_data() {
		return docitem_data;
	}

	public void setDocitem_data(String docitem_data) {
		this.docitem_data = docitem_data;
	}

	/**
	 * @return the status_text
	 */
	public String getStatus_text() {
		return status_text;
	}


	/**
	 * @return the print_date
	 */
	public Date getPrint_date() {
		return print_date;
	}

	/**
	 * @param print_date the print_date to set
	 */
	public void setPrint_date(Date print_date) {
		this.print_date = print_date;
	}

	/**
	 * @return the amout
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amout the amout to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getInvoice_book_id() {
		return invoice_book_id;
	}

	public void setInvoice_book_id(String invoice_book_id) {
		this.invoice_book_id = invoice_book_id;
	}

}