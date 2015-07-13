/**
 * TCustColonyCfg.java	2012/04/24
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * TCustColonyCfg -> T_CUST_COLONY_CFG mapping 
 */
@POJO(
	tn="T_CUST_COLONY_CFG",
	sn="",
	pk="")
public class TCustColonyCfg extends BusiBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5034659696619284286L;
	// TCustColonyCfg all properties 

	private String year_date ;	
	private Integer cust_num ;	
	private Integer use_num ;	
	private String cust_colony ;	
	private String county_id_for;
	private String cust_colony_text;
	private String county_name_for;
	private String status;
	private String cust_class;
	private String cust_class_text;
	private Integer user_num;


	/**
	 * default empty constructor
	 */
	public TCustColonyCfg() {}
	
	

	public String getCounty_name_for() {
		return county_name_for;
	}

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getCust_colony_text() {
		return cust_colony_text;
	}

	public String getYear_date() {
		return year_date;
	}

	public void setYear_date(String yearDate) {
		year_date = yearDate;
	}

	// cust_num getter and setter
	public Integer getCust_num(){
		return this.cust_num ;
	}
	
	public void setCust_num(Integer cust_num){
		this.cust_num = cust_num ;
	}
	
	// use_num getter and setter
	public Integer getUse_num(){
		return this.use_num ;
	}
	
	public void setUse_num(Integer use_num){
		this.use_num = use_num ;
	}
	
	// cust_colony getter and setter
	public String getCust_colony(){
		return this.cust_colony ;
	}
	
	public void setCust_colony(String cust_colony){
		this.cust_colony = cust_colony ;
		cust_colony_text = MemoryDict.getDictName(DictKey.CUST_COLONY, cust_colony);
	}
	public String getCounty_id_for() {
		return county_id_for;
	}


	public void setCounty_id_for(String countyIdFor) {
		county_id_for = countyIdFor;
		county_name_for = MemoryDict.getDictName(DictKey.COUNTY, county_id_for);
	}

	public String getCust_class() {
		return cust_class;
	}

	public void setCust_class(String cust_class) {
		this.cust_class = cust_class;
		cust_class_text = MemoryDict.getDictName(DictKey.CUST_CLASS, cust_class);
	}

	public Integer getUser_num() {
		return user_num;
	}

	public void setUser_num(Integer user_num) {
		this.user_num = user_num;
	}


	public String getCust_class_text() {
		return cust_class_text;
	}

}