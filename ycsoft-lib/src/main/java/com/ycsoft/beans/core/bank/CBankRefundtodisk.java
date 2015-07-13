/**
 * CBankRefundtodisk.java	2013/09/05
 */
 
package com.ycsoft.beans.core.bank; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * CBankRefundtodisk -> C_BANK_REFUNDTODISK mapping 
 */
@POJO(
	tn="C_BANK_REFUNDTODISK",
	sn="",
	pk="trans_sn")
public class CBankRefundtodisk implements Serializable {
	
	// CBankRefundtodisk all properties 

	private String trans_sn ;	
	private String file_no ;	
	private String cust_id ;	
	private String cust_no ;	
	private String cust_name ;	
	private String bank_code ;	
	private String bank_account ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private String busi_type ;	
	private String bill_sn ;	
	private Date start_date ;	
	private Date end_date ;	
	private String bank_trans_sn ;	
	private String bank_fee_name ;	
	private Integer fee ;	
	private Date create_time ;	
	private String county_id ;	
	private String area_id ;	
	private String user_id;
	
	/**
	 * default empty constructor
	 */
	public CBankRefundtodisk() {}
	
	
	// trans_sn getter and setter
	public String getTrans_sn(){
		return this.trans_sn ;
	}
	
	public void setTrans_sn(String trans_sn){
		this.trans_sn = trans_sn ;
	}
	
	// file_no getter and setter
	public String getFile_no(){
		return this.file_no ;
	}
	
	public void setFile_no(String file_no){
		this.file_no = file_no ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// cust_no getter and setter
	public String getCust_no(){
		return this.cust_no ;
	}
	
	public void setCust_no(String cust_no){
		this.cust_no = cust_no ;
	}
	
	// cust_name getter and setter
	public String getCust_name(){
		return this.cust_name ;
	}
	
	public void setCust_name(String cust_name){
		this.cust_name = cust_name ;
	}
	
	// bank_code getter and setter
	public String getBank_code(){
		return this.bank_code ;
	}
	
	public void setBank_code(String bank_code){
		this.bank_code = bank_code ;
	}
	
	// bank_account getter and setter
	public String getBank_account(){
		return this.bank_account ;
	}
	
	public void setBank_account(String bank_account){
		this.bank_account = bank_account ;
	}
	
	// acct_id getter and setter
	public String getAcct_id(){
		return this.acct_id ;
	}
	
	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}
	
	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return this.acctitem_id ;
	}
	
	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}
	
	// busi_type getter and setter
	public String getBusi_type(){
		return this.busi_type ;
	}
	
	public void setBusi_type(String busi_type){
		this.busi_type = busi_type ;
	}
	
	// bill_sn getter and setter
	public String getBill_sn(){
		return this.bill_sn ;
	}
	
	public void setBill_sn(String bill_sn){
		this.bill_sn = bill_sn ;
	}
	
	// start_date getter and setter
	public Date getStart_date(){
		return this.start_date ;
	}
	
	public void setStart_date(Date start_date){
		this.start_date = start_date ;
	}
	
	// end_date getter and setter
	public Date getEnd_date(){
		return this.end_date ;
	}
	
	public void setEnd_date(Date end_date){
		this.end_date = end_date ;
	}
	
	// bank_trans_sn getter and setter
	public String getBank_trans_sn(){
		return this.bank_trans_sn ;
	}
	
	public void setBank_trans_sn(String bank_trans_sn){
		this.bank_trans_sn = bank_trans_sn ;
	}
	
	// bank_fee_name getter and setter
	public String getBank_fee_name(){
		return this.bank_fee_name ;
	}
	
	public void setBank_fee_name(String bank_fee_name){
		this.bank_fee_name = bank_fee_name ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
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


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}