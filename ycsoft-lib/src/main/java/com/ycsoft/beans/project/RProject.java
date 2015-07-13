/**
 * RProject.java	2012/05/07
 */
 
package com.ycsoft.beans.project; 

import java.io.Serializable;
import java.util.Date;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * RProject -> R_PROJECT mapping 
 */
@POJO(
	tn="R_PROJECT",
	sn="SEQ_PROJECT_ID",
	pk="PROJECT_ID")
public class RProject implements Serializable {
	
	// RProject all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -1554070790922251141L;
	private String project_id;
	private String project_number ;	
	private String project_name ;	
	private String project_type ;	
	private Date pre_start_date ;	
	private Date start_date ;	
	private Date pre_end_date ;	
	private Date end_date ;	
	private Integer plan_num ;	
	private Integer real_num ;	
	private Integer plan_users_1 ;	
	private Integer plan_income_1 ;	
	private Integer plan_users_2 ;	
	private Integer plan_income_2 ;	
	private Integer plan_users_3 ;	
	private Integer plan_income_3 ;	
	private Integer plan_users_4 ;	
	private Integer plan_income_4 ;	
	private Integer plan_users_5 ;	
	private Integer plan_income_5 ;	
	private Integer optic_cable_length ;	
	private Integer electric_cable_length ;	
	private Integer optical_node_number ;	
	private Integer project_plan_money ;	
	private Integer project_final_money ;	
	private String status ;	
	private Date create_date ;
	private Date change_date ;	
	private String is_valid ;	
	private String county_id ;	
	private String remark ;	
	
	private String county_name;
	private String project_type_text;
	private String status_text;
	private String is_valid_text;
	
	private String addr_name;
	
	/**
	 * default empty constructor
	 */
	public RProject() {}
	
	
	// project_id getter and setter
	public String getProject_id(){
		return this.project_id ;
	}
	
	public void setProject_id(String project_id){
		this.project_id = project_id ;
	}
	
	// project_name getter and setter
	public String getProject_name(){
		return this.project_name ;
	}
	
	public void setProject_name(String project_name){
		this.project_name = project_name ;
	}
	
	// project_type getter and setter
	public String getProject_type(){
		return this.project_type ;
	}
	
	public void setProject_type(String project_type){
		this.project_type = project_type ;
		this.project_type_text = MemoryDict.getDictName(DictKey.PROJECT_TYPE, project_type);
	}
	
	// pre_start_date getter and setter
	public Date getPre_start_date(){
		return this.pre_start_date ;
	}
	
	public void setPre_start_date(Date pre_start_date){
		this.pre_start_date = pre_start_date ;
	}
	
	// start_date getter and setter
	public Date getStart_date(){
		return this.start_date ;
	}
	
	public void setStart_date(Date start_date){
		this.start_date = start_date ;
	}
	
	// pre_end_date getter and setter
	public Date getPre_end_date(){
		return this.pre_end_date ;
	}
	
	public void setPre_end_date(Date pre_end_date){
		this.pre_end_date = pre_end_date ;
	}
	
	// end_date getter and setter
	public Date getEnd_date(){
		return this.end_date ;
	}
	
	public void setEnd_date(Date end_date){
		this.end_date = end_date ;
	}
	
	// plan_num getter and setter
	public Integer getPlan_num(){
		return this.plan_num ;
	}
	
	public void setPlan_num(Integer plan_num){
		this.plan_num = plan_num ;
	}
	
	// real_num getter and setter
	public Integer getReal_num(){
		return this.real_num ;
	}
	
	public void setReal_num(Integer real_num){
		this.real_num = real_num ;
	}
	
	// plan_users_1 getter and setter
	public Integer getPlan_users_1(){
		return this.plan_users_1 ;
	}
	
	public void setPlan_users_1(Integer plan_users_1){
		this.plan_users_1 = plan_users_1 ;
	}
	
	// plan_income_1 getter and setter
	public Integer getPlan_income_1(){
		return this.plan_income_1 ;
	}
	
	public void setPlan_income_1(Integer plan_income_1){
		this.plan_income_1 = plan_income_1 ;
	}
	
