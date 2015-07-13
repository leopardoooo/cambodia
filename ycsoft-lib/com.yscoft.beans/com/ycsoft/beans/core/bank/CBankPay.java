/**
 * CBankPay.java	2010/11/01
 */

package com.ycsoft.beans.core.bank;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CBankPay -> C_BANK_PAY mapping
 */
@POJO(tn = "C_BANK_PAY", sn = "", pk = "")
public class CBankPay extends BusiBase implements Serializable {

	// CBankPay all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1900166852867845694L;
	private String trans_id;
	private String mecd_id;
	private String net_code;
	private String banklogid;
	private String bank_operid;
	private String trans_time;
	private String cust_no;
	private Integer late_fee;
	private Integer trans_amount;
	private String pay_mode;
	private Date recieve_date;
	private String invoice_num;
	private String reverse;
	private Date reverse_date;
	private String cancel_done_code;

	/**
	 * default empty constructor
	 */
	public CBankPay() {
	}

	// trans_id getter and setter
	public String getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(String trans_id) {
		this.trans_id = trans_id;
	}

	// mecd_id getter and setter
	public String getMecd_id() {
		return mecd_id;
	}

	public void setMecd_id(String mecd_id) {
		this.mecd_id = mecd_id;
	}

	// net_code getter and setter
	public String getNet_code() {
		return net_code;
	}

	public void setNet_code(String net_code) {
		this.net_code = net_code;
	}

	// banklogid getter and setter
	public String getBanklogid() {
		return banklogid;
	}

	public void setBanklogid(String banklogid) {
		this.banklogid = banklogid;
	}

	// bank_operid getter and setter
	public String getBank_operid() {
		return bank_operid;
	}

	public void setBank_operid(String bank_operid) {
		this.bank_operid = bank_operid;
	}

	// trans_time getter and setter
	public String getTrans_time() {
		return trans_time;
	}

	public void setTrans_time(String trans_time) {
		this.trans_time = trans_time;
	}

	// cust_no getter and setter
	public String getCust_no() {
		return cust_no;
	}

	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	// late_fee getter and setter
	public Integer getLate_fee() {
		return late_fee;
	}

	public void setLate_fee(Integer late_fee) {
		this.late_fee = late_fee;
	}

	// trans_amount getter and setter
	public Integer getTrans_amount() {
		return trans_amount;
	}

	public void setTrans_amount(Integer trans_amount) {
		this.trans_amount = trans_amount;
	}

	// pay_mode getter and setter
	public String getPay_mode() {
		return pay_mode;
	}

	public void setPay_mode(String pay_mode) {
		this.pay_mode = pay_mode;
	}

	// recieve_date getter and setter
	public Date getRecieve_date() {
		return recieve_date;
	}

	public void setRecieve_date(Date recieve_date) {
		this.recieve_date = recieve_date;
	}

	// invoice_num getter and setter
	public String getInvoice_num() {
		return invoice_num;
	}

	public void setInvoice_num(String invoice_num) {
		this.invoice_num = invoice_num;
	}

	// reverse getter and setter
	public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}

	// reverse_date getter and setter
	public Date getReverse_date() {
		return reverse_date;
	}

	public void setReverse_date(Date reverse_date) {
		this.reverse_date = reverse_date;
	}

	// cancel_done_code getter and setter
	public String getCancel_done_code() {
		return cancel_done_code;
	}

	public void setCancel_done_code(String cancel_done_code) {
		this.cancel_done_code = cancel_done_code;
	}

}