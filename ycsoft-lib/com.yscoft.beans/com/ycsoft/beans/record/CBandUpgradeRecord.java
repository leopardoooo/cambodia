/**
 * CBandUpgradeRecord.java	2012/12/19
 */
 
package com.ycsoft.beans.record; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * CBandUpgradeRecord -> C_BAND_UPGRADE_RECORD mapping 
 */
@POJO(
	tn="C_BAND_UPGRADE_RECORD",
	sn="",
	pk="")
public class CBandUpgradeRecord implements Serializable {
	
	// CBandUpgradeRecord all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -2161115352008693896L;
	private Integer done_code ;	
	private String cust_id ;	
	private String user_id ;	
	private String acct_id ;	
	private String old_prod_id;
	private String new_prod_id;
	private String old_tariff_id ;	
	private String new_tariff_id ;	
	private String busi_code ;	
	private String change_type ;	
	private String fee_type ;	
	private Integer pre_fee ;	
	private Integer change_fee ;	
	private Integer fee ;	
	private Date create_time;
	private String county_id ;	
	private String area_id ;	
	
	private String old_prod_name;
	private String new_prod_name;
	private String old_tariff_name;
	private String new_tariff_name;
	private String change_type_text;
	private String fee_type_text;
	
	public String getOld_prod_name() {
		return old_prod_name;
	}


	public String getNew_prod_name() {
		return new_prod_name;
	}


	public String getOld_tariff_name() {
		return old_tariff_name;
	}


	public String getNew_tariff_name() {
		return new_tariff_name;
	}


	public String getChange_type_text() {
		return change_type_text;
	}


	public String getFee_type_text() {
		return fee_type_text;
	}


	/**
	 * default empty constructor
	 */
	public CBandUpgradeRecord() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// acct_id getter and setter
	public String getAcct_id(){
		return this.acct_id ;
	}
	
	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}
	
	// old_tariff_id getter and setter
	public String getOld_tariff_id(){
		return this.old_tariff_id ;
	}
	
	public void setOld_tariff_id(String old_tariff_id){
		this.old_tariff_id = old_tariff_id ;
	}
	
	// new_tariff_id getter and setter
	public String getNew_tariff_id(){
		return this.new_tariff_id ;
	}
	
	public void setNew_tariff_id(String new_tariff_id){
		this.new_tariff_id = new_tariff_id ;
	}
	
	// busi_code getter and setter
	public String getBusi_code(){
		return this.busi_code ;
	}
	
	public void setBusi_code(String busi_code){
		this.busi_code = busi_code ;
	}
	
	// change_type getter and setter
	public String getChange_type(){
		return this.change_type ;
	}
	
	public void setChange_type(String change_type){
		this.change_type = change_type ;
		this.change_type_text = MemoryDict.getDictName(DictKey.ACCT_CHANGE_TYPE, change_type);
	}
	
	// fee_type getter and setter
	public String getFee_type(){
		return this.fee_type ;
	}
	
	public void setFee_type(String fee_type){
		this.fee_type = fee_type ;
		this.fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
	}
	
	// pre_fee getter and setter
	public Integer getPre_fee(){
		return this.pre_fee ;
	}
	
	public void setPre_fee(Integer pre_fee){
		this.pre_fee = pre_fee ;
	}
	
	// change_fee getter and setter
	public Integer getChange_fee(){
		return this.change_fee ;
	}
	
	public void setChange_fee(Integer change_fee){
		this.change_fee = change_fee ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}


	public String getOld_prod_id() {
		return old_prod_id;
	}


	public void setOld_prod_id(String old_prod_id) {
		this.old_prod_id = old_prod_id;
	}


	public String getNew_prod_id() {
		return new_prod_id;
	}


	public void setNew_prod_id(String new_prod_id) {
		this.new_prod_id = new_prod_id;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public void setOld_prod_name(String old_prod_name) {
		this.old_prod_name = old_prod_name;
	}


	public void setNew_prod_name(String new_prod_name) {
		this.new_prod_name = new_prod_name;
	}


	public void setOld_tariff_name(String old_tariff_name) {
		this.old_tariff_name = old_tariff_name;
	}


	public void setNew_tariff_name(String new_tariff_name) {
		this.new_tariff_name = new_tariff_name;
	}

}