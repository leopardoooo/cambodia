/**
 * TRuleDefineCounty.java	2011/07/14
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * TRuleDefineCounty -> T_RULE_DEFINE_COUNTY mapping 
 */
@POJO(
	tn="T_RULE_DEFINE_COUNTY",
	sn="",
	pk="")
public class TRuleDefineCounty implements Serializable {
	
	// TRuleDefineCounty all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 4408778837672686463L;
	private String rule_id ;	
	private String county_id ;	
	
	/**
	 * default empty constructor
	 */
	public TRuleDefineCounty() {}
	
	
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

}