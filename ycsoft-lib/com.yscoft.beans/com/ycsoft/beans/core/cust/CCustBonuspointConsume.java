/**
 * CCustBonuspointConsume.java	2011/02/24
 */
 
package com.ycsoft.beans.core.cust; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CCustBonuspointConsume -> C_CUST_BONUSPOINT_CONSUME mapping 
 */
@POJO(
	tn="C_CUST_BONUSPOINT_CONSUME",
	sn="",
	pk="")
public class CCustBonuspointConsume implements Serializable {
	
	// CCustBonuspointConsume all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -3632894698468204624L;
	private Integer done_code ;	
	private Integer sn ;	
	private String cust_id ;	
	private String years ;	
	private String bonuspoint_type ;	
	private Integer consumed_balance ;	
	private Date consume_date ;	
	private String consume_rule ;	
	private String county_id ;	
	private String area_id ;	
	
	/**
	 * default empty constructor
	 */
	public CCustBonuspointConsume() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// sn getter and setter
	public Integer getSn(){
		return this.sn ;
	}
	
	public void setSn(Integer sn){
		this.sn = sn ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// years getter and setter
	public String getYears(){
		return this.years ;
	}
	
	public void setYears(String years){
		this.years = years ;
	}
	
	// bonuspoint_type getter and setter
	public String getBonuspoint_type(){
		return this.bonuspoint_type ;
	}
	
	public void setBonuspoint_type(String bonuspoint_type){
		this.bonuspoint_type = bonuspoint_type ;
	}
	
	// consumed_balance getter and setter
	public Integer getConsumed_balance(){
		return this.consumed_balance ;
	}
	
	public void setConsumed_balance(Integer consumed_balance){
		this.consumed_balance = consumed_balance ;
	}
	
	// consume_date getter and setter
	public Date getConsume_date(){
		return this.consume_date ;
	}
	
	public void setConsume_date(Date consume_date){
		this.consume_date = consume_date ;
	}
	
	// consume_rule getter and setter
	public String getConsume_rule(){
		return this.consume_rule ;
	}
	
	public void setConsume_rule(String consume_rule){
		this.consume_rule = consume_rule ;
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