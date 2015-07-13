package com.ycsoft.beans.core.job;

/**
 * JCustInvalidCal.java	2011/11/21
 */
 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * JCustInvalidCal -> J_CUST_INVALID_CAL mapping 
 */
@POJO(
	tn="J_CUST_ACCTMODE_CAL",
	sn="",
	pk="")
public class JCustAcctmodeCal implements Serializable {
	
	// JCustInvalidCal all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 3419993852941724995L;
	private Integer job_id ;	
	private Integer done_code ;	
	private String cust_id ;	
	private Date create_time ;	
	private String area_id ;	
	private String county_id ;	
	
	/**
	 * default empty constructor
	 */
	public JCustAcctmodeCal() {}
	
	
	// job_id getter and setter
	public Integer getJob_id(){
		return this.job_id ;
	}
	
	public void setJob_id(Integer job_id){
		this.job_id = job_id ;
	}
	
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