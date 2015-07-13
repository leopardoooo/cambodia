/**
 * CCustBonuspointProduce.java	2011/02/24
 */
 
package com.ycsoft.beans.core.cust; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CCustBonuspointProduce -> C_CUST_BONUSPOINT_PRODUCE mapping 
 */
@POJO(
	tn="C_CUST_BONUSPOINT_PRODUCE",
	sn="",
	pk="")
public class CCustBonuspointProduce implements Serializable {
	
	// CCustBonuspointProduce all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 4690830938177742884L;
	private Integer sn ;	
	private String cust_id ;	
	private String years ;	
	private String bonuspoint_type ;	
	private Integer balance ;	
	private Integer consumed_balance ;	
	private Date produce_date ;	
	private Date exp_date ;	
	private String status ;	
	private String produce_attr ;	
	private String produce_value ;	
	private String produce_rule ;	
	private String county_id ;	
	private String area_id ;	
	
	/**
	 * default empty constructor
	 */
	public CCustBonuspointProduce() {}
	
	
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
	
	// balance getter and setter
	public Integer getBalance(){
		return this.balance ;
	}
	
	public void setBalance(Integer balance){
		this.balance = balance ;
	}
	
	// consumed_balance getter and setter
	public Integer getConsumed_balance(){
		return this.consumed_balance ;
	}
	
	public void setConsumed_balance(Integer consumed_balance){
		this.consumed_balance = consumed_balance ;
	}
	
	// produce_date getter and setter
	public Date getProduce_date(){
		return this.produce_date ;
	}
	
	public void setProduce_date(Date produce_date){
		this.produce_date = produce_date ;
	}
	
	// exp_date getter and setter
	public Date getExp_date(){
		return this.exp_date ;
	}
	
	public void setExp_date(Date exp_date){
		this.exp_date = exp_date ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// produce_attr getter and setter
	public String getProduce_attr(){
		return this.produce_attr ;
	}
	
	public void setProduce_attr(String produce_attr){
		this.produce_attr = produce_attr ;
	}
	
	// produce_value getter and setter
	public String getProduce_value(){
		return this.produce_value ;
	}
	
	public void setProduce_value(String produce_value){
		this.produce_value = produce_value ;
	}
	
	// produce_rule getter and setter
	public String getProduce_rule(){
		return this.produce_rule ;
	}
	
	public void setProduce_rule(String produce_rule){
		this.produce_rule = produce_rule ;
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