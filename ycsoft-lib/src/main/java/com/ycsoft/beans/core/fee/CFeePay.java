/**
 * CFeePay.java	2010/04/08
 */

package com.ycsoft.beans.core.fee;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CFeePay -> C_FEE_PAY mapping
 */
@POJO(tn = "C_FEE_PAY", sn = "SEQ_PAY_SN", pk = "PAY_SN")
public class CFeePay extends BusiBase implements Serializable {

	// CFeePay all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7263906148592323837L;
	private String pay_sn;
	private Integer reverse_done_code;
	private String cust_id;
	private String pay_type;
	private String receipt_id;
	private String is_valid;
	private String payer;
	private Date acct_date;
	private String invoice_mode;
	private String remark;
	private Integer usd;
	private Integer khr;
	private String  exchange;	

	public Integer getUsd() {
		return usd;
	}

	public void setUsd(Integer usd) {
		this.usd = usd;
	}

	public Integer getKhr() {
		return khr;
	}

	public void setKhr(Integer khr) {
		this.khr = khr;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	/**
	 * default empty constructor
	 */
	public CFeePay() {
	}

	// pay_sn getter and setter
	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	// reverse_done_code getter and setter
	public Integer getReverse_done_code() {
		return reverse_done_code;
	}

	public void setReverse_done_code(Integer reverse_done_code) {
		this.reverse_done_code = reverse_done_code;
	}

	// pay_type getter and setter
	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	// receipt_id getter and setter
	public String getReceipt_id() {
		return receipt_id;
	}

	public void setReceipt_id(String receipt_id) {
		this.receipt_id = receipt_id;
	}

	// is_valid getter and setter
	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	// payer getter and setter
	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the acct_date
	 */
	public Date getAcct_date() {
		return acct_date;
	}

	/**
	 * @param acct_date the acct_date to set
	 */
	public void setAcct_date(Date acct_date) {
		this.acct_date = acct_date;
	}

	/**
	 * @return the invoice_mode
	 */
	public String getInvoice_mode() {
		return invoice_mode;
	}

	/**
	 * @param invoice_mode the invoice_mode to set
	 */
	public void setInvoice_mode(String invoice_mode) {
		this.invoice_mode = invoice_mode;
	}

	/**
	 * @return the cust_id
	 */
	public String getCust_id() {
		return cust_id;
	}

	/**
	 * @param cust_id the cust_id to set
	 */
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}




}