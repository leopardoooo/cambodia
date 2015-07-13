package com.ycsoft.beans.core.acct;

/**
 * CGeneralAcctDedail.java	2012/01/05
 */
 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralAcctDedail -> C_GENERAL_ACCT_DEDAIL mapping 
 */
@POJO(
	tn="C_GENERAL_ACCT_DEDAIL",
	sn="",
	pk="")
public class CGeneralAcctDedail implements Serializable {
	
	// CGeneralAcctDedail all properties 

	private String g_acct_id ;	
	private Integer done_code ;	
	private Integer change_fee ;	
	private String county_id ;	
	
	/**
	 * default empty constructor
	 */
	public CGeneralAcctDedail() {}
	
	
	// g_acct_id getter and setter
	public String getG_acct_id(){
		return this.g_acct_id ;
	}
	
	public void setG_acct_id(String g_acct_id){
		this.g_acct_id = g_acct_id ;
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// change_fee getter and setter
	public Integer getChange_fee(){
		return this.change_fee ;
	}
	
	public void setChange_fee(Integer change_fee){
		this.change_fee = change_fee ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}