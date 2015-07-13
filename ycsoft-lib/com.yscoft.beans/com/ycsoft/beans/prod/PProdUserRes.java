/**
 * PProdUserRes.java	2010/11/22
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * PProdUserRes -> P_PROD_USER_RES mapping 
 */
@POJO(
	tn="P_PROD_USER_RES",
	sn="",
	pk="")
public class PProdUserRes implements Serializable {
	
	// PProdUserRes all properties 

	private String prod_id ;	
	private String res_id ;	
	private String rule_id ;	
	private String county_id ;	
	private String status;
	
	private String rule_id_text;
	
	/**
	 * default empty constructor
	 */
	public PProdUserRes() {}
	
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// res_id getter and setter
	public String getRes_id(){
		return this.res_id ;
	}
	
	public void setRes_id(String res_id){
		this.res_id = res_id ;
	}
	
	// rule_id getter and setter
	public String getRule_id(){
		return this.rule_id ;
	}
	
	public void setRule_id(String rule_id){
		this.rule_id = rule_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}


	public String getRule_id_text() {
		return rule_id_text;
	}


	public void setRule_id_text(String rule_id_text) {
		this.rule_id_text = rule_id_text;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	

}