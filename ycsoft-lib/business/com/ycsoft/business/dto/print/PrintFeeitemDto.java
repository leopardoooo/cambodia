/*
 * @(#) PrintFeeitemDto.java 1.0.0 Aug 5, 2011 8:00:52 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ycsoft.business.dto.print;

import java.util.Date;
import java.util.List;



/**
 * 
 *
 * @author allex
 * @since 1.0
 */
public class PrintFeeitemDto {
	private Integer done_code;
	private String busi_code;
	private Integer should_pay;
	private String acctitem_id;
	private String acctitem_name;
	private Integer real_pay;
	private String invoice_id;
	private int count;
	private String printitem_id;
	private String feetitle_remark;
	private String writeoff_date;
	private String disct_name;
	private List<Object> feeItemList;
	private String fee_id;
	private Date invalid_date;
	private String card_id;
	private String stb_id;
	private String modem_mac;
	
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public String getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}
	public Integer getShould_pay() {
		return should_pay;
	}
	public void setShould_pay(Integer should_pay) {
		this.should_pay = should_pay;
	}
	public String getAcctitem_id() {
		return acctitem_id;
	}
	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}
	public String getAcctitem_name() {
		return acctitem_name;
	}
	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}
	public Integer getDone_code() {
		return done_code;
	}
	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getPrintitem_id() {
		return printitem_id;
	}
	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
	public String getFeetitle_remark() {
		return feetitle_remark;
	}
	public void setFeetitle_remark(String feetitle_remark) {
		this.feetitle_remark = feetitle_remark;
	}
	public String getWriteoff_date() {
		return writeoff_date;
	}
	public void setWriteoff_date(String writeoff_date) {
		this.writeoff_date = writeoff_date;
	}
	public List<Object> getFeeItemList() {
		return feeItemList;
	}
	public void setFeeItemList(List<Object> feeItemList) {
		this.feeItemList = feeItemList;
	}
	/**
	 * @return the disct_name
	 */
	public String getDisct_name() {
		return disct_name;
	}
	/**
	 * @param disct_name the disct_name to set
	 */
	public void setDisct_name(String disct_name) {
		this.disct_name = disct_name;
	}
	/**
	 * @return the invalid_date
	 */
	public Date getInvalid_date() {
		return invalid_date;
	}
	/**
	 * @param invalid_date the invalid_date to set
	 */
	public void setInvalid_date(Date invalid_date) {
		this.invalid_date = invalid_date;
	}
	/**
	 * @return the card_id
	 */
	public String getCard_id() {
		return card_id;
	}
	/**
	 * @param card_id the card_id to set
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	/**
	 * @return the stb_id
	 */
	public String getStb_id() {
		return stb_id;
	}
	/**
	 * @param stb_id the stb_id to set
	 */
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}
	/**
	 * @return the modem_mac
	 */
	public String getModem_mac() {
		return modem_mac;
	}
	/**
	 * @param modem_mac the modem_mac to set
	 */
	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}
}
