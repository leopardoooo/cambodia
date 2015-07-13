package com.ycsoft.beans.config;

/**
 * TCountyAcct.java	2012/04/24
 */
 

import java.io.Serializable ;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * TCountyAcct -> T_COUNTY_ACCT mapping 
 */
@POJO(
	tn="T_COUNTY_ACCT",
	sn="SEQ_ACCT_ID",
	pk="t_acct_id")
public class TCountyAcct implements Serializable {
	
	// TCountyAcct all properties 

	private String t_acct_id ;	
	private String county_id ;	
	private Integer initamount ;	
	private Integer balance ;	
	private Integer config_year ;	
	private String optr_id ;	
	private String cust_colony ;
	private Date create_time;
	
	
	private String county_name;
	private String optr_name;
	private String cust_colony_text;
	
	private Integer change_amount;
	
	/**
	 * default empty constructor
	 */
	public TCountyAcct() {}
	
	
	// t_acct_id getter and setter
	public String getT_acct_id(){
		return this.t_acct_id ;
	}
	
	public void setT_acct_id(String t_acct_id){
		this.t_acct_id = t_acct_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		this.county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}
	
	// initamount getter and setter
	public Integer getInitamount(){
		return this.initamount ;
	}
	
	public void setInitamount(Integer initamount){
		this.initamount = initamount ;
	}
	
	// balance getter and setter
	public Integer getBalance(){
		return this.balance ;
	}
	
	public void setBalance(Integer balance){
		this.balance = balance ;
	}
	
	// config_year getter and setter
	public Integer getConfig_year(){
		return this.config_year ;
	}
	
	public void setConfig_year(Integer config_year){
		this.config_year = config_year ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	
	// cust_colony getter and setter
	public String getCust_colony(){
		return this.cust_colony ;
	}
	
	public void setCust_colony(String cust_colony){
		this.cust_colony = cust_colony ;
		this.cust_colony_text = MemoryDict.getDictName(DictKey.CUST_COLONY, cust_colony) ;
	}


	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String countyName) {
		county_name = countyName;
	}


	public String getOptr_name() {
		return optr_name;
	}


	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}


	public String getCust_colony_text() {
		return cust_colony_text;
	}


	public void setCust_colony_text(String custColonyText) {
		cust_colony_text = custColonyText;
	}

	public Integer getChange_amount() {
		return change_amount;
	}


	public void setChange_amount(Integer changeAmount) {
		change_amount = changeAmount;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}


}