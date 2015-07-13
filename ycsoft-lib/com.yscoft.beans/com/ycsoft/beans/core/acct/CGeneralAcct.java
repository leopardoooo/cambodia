package com.ycsoft.beans.core.acct;

/**
 * CGeneralAcct.java	2011/01/24
 */
 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralAcct -> C_GENERAL_ACCT mapping 
 */
@POJO(
	tn="C_GENERAL_ACCT",
	sn="SEQ_ACCT_ID",
	pk="g_acct_id")
public class CGeneralAcct implements Serializable {
	
	// CGeneralAcct all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 9221992261037281580L;
	private String g_acct_id ;	
	private String type ;	
	private String pay_type ;	
	private Integer balance ;	
	private String county_id ;
	
	private String county_name;
	private Integer changeBalance;
	
	/**
	 * default empty constructor
	 */
	public CGeneralAcct() {}
	
	
	// g_acct_id getter and setter
	public String getG_acct_id(){
		return this.g_acct_id ;
	}
	
	public void setG_acct_id(String g_acct_id){
		this.g_acct_id = g_acct_id ;
	}
	
	// type getter and setter
	public String getType(){
		return this.type ;
	}
	
	public void setType(String type){
		this.type = type ;
	}
	
	// pay_type getter and setter
	public String getPay_type(){
		return this.pay_type ;
	}
	
	public void setPay_type(String pay_type){
		this.pay_type = pay_type ;
	}
	
	// balance getter and setter
	public Integer getBalance(){
		return this.balance ;
	}
	
	public void setBalance(Integer balance){
		this.balance = balance ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}


	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}


	public Integer getChangeBalance() {
		return changeBalance;
	}


	public void setChangeBalance(Integer changeBalance) {
		this.changeBalance = changeBalance;
	}

}