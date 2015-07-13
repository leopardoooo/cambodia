package com.ycsoft.beans.bill;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * 
 * @author Tom
 *
 */
@POJO(tn = "B_CREDIT_ADDRESS_STOP", sn = "", pk = "ADDR_ID")
public class BCreditAddressStop implements Serializable{

	private static final long serialVersionUID = 2420872176764423966L;
	
	private String addr_id;
	private Integer base_prod_count;
	private String area_id;
	private String county_id;
	
	private String addr_name;
	
	public String getAddr_name() {
		return addr_name;
	}
	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}
	public String getAddr_id() {
		return addr_id;
	}
	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}
	public Integer getBase_prod_count() {
		return base_prod_count;
	}
	public void setBase_prod_count(Integer base_prod_count) {
		this.base_prod_count = base_prod_count;
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	public String getCounty_id() {
		return county_id;
	}
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}
}
