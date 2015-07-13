package com.ycsoft.beans.prod;

/**
 * PPromFeeUser.java	2012/06/28
 */
 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * PPromFeeUser -> P_PROM_FEE_USER mapping 
 */
@POJO(
	tn="P_PROM_FEE_USER",
	sn="",
	pk="")
public class PPromFeeUser implements Serializable {
	
	// PPromFeeUser all properties 

	private String prom_fee_id ;	
	private String user_no ;	
	private String rule_id ;	
	private Integer prod_count ;
	private Integer user_fee ;	
	
	private String rule_id_text;
	private String rule_name;
	private String allow_user;
	
	/**
	 * default empty constructor
	 */
	public PPromFeeUser() {}
	
	
	public String getAllow_user() {
		return allow_user;
	}


	public void setAllow_user(String allowUser) {
		allow_user = allowUser;
	}


	public Integer getUser_fee() {
		return user_fee;
	}


	public void setUser_fee(Integer userFee) {
		user_fee = userFee;
	}


	// prom_fee_id getter and setter
	public String getProm_fee_id(){
		return this.prom_fee_id ;
	}
	
	public void setProm_fee_id(String prom_fee_id){
		this.prom_fee_id = prom_fee_id ;
	}
	
	// user_no getter and setter
	public String getUser_no(){
		return this.user_no ;
	}
	
	public void setUser_no(String user_no){
		this.user_no = user_no ;
	}
	
	// rule_id getter and setter
	public String getRule_id(){
		return this.rule_id ;
	}
	
	public void setRule_id(String rule_id){
		this.rule_id = rule_id ;
	}
	
	// prod_count getter and setter
	public Integer getProd_count(){
		return this.prod_count ;
	}
	
	public void setProd_count(Integer prod_count){
		this.prod_count = prod_count ;
	}


	public String getRule_id_text() {
		return rule_id_text;
	}


	public void setRule_id_text(String ruleIdText) {
		rule_id_text = ruleIdText;
	}


	public String getRule_name() {
		return rule_name;
	}


	public void setRule_name(String ruleName) {
		rule_name = ruleName;
	}
	
	

}