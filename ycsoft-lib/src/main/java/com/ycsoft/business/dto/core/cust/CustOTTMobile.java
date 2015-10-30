package com.ycsoft.business.dto.core.cust;

import java.util.Date;

public class CustOTTMobile {

	private String cust_name_prefix;
	private String address;
	private String prod_id;
	private String tariff_id;
	private Date invalid_date;
	private Integer cust_number;
	private String note;
	private String addr_id;
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAddr_id() {
		return addr_id;
	}
	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}
	public String getCust_name_prefix() {
		return cust_name_prefix;
	}
	public void setCust_name_prefix(String cust_name_prefix) {
		this.cust_name_prefix = cust_name_prefix;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}
	public Date getInvalid_date() {
		return invalid_date;
	}
	public void setInvalid_date(Date invalid_date) {
		this.invalid_date = invalid_date;
	}
	public Integer getCust_number() {
		return cust_number;
	}
	public void setCust_number(Integer cust_number) {
		this.cust_number = cust_number;
	}
	
}
