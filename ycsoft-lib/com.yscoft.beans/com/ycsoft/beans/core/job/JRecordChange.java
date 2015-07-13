/**
 * JRecordChange.java	2010/11/28
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * JRecordChange -> J_RECORD_CHANGE mapping 
 */
@POJO(
	tn="J_RECORD_CHANGE",
	sn="SEQ_JOB_CHANGE",
	pk="CHANGE_SN")
public class JRecordChange implements Serializable {
	
	// JRecordChange all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -7874437216216343615L;
	private Integer change_sn ;	
	private String table_name ;	
	private String rec_id ;	
	private String change_type ;	
	private Date change_date ;	
	private String prep_flag ;	
	private String usefee_flag ;	
	private String bill_flag ;	
	private String county_id ;	
	private String area_id ;
	
	private String p_flag1;
	private String p_flag2;
	private String p_flag3;
	private String p_flag4;
	private String p_flag5;
	
	/**
	 * default empty constructor
	 */
	public JRecordChange() {}
	
	
	// change_sn getter and setter
	public Integer getChange_sn(){
		return this.change_sn ;
	}
	
	public void setChange_sn(Integer change_sn){
		this.change_sn = change_sn ;
	}
	
	// table_name getter and setter
	public String getTable_name(){
		return this.table_name ;
	}
	
	public void setTable_name(String table_name){
		this.table_name = table_name ;
	}
	
	// rec_id getter and setter
	public String getRec_id(){
		return this.rec_id ;
	}
	
	public void setRec_id(String rec_id){
		this.rec_id = rec_id ;
	}
	
	// change_type getter and setter
	public String getChange_type(){
		return this.change_type ;
	}
	
	public void setChange_type(String change_type){
		this.change_type = change_type ;
	}
	
	// change_date getter and setter
	public Date getChange_date(){
		return this.change_date ;
	}
	
	public void setChange_date(Date change_date){
		this.change_date = change_date ;
	}
	
	// prep_flag getter and setter
	public String getPrep_flag(){
		return this.prep_flag ;
	}
	
	public void setPrep_flag(String prep_flag){
		this.prep_flag = prep_flag ;
	}
	
	// usefee_flag getter and setter
	public String getUsefee_flag(){
		return this.usefee_flag ;
	}
	
	public void setUsefee_flag(String usefee_flag){
		this.usefee_flag = usefee_flag ;
	}
	
	// bill_flag getter and setter
	public String getBill_flag(){
		return this.bill_flag ;
	}
	
	public void setBill_flag(String bill_flag){
		this.bill_flag = bill_flag ;
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


	public String getP_flag1() {
		return p_flag1;
	}


	public void setP_flag1(String p_flag1) {
		this.p_flag1 = p_flag1;
	}


	public String getP_flag2() {
		return p_flag2;
	}


	public void setP_flag2(String p_flag2) {
		this.p_flag2 = p_flag2;
	}


	public String getP_flag3() {
		return p_flag3;
	}


	public void setP_flag3(String p_flag3) {
		this.p_flag3 = p_flag3;
	}


	public String getP_flag4() {
		return p_flag4;
	}


	public void setP_flag4(String p_flag4) {
		this.p_flag4 = p_flag4;
	}


	public String getP_flag5() {
		return p_flag5;
	}


	public void setP_flag5(String p_flag5) {
		this.p_flag5 = p_flag5;
	}

}