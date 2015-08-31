/**
 *
 */
package com.ycsoft.business.dto.core.fee;

import com.ycsoft.beans.core.fee.CFeePay;

/**
 * @author liujiaqi
 * 
 */
public class CFeePayDto extends CFeePay {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4045015007698533843L;
	private String invoice_id;
	private String invoice_code;
	private String invoice_book_id;
	
	private String busi_optr_id;

	/**
	 * @return the invoice_id
	 */
	public String getInvoice_id() {
		return invoice_id;
	}

	/**
	 * @param invoice_id
	 *            the invoice_id to set
	 */
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	/**
	 * @return the invoice_code
	 */
	public String getInvoice_code() {
		return invoice_code;
	}

	/**
	 * @param invoice_code
	 *            the invoice_code to set
	 */
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	/**
	 * @return the invoice_book_id
	 */
	public String getInvoice_book_id() {
		return invoice_book_id;
	}

	/**
	 * @param invoice_book_id
	 *            the invoice_book_id to set
	 */
	public void setInvoice_book_id(String invoice_book_id) {
		this.invoice_book_id = invoice_book_id;
	}



	public String getBusi_optr_id() {
		return busi_optr_id;
	}

	public void setBusi_optr_id(String busi_optr_id) {
		this.busi_optr_id = busi_optr_id;
	}

}
