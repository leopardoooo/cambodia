package com.ycsoft.business.dto.core.user;

import java.io.Serializable;

public class ChangedUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1111580704625553711L;
	
	private java.lang.String user_id;
	
	private java.lang.String data_type;

    private java.lang.String card_id;

    private java.lang.String cust_name;

    private java.lang.String address;

    private java.lang.String mobile;

    private java.lang.String id_num;

    private java.lang.String old_card_id;
    
    private String change_time;

    private java.lang.String area_id;

    private java.lang.String county_id;

	public java.lang.String getData_type() {
		return data_type;
	}

	public void setData_type(java.lang.String data_type) {
		this.data_type = data_type;
	}

	public java.lang.String getCard_id() {
		return card_id;
	}

	public void setCard_id(java.lang.String card_id) {
		this.card_id = card_id;
	}

	public java.lang.String getCust_name() {
		return cust_name;
	}

	public void setCust_name(java.lang.String cust_name) {
		this.cust_name = cust_name;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getId_num() {
		return id_num;
	}

	public void setId_num(java.lang.String id_num) {
		this.id_num = id_num;
	}

	public java.lang.String getOld_card_id() {
		return old_card_id;
	}

	public void setOld_card_id(java.lang.String old_card_id) {
		this.old_card_id = old_card_id;
	}

	public java.lang.String getArea_id() {
		return area_id;
	}

	public void setArea_id(java.lang.String area_id) {
		this.area_id = area_id;
	}

	public java.lang.String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(java.lang.String county_id) {
		this.county_id = county_id;
	}

	public String getChange_time() {
		return change_time;
	}

	public void setChange_time(String change_time) {
		this.change_time = change_time;
	}

	public java.lang.String getUser_id() {
		return user_id;
	}

	public void setUser_id(java.lang.String user_id) {
		this.user_id = user_id;
	}

}
