/**
 * CInvoice.java	2012/10/30
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
 * CInvoicePropChange -> C_INVOICE_PROP_CHANGE mapping
 */
@POJO(tn = "C_INVOICE_PROP_CHANGE", sn = "", pk = "")
public class CInvoicePropChange extends BusiBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 374344665829672733L;
	private Integer done_code;
	private String old_invoice_id;
	private String old_invoice_code;
	private String old_invoice_book_id;
	private String old_invoice_type;
	private String new_invoice_id;
	private String new_invoice_code;
	private String new_invoice_book_id;
	private String new_invoice_type;
	private Date change_time;

	/**
	 * default empty constructor
	 */
	public CInvoicePropChange() {
	}

	public Integer getDone_code() {
		return done_code;
	}

	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}

	public String getOld_invoice_id() {
		return old_invoice_id;
	}

	public void setOld_invoice_id(String old_invoice_id) {
		this.old_invoice_id = old_invoice_id;
	}

	public String getOld_invoice_code() {
		return old_invoice_code;
	}

	public void setOld_invoice_code(String old_invoice_code) {
		this.old_invoice_code = old_invoice_code;
	}

	public String getOld_invoice_book_id() {
		return old_invoice_book_id;
	}

	public void setOld_invoice_book_id(String old_invoice_book_id) {
		this.old_invoice_book_id = old_invoice_book_id;
	}

	public String getOld_invoice_type() {
		return old_invoice_type;
	}

	public void setOld_invoice_type(String old_invoice_type) {
		this.old_invoice_type = old_invoice_type;
	}

	public String getNew_invoice_id() {
		return new_invoice_id;
	}

	public void setNew_invoice_id(String new_invoice_id) {
		this.new_invoice_id = new_invoice_id;
	}

	public String getNew_invoice_code() {
		return new_invoice_code;
	}

	public void setNew_invoice_code(String new_invoice_code) {
		this.new_invoice_code = new_invoice_code;
	}

	public String getNew_invoice_book_id() {
		return new_invoice_book_id;
	}

	public void setNew_invoice_book_id(String new_invoice_book_id) {
		this.new_invoice_book_id = new_invoice_book_id;
	}

	public String getNew_invoice_type() {
		return new_invoice_type;
	}

	public void setNew_invoice_type(String new_invoice_type) {
		this.new_invoice_type = new_invoice_type;
	}

	public Date getChange_time() {
		return change_time;
	}

	public void setChange_time(Date change_time) {
		this.change_time = change_time;
	}
}