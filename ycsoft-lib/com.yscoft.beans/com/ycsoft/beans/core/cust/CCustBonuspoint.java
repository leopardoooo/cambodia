/**
 * CCustBonuspoint.java	2011/02/24
 */
 
package com.ycsoft.beans.core.cust; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CCustBonuspoint -> C_CUST_BONUSPOINT mapping 
 */
@POJO(
	tn="C_CUST_BONUSPOINT",
	sn="",
	pk="CUST_ID")
public class CCustBonuspoint implements Serializable {
	
	// CCustBonuspoint all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4546844941559022548L;
	private String cust_id ;	
	private String bonuspoint_type ;	
	private Integer balance ;	
	private Integer his_balance ;	
	private String county_id ;	
	private String area_id ;	
	
	/**
	 * default empty constructor
	 */
	public CCustBonuspoint() {}
	
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// bonuspoint_type getter and setter
	public String getBonuspoint_type(){
		return this.bonuspoint_type ;
	}
	
	public void setBonuspoint_type(String bonuspoint_type){
		this.bonuspoint_type = bonuspoint_type ;
	}
	
	// balance getter and setter
	public Integer getBalance(){
		return this.balance ;
	}
	
	public void setBalance(Integer balance){
		this.balance = balance ;
	}
	
	// his_balance getter and setter
	public Integer getHis_balance(){
		return this.his_balance ;
	}
	
	public void setHis_balance(Integer his_balance){
		this.his_balance = his_balance ;
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