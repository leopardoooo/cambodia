/**
 * JCustCreditCal.java	2010/06/08
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * JCustCreditCal -> J_CUST_CREDIT_CAL mapping 
 */
@POJO(
	tn="J_CUST_CREDIT_CAL",
	sn="",
	pk="")
public class JCustCreditCal implements Serializable {
	
	// JCustCreditCal all properties 

	private Integer job_id ;	
	private Integer done_code ;	
	private String cust_id ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private Date create_time ;	
	private String area_id ;	
	private String county_id ;	
	private String credit_exec;
	
	
	public String getCredit_exec() {
		return credit_exec;
	}


	public void setCredit_exec(String credit_exec) {
		this.credit_exec = credit_exec;
	}


	/**
	 * default empty constructor
	 */
	public JCustCreditCal() {}
	
	

	
	public Integer getJob_id() {
		return job_id;
	}


	public void setJob_id(Integer job_id) {
		this.job_id = job_id;
	}


	public Integer getDone_code() {
		return done_code;
	}


	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}


	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
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
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}