/**
 * CCustDeviceBuymodeProp.java	2012/08/13
 */
 
package com.ycsoft.beans.core.cust; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CCustDeviceBuymodeProp -> C_CUST_DEVICE_BUYMODE_PROP mapping 
 */
@POJO(
	tn="C_CUST_DEVICE_BUYMODE_PROP",
	sn="",
	pk="")
public class CCustDeviceBuymodeProp implements Serializable {
	
	// CCustDeviceBuymodeProp all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4093559525443738169L;
	private Integer done_code ;	
	private String cust_id ;	
	private String device_type ;	
	private String old_device_id ;	
	private String old_device_code ;	
	private String old_buy_mode ;	
	private String new_device_id ;	
	private String new_device_code ;	
	private String new_buy_mode ;	
	private Date create_time ;	
	private String optr_id ;	
	private String county_id ;	
	private String area_id ;	
	
	/**
	 * default empty constructor
	 */
	public CCustDeviceBuymodeProp() {}
	
	
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
	
	// device_type getter and setter
	public String getDevice_type(){
		return this.device_type ;
	}
	
	public void setDevice_type(String device_type){
		this.device_type = device_type ;
	}
	
	// old_device_id getter and setter
	public String getOld_device_id(){
		return this.old_device_id ;
	}
	
	public void setOld_device_id(String old_device_id){
		this.old_device_id = old_device_id ;
	}
	
	// old_device_code getter and setter
	public String getOld_device_code(){
		return this.old_device_code ;
	}
	
	public void setOld_device_code(String old_device_code){
		this.old_device_code = old_device_code ;
	}
	
	// old_buy_mode getter and setter
	public String getOld_buy_mode(){
		return this.old_buy_mode ;
	}
	
	public void setOld_buy_mode(String old_buy_mode){
		this.old_buy_mode = old_buy_mode ;
	}
	
	// new_device_id getter and setter
	public String getNew_device_id(){
		return this.new_device_id ;
	}
	
	public void setNew_device_id(String new_device_id){
		this.new_device_id = new_device_id ;
	}
	
	// new_device_code getter and setter
	public String getNew_device_code(){
		return this.new_device_code ;
	}
	
	public void setNew_device_code(String new_device_code){
		this.new_device_code = new_device_code ;
	}
	
	// new_buy_mode getter and setter
	public String getNew_buy_mode(){
		return this.new_buy_mode ;
	}
	
	public void setNew_buy_mode(String new_buy_mode){
		this.new_buy_mode = new_buy_mode ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
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

}