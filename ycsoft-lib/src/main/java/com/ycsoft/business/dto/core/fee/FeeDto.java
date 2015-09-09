/**
 *
 */
package com.ycsoft.business.dto.core.fee;

import java.util.Date;

import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class FeeDto extends CFee {

	/**
	 *
	 */
	private static final long serialVersionUID = -6203704351553620392L;
	private String acct_type;
	private String acct_type_text;

	private String fee_text ;//显示的费用名称或账目项名称
	private String count_text;//费用订购数量描述

	private String device_type;
	private String device_type_text;

	private String device_code;
	private String doc_type;
	private String user_type;
	private String user_type_text;
	private String user_name;

	private String deposit;
	
	private String new_invoice_id;
	private String new_invoice_code;
	private String new_invoice_book_id;
	
	private Date begin_date;
	private Date prod_invalid_date;
	private String prod_sn;
	
	//用户是否销户
	private String is_logoff;
	private String finance_status;
	private String data_right;//权限级别
	private String is_busi_fee;
	private String doc_type_text;
	private String allow_done_code;
	
	private String device_model;
	private String device_model_name;

	
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public String getDevice_model_name() {
		return device_model_name;
	}
	public void setDevice_model_name(String device_model_name) {
		this.device_model_name = device_model_name;
	}
	public String getCount_text() {
		return count_text;
	}
	public void setCount_text(String count_text) {
		this.count_text = count_text;
	}
	public String getProd_sn() {
		return prod_sn;
	}
	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}
	/**
	 * @return the deposit
	 */
	public String getDeposit() {
		return deposit;
	}
	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	/**
	 * @return the user_type_text
	 */
	public String getUser_type_text() {
		return user_type_text;
	}
	/**
	 * @param user_type_text the user_type_text to set
	 */
	public void setUser_type_text(String user_type_text) {
		this.user_type_text = user_type_text;
	}
	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}
	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	/**
	 * @return the acct_type
	 */
	public String getAcct_type() {
		return acct_type;
	}
	/**
	 * @param acct_type the acct_type to set
	 */
	public void setAcct_type(String acct_type) {
		acct_type_text = MemoryDict.getDictName(DictKey.ACCT_TYPE, acct_type);
		this.acct_type = acct_type;
	}
	/**
	 * @return the acct_type_text
	 */
	public String getAcct_type_text() {
		return acct_type_text;
	}
	/**
	 * @return the device_type
	 */
	public String getDevice_type() {
		return device_type;
	}
	/**
	 * @param device_type the device_type to set
	 */
	public void setDevice_type(String device_type) {
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE, device_type);
		this.device_type = device_type;
	}
	/**
	 * @return the device_code
	 */
	public String getDevice_code() {
		return device_code;
	}
	/**
	 * @param device_code the device_code to set
	 */
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getFee_text() {
		return fee_text;
	}
	public void setFee_text(String fee_text) {
		this.fee_text = fee_text;
	}
	public String getDevice_type_text() {
		return device_type_text;
	}
	/**
	 * @return the new_invoice_id
	 */
	public String getNew_invoice_id() {
		return new_invoice_id;
	}
	/**
	 * @param new_invoice_id the new_invoice_id to set
	 */
	public void setNew_invoice_id(String new_invoice_id) {
		this.new_invoice_id = new_invoice_id;
	}
	/**
	 * @return the new_invoice_code
	 */
	public String getNew_invoice_code() {
		return new_invoice_code;
	}
	/**
	 * @param new_invoice_code the new_invoice_code to set
	 */
	public void setNew_invoice_code(String new_invoice_code) {
		this.new_invoice_code = new_invoice_code;
	}
	/**
	 * @return the new_invoice_book_id
	 */
	public String getNew_invoice_book_id() {
		return new_invoice_book_id;
	}
	/**
	 * @param new_invoice_book_id the new_invoice_book_id to set
	 */
	public void setNew_invoice_book_id(String new_invoice_book_id) {
		this.new_invoice_book_id = new_invoice_book_id;
	}
	
	public Date getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}
	public Date getProd_invalid_date() {
		return prod_invalid_date;
	}
	
	public void setProd_invalid_date(Date prod_invalid_date) {
		this.prod_invalid_date = prod_invalid_date;
	}
	/**
	 * @return the is_logoff
	 */
	public String getIs_logoff() {
		return is_logoff;
	}
	/**
	 * @param is_logoff the is_logoff to set
	 */
	public void setIs_logoff(String is_logoff) {
		this.is_logoff = is_logoff;
	}
	public String getFinance_status() {
		return finance_status;
	}
	public void setFinance_status(String finance_status) {
		this.finance_status = finance_status;
	}
	public String getData_right() {
		return data_right;
	}
	public void setData_right(String data_right) {
		this.data_right = data_right;
	}
	public String getIs_busi_fee() {
		return is_busi_fee;
	}
	public void setIs_busi_fee(String isBusiFee) {
		is_busi_fee = isBusiFee;
	}
	/**
	 * @return the doc_type
	 */
	public String getDoc_type() {
		return doc_type;
	}
	/**
	 * @param doc_type the doc_type to set
	 */
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
		this.doc_type_text = MemoryDict.getDictName(DictKey.INVOICE_TYPE, doc_type);
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getDoc_type_text() {
		return doc_type_text;
	}
	public String getAllow_done_code() {
		return allow_done_code;
	}
	public void setAllow_done_code(String allow_done_code) {
		this.allow_done_code = allow_done_code;
	}


}
