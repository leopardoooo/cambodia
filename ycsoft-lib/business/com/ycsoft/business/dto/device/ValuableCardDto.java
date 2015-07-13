package com.ycsoft.business.dto.device;

import java.util.Date;

import com.ycsoft.beans.core.valuable.CValuableCard;

public class ValuableCardDto extends CValuableCard {
	
	private static final long serialVersionUID = 5198933635460798863L;
	
	private String invoice_id;
	private String invoice_code;
	private String invoice_book_id;
	private String invoice_mode;
	private Integer real_pay;
	private Date acct_date;
	private String fee_sn;
	private String finance_status;
	private Integer busi_done_code;
	
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
	public String getFee_sn() {
		return fee_sn;
	}
	public void setFee_sn(String feeSn) {
		fee_sn = feeSn;
	}
	public Integer getBusi_done_code() {
		return busi_done_code;
	}
	public void setBusi_done_code(Integer busiDoneCode) {
		busi_done_code = busiDoneCode;
	}
	
}
