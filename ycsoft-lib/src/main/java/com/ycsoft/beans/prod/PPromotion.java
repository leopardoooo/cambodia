package com.ycsoft.beans.prod;

/**
 * PPromotion.java	2010/10/09
 */


import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * PPromotion -> P_PROMOTION mapping
 */
@POJO(
	tn="P_PROMOTION",
	sn="",
	pk="promotion_id")
public class PPromotion implements Serializable {

	// PPromotion all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2921991635623784340L;
	private String promotion_id ;
	private String promotion_name ;
	private String theme_id ;
	private String protocol_id ;
	private String rule_id ;
	private String auto_exec ;
	private String for_area_id ;
	private Date eff_date ;
	private Date exp_date ;
	private Integer total_acct_fee ;
	private String area_id ;
	private String county_id ;
	private String optr_id ;
	private Date create_time ;
	private Integer priority;
	private Integer repetition_times;
	private Integer total_acct_count ;
	private Integer days;
	private Integer times;



	private String auto_exec_text;
	private String for_area_id_text;

	/**
	 * default empty constructor
	 */
	public PPromotion() {}


	// promotion_id getter and setter
	public String getPromotion_id(){
		return promotion_id ;
	}

	public void setPromotion_id(String promotion_id){
		this.promotion_id = promotion_id ;
	}

	// promotion_name getter and setter
	public String getPromotion_name(){
		return promotion_name ;
	}

	public void setPromotion_name(String promotion_name){
		this.promotion_name = promotion_name ;
	}

	// theme_id getter and setter
	public String getTheme_id(){
		return theme_id ;
	}

	public void setTheme_id(String theme_id){
		this.theme_id = theme_id ;
	}

	// protocol_id getter and setter
	public String getProtocol_id(){
		return protocol_id ;
	}

	public void setProtocol_id(String protocol_id){
		this.protocol_id = protocol_id ;
	}

	// rule_id getter and setter
	public String getRule_id(){
		return rule_id ;
	}

	public void setRule_id(String rule_id){
		this.rule_id = rule_id ;
	}

	// auto_exec getter and setter
	public String getAuto_exec(){
		return auto_exec ;
	}

	public void setAuto_exec(String auto_exec){
		this.auto_exec = auto_exec ;
		auto_exec_text = MemoryDict.getDictName("BOOLEAN", this.auto_exec);
	}

	// for_area_id getter and setter
	public String getFor_area_id(){
		return for_area_id ;
	}

	public void setFor_area_id(String for_area_id){
		this.for_area_id = for_area_id ;
		for_area_id_text = MemoryDict.getDictName("AREA", for_area_id);
	}

	// eff_date getter and setter
	public Date getEff_date(){
		return eff_date ;
	}

	public void setEff_date(Date eff_date){
		this.eff_date = eff_date ;
	}

	// exp_date getter and setter
	public Date getExp_date(){
		return exp_date ;
	}

	public void setExp_date(Date exp_date){
		this.exp_date = exp_date ;
	}

	// total_acct_fee getter and setter
	public Integer getTotal_acct_fee(){
		return total_acct_fee ;
	}

	public void setTotal_acct_fee(Integer total_acct_fee){
		this.total_acct_fee = total_acct_fee ;
	}

	// area_id getter and setter
	public String getArea_id(){
		return area_id ;
	}

	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

	// optr_id getter and setter
	public String getOptr_id(){
		return optr_id ;
	}

	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}





	public String getAuto_exec_text() {
		return auto_exec_text;
	}


	public void setAuto_exec_text(String auto_exec_text) {
		this.auto_exec_text = auto_exec_text;
	}


	public String getFor_area_id_text() {
		return for_area_id_text;
	}


	public void setFor_area_id_text(String for_area_id_text) {
		this.for_area_id_text = for_area_id_text;
	}


	public Integer getPriority() {
		return priority;
	}


	public void setPriority(Integer priority) {
		this.priority = priority;
	}


	public Integer getRepetition_times() {
		return repetition_times;
	}


	public void setRepetition_times(Integer repetition_times) {
		this.repetition_times = repetition_times;
	}


	public Integer getTotal_acct_count() {
		return total_acct_count;
	}


	public void setTotal_acct_count(Integer total_acct_count) {
		this.total_acct_count = total_acct_count;
	}


	public Integer getDays() {
		return days;
	}


	public void setDays(Integer days) {
		this.days = days;
	}


	public Integer getTimes() {
		return times;
	}


	public void setTimes(Integer times) {
		this.times = times;
	}



}