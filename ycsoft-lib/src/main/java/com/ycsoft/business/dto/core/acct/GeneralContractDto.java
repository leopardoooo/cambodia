package com.ycsoft.business.dto.core.acct;

import java.util.Date;

import com.ycsoft.beans.core.acct.CGeneralContract;

public class GeneralContractDto extends CGeneralContract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7291294255542620457L;
	
	private String invoice_id;
	private String invoice_code;
	private String invoice_book_id;
	private String invoice_mode;
	private Integer used_amount;
	private Integer left_amount;
	private Integer real_pay;
	private Date acct_date;
	private String finance_status;
	private Integer refund_amount;
	
	public Integer getUsed_amount() {
		return used_amount;
	}
	public void setUsed_amount(Integer used_amount) {
		this.used_amount = used_amount;
	}
	public Integer getLeft_amount() {
		return left_amount;
	}
	public void setLeft_amount(Integer left_amount) {
		this.left_amount = left_amount;
	}
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}
	public String getInvoice_code() {
		return invoice_code;
	}
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}
	public String getInvoice_book_id() {
		return invoice_book_id;
	}
	public void setInvoice_book_id(String invoice_book_id) {
		this.invoice_book_id = invoice_book_id;
	}
	public String getInvoice_mode() {
		return invoice_mode;
	}
	public void setInvoice_mode(String invoice_mode) {
		this.invoice_mode = invoice_mode;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}
	public Date getAcct_date() {
		return acct_date;
	}
	public void setAcct_date(Date acct_date) {
		this.acct_date = acct_date;
	}
	public String getFinance_status() {
		return finance_status;
	}
	public void setFinance_status(String finance_status) {
		this.finance_status = finance_status;
	}
	public Integer getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(Integer refundAmount) {
		refund_amount = refundAmount;
	}
	
}
