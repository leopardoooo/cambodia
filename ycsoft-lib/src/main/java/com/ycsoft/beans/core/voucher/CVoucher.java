package com.ycsoft.beans.core.voucher;

/**
 * CVoucher.java	2011/01/25
 */
 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * CVoucher -> C_VOUCHER mapping 
 */
@POJO(
	tn="C_VOUCHER",
	sn="",
	pk="voucher_id")
public class CVoucher implements Serializable {
	
	// CVoucher all properties 

	private String voucher_id ;	
	private Integer voucher_value ;	
	private String status ;	
	private Date status_time ;	
	private String for_county_id ;	
	private String optr_id ;	
	private Date used_time ;	
	private Integer used_money ;	
	private Integer unused_money ;	
	private Date exp_time ;	
	private Date create_time;
	private Date invalid_time;
	private String is_procured;
	private String voucher_type;
	private String voucher_type_name;
	
	private String status_text;
	private String optr_name;
	private String for_county_id_text;
	private String is_procured_text;
	private String cust_id;
	private String cust_name;
	
	/**
	 * default empty constructor
	 */
	public CVoucher() {}
	
	
	// voucher_id getter and setter
	public String getVoucher_id(){
		return this.voucher_id ;
	}
	
	public void setVoucher_id(String voucher_id){
		this.voucher_id = voucher_id ;
	}
	
	// voucher_value getter and setter
	public Integer getVoucher_value(){
		return this.voucher_value ;
	}
	
	public void setVoucher_value(Integer voucher_value){
		this.voucher_value = voucher_value ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}
	
	// status_time getter and setter
	public Date getStatus_time(){
		return this.status_time ;
	}
	
	public void setStatus_time(Date status_time){
		this.status_time = status_time ;
	}
	
	
	public String getFor_county_id() {
		return for_county_id;
	}


	public void setFor_county_id(String for_county_id) {
		this.for_county_id = for_county_id;
		this.for_county_id_text = MemoryDict.getDictName(DictKey.COUNTY, for_county_id);
	}


	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	
	// used_time getter and setter
	public Date getUsed_time(){
		return this.used_time ;
	}
	
	public void setUsed_time(Date used_time){
		this.used_time = used_time ;
	}
	
	// used_money getter and setter
	public Integer getUsed_money(){
		return this.used_money ;
	}
	
	public void setUsed_money(Integer used_money){
		this.used_money = used_money ;
	}
	
	// unused_money getter and setter
	public Integer getUnused_money(){
		return this.unused_money ;
	}
	
	public void setUnused_money(Integer unused_money){
		this.unused_money = unused_money ;
	}
	
	// exp_time getter and setter
	public Date getExp_time(){
		return this.exp_time ;
	}
	
	public void setExp_time(Date exp_time){
		this.exp_time = exp_time ;
	}


	public String getStatus_text() {
		return status_text;
	}


	public String getOptr_name() {
		return optr_name;
	}


	public String getFor_county_id_text() {
		return for_county_id_text;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public Date getInvalid_time() {
		return invalid_time;
	}


	public void setInvalid_time(Date invalid_time) {
		this.invalid_time = invalid_time;
	}


	public String getIs_procured() {
		return is_procured;
	}


	public void setIs_procured(String is_procured) {
		this.is_procured = is_procured;
		this.is_procured_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_procured);
	}


	public String getIs_procured_text() {
		return is_procured_text;
	}


	/**
	 * @return the voucher_type
	 */
	public String getVoucher_type() {
		return voucher_type;
	}


	/**
	 * @param voucher_type the voucher_type to set
	 */
	public void setVoucher_type(String voucher_type) {
		this.voucher_type = voucher_type;
		this.voucher_type_name = MemoryDict.getDictName(DictKey.VOUCHER_TYPE, voucher_type);
	}


	public String getVoucher_type_name() {
		return voucher_type_name;
	}


	public String getCust_id() {
		return cust_id;
	}


	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}


	public String getCust_name() {
		return cust_name;
	}


	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
}