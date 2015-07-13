/**
 *
 */
package com.ycsoft.business.dto.core.acct;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 *
 */
public class PayDto implements Serializable{
	/**
	 */
	private static final long serialVersionUID = 1L;
	private String acct_id ;
	private String acctitem_id ;
	private String cust_id;
	private String user_id;
	private String prod_sn;
	private String tariff_id;
	private String fee_date;
	private String begin_date;
	private String invalid_date;
	private String disct_id;
	private Integer fee;
	private Integer present_fee;
	private String card_id;
	private String next_invalid_date;
	
	private String cust_no;
	private String cust_name;
	private String cust_addr;
	private String user_type;
	private String prod_name;
	private String invoice_id;
	private String invoice_code;
	private String invoice_book_id;
	private String invoice_mode;
	private Integer done_code;
	private String create_time;
	private String user_type_text;

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
	public String getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
	public String getAcctitem_id() {
		return acctitem_id;
	}
	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}
	public String getProd_sn() {
		return prod_sn;
	}
	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}
	public String getFee_date() {
		return fee_date;
	}
	public void setFee_date(String fee_date) {
		this.fee_date = fee_date;
	}
	public String getInvalid_date() {
		return invalid_date;
	}
	public void setInvalid_date(String invalid_date) {
		this.invalid_date = invalid_date;
	}
	public String getDisct_id() {
		return disct_id;
	}
	public void setDisct_id(String disct_id) {
		this.disct_id = disct_id;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public String getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	public Integer getPresent_fee() {
		return present_fee==null?0:present_fee;
	}
	public void setPresent_fee(Integer present_fee) {
		this.present_fee = present_fee;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getCust_addr() {
		return cust_addr;
	}
	public void setCust_addr(String cust_addr) {
		this.cust_addr = cust_addr;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
		this.user_type_text = MemoryDict.getDictName(DictKey.USER_TYPE, user_type);
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
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
	public Integer getDone_code() {
		return done_code;
	}
	public void setDone_code(Integer doneCode) {
		done_code = doneCode;
	}
	public String getInvoice_code() {
		return invoice_code;
	}
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}
	public String getNext_invalid_date() {
		return next_invalid_date;
	}
	public void setNext_invalid_date(String next_invalid_date) {
		this.next_invalid_date = next_invalid_date;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public String getUser_type_text() {
		return user_type_text;
	}
}
