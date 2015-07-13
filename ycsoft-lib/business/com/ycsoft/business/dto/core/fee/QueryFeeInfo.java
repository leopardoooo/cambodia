package com.ycsoft.business.dto.core.fee;

import java.io.Serializable;

public class QueryFeeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5263669008319703535L;
	private String device_code;
	private String status;
	private String create_time1;	//开始时间
	private String create_time2;	//结束时间
	private String optr_name;
	private String invoice_id;
	private String busi_name;
	
	private String bill_done_code;//出账流水
	private String card_id;//对应用户的card
	
	private String device_type;
	private String bill_date1;
	private String bill_date2;
	private String acctitem_name;
	private String tariff_name;
	private String billing_cycle_id;
	private boolean oweFee = true;// 是否是查询欠费账单,true是,false,查询全部,默认查询欠费账单.
	public String getBill_date1() {
		return bill_date1;
	}
	public void setBill_date1(String bill_date1) {
		this.bill_date1 = bill_date1;
	}
	public String getBill_date2() {
		return bill_date2;
	}
	public void setBill_date2(String bill_date2) {
		this.bill_date2 = bill_date2;
	}
	public String getAcctitem_name() {
		return acctitem_name;
	}
	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public String getBilling_cycle_id() {
		return billing_cycle_id;
	}
	public void setBilling_cycle_id(String billing_cycle_id) {
		this.billing_cycle_id = billing_cycle_id;
	}
	public String getDevice_code() {
		return device_code;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public String getCreate_time1() {
		return create_time1;
	}
	public void setCreate_time1(String create_time1) {
		this.create_time1 = create_time1;
	}
	public String getCreate_time2() {
		return create_time2;
	}
	public void setCreate_time2(String create_time2) {
		this.create_time2 = create_time2;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}
	public String getOptr_name() {
		return optr_name;
	}
	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}
	public String getBusi_name() {
		return busi_name;
	}
	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public boolean isOweFee() {
		return oweFee;
	}
	public void setOweFee(boolean oweFee) {
		this.oweFee = oweFee;
	}
	public String getBill_done_code() {
		return bill_done_code;
	}
	public void setBill_done_code(String bill_done_code) {
		this.bill_done_code = bill_done_code;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
}
