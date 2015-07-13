/**
 * CProdSyn.java	2011/10/19
 */
 
package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * CProdSyn -> C_PROD_SYN mapping 
 */
@POJO(
	tn="C_PROD_SYN",
	sn="",
	pk="")
public class CProdSyn implements Serializable {
	
	// CProdSyn all properties 

	private Integer done_code ;	
	private String cust_id ;	
	private String acct_id ;	
	private String soruce_user_id ;	
	private String order_user_id ;	
	private String prod_id ;	
	private String is_new_prod ;	
	private Date old_invalid_date ;	
	private Date new_invalid_date ;	
	private Integer fee ;	
	private String area_id ;	
	private String county_id ;	
	private Date create_time ;	
	
	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}


	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	/**
	 * default empty constructor
	 */
	public CProdSyn() {}
	
	
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
	
	// acct_id getter and setter
	public String getAcct_id(){
		return this.acct_id ;
	}
	
	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}
	
	// soruce_user_id getter and setter
	public String getSoruce_user_id(){
		return this.soruce_user_id ;
	}
	
	public void setSoruce_user_id(String soruce_user_id){
		this.soruce_user_id = soruce_user_id ;
	}
	
	// order_user_id getter and setter
	public String getOrder_user_id(){
		return this.order_user_id ;
	}
	
	public void setOrder_user_id(String order_user_id){
		this.order_user_id = order_user_id ;
	}
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// is_new_prod getter and setter
	public String getIs_new_prod(){
		return this.is_new_prod ;
	}
	
	public void setIs_new_prod(String is_new_prod){
		this.is_new_prod = is_new_prod ;
	}
	
	// old_invalid_date getter and setter
	public Date getOld_invalid_date(){
		return this.old_invalid_date ;
	}
	
	public void setOld_invalid_date(Date old_invalid_date){
		this.old_invalid_date = old_invalid_date ;
	}
	
	// new_invalid_date getter and setter
	public Date getNew_invalid_date(){
		return this.new_invalid_date ;
	}
	
	public void setNew_invalid_date(Date new_invalid_date){
		this.new_invalid_date = new_invalid_date ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
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