	// plan_users_2 getter and setter
	public Integer getPlan_users_2(){
		return this.plan_users_2 ;
	}
	
	public void setPlan_users_2(Integer plan_users_2){
		this.plan_users_2 = plan_users_2 ;
	}
	
	// plan_income_2 getter and setter
	public Integer getPlan_income_2(){
		return this.plan_income_2 ;
	}
	
	public void setPlan_income_2(Integer plan_income_2){
		this.plan_income_2 = plan_income_2 ;
	}
	
	// plan_users_3 getter and setter
	public Integer getPlan_users_3(){
		return this.plan_users_3 ;
	}
	
	public void setPlan_users_3(Integer plan_users_3){
		this.plan_users_3 = plan_users_3 ;
	}
	
	// plan_income_3 getter and setter
	public Integer getPlan_income_3(){
		return this.plan_income_3 ;
	}
	
	public void setPlan_income_3(Integer plan_income_3){
		this.plan_income_3 = plan_income_3 ;
	}
	
	// optic_cable_length getter and setter
	public Integer getOptic_cable_length(){
		return this.optic_cable_length ;
	}
	
	public void setOptic_cable_length(Integer optic_cable_length){
		this.optic_cable_length = optic_cable_length ;
	}
	
	// electric_cable_length getter and setter
	public Integer getElectric_cable_length(){
		return this.electric_cable_length ;
	}
	
	public void setElectric_cable_length(Integer electric_cable_length){
		this.electric_cable_length = electric_cable_length ;
	}
	
	// optical_node_number getter and setter
	public Integer getOptical_node_number(){
		return this.optical_node_number ;
	}
	
	public void setOptical_node_number(Integer optical_node_number){
		this.optical_node_number = optical_node_number ;
	}
	
	// project_plan_money getter and setter
	public Integer getProject_plan_money(){
		return this.project_plan_money ;
	}
	
	public void setProject_plan_money(Integer project_plan_money){
		this.project_plan_money = project_plan_money ;
	}
	
	// project_final_money getter and setter
	public Integer getProject_final_money(){
		return this.project_final_money ;
	}
	
	public void setProject_final_money(Integer project_final_money){
		this.project_final_money = project_final_money ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
		this.status_text = MemoryDict.getDictName(DictKey.PROJECT_STATUS, status);
	}
	
	// create_date getter and setter
	public Date getCreate_date(){
		return this.create_date ;
	}
	
	public void setCreate_date(Date create_date){
		this.create_date = create_date ;
	}
	
	// is_valid getter and setter
	public String getIs_valid(){
		return this.is_valid ;
	}
	
	public void setIs_valid(String is_valid){
		this.is_valid = is_valid ;
		this.is_valid_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_valid);
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		this.county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}


	public String getProject_type_text() {
		return project_type_text;
	}


	public String getStatus_text() {
		return status_text;
	}


	public String getIs_valid_text() {
		return is_valid_text;
	}


	public String getCounty_name() {
		return county_name;
	}


	public Date getChange_date() {
		return change_date;
	}


	public void setChange_date(Date change_date) {
		this.change_date = change_date;
	}


	public Integer getPlan_users_4() {
		return plan_users_4;
	}


	public void setPlan_users_4(Integer plan_users_4) {
		this.plan_users_4 = plan_users_4;
	}


	public Integer getPlan_income_4() {
		return plan_income_4;
	}


	public void setPlan_income_4(Integer plan_income_4) {
		this.plan_income_4 = plan_income_4;
	}


	public Integer getPlan_users_5() {
		return plan_users_5;
	}


	public void setPlan_users_5(Integer plan_users_5) {
		this.plan_users_5 = plan_users_5;
	}


	public Integer getPlan_income_5() {
		return plan_income_5;
	}


	public void setPlan_income_5(Integer plan_income_5) {
		this.plan_income_5 = plan_income_5;
	}


	public String getAddr_name() {
		return addr_name;
	}


	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}


	public String getProject_number() {
		return project_number;
	}


	public void setProject_number(String project_number) {
		this.project_number = project_number;
	}

}