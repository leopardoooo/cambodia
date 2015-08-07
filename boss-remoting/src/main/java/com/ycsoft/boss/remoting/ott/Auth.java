package com.ycsoft.boss.remoting.ott;

import com.ycsoft.commons.helper.DateHelper;

public class Auth {
	private String user_id;
	private String product_id;
	private String product_fee_id;
	private String begin_time;
	private String end_time;
	private String state;
	private String[] ott_data;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_fee_id() {
		return product_fee_id;
	}
	public void setProduct_fee_id(String product_fee_id) {
		this.product_fee_id = product_fee_id;
	}
	
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String[] getOtt_data() {
		return ott_data;
	}
	public void setOtt_data(String[] ott_data) {
		this.ott_data = ott_data;
	}
	public Auth(String user_id, String product_id) {
		super();
		this.user_id = user_id;
		this.product_id = product_id;
		this.product_fee_id = "";
		this.state = "0";
		
		this.begin_time = DateHelper.formatNowTime();
		this.end_time ="2050-06-19 08:59:59";
		
		this.ott_data = new String[0];
	}
	
	

}
