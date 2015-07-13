/**
 * JSendMsg.java	2010/11/21
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * JSendMsg -> J_SEND_MSG mapping 
 */
@POJO(
	tn="J_SEND_MSG",
	sn="",
	pk="")
public class JSendMsg implements Serializable {
	
	// JSendMsg all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -5013153236804217226L;
	private Integer job_id ;	
	private Integer done_code ;
	private String task_code ;
	private String msg_type ;	
	private Date create_time ;	
	private Date exec_time ;	
	private Date done_time ;	
	private Integer limit_user_cnt ;	
	private Integer user_count ;	
	private Integer send_user_count ;	
	private String status ;	
	private String subdistrict ;	
	private String district ;	
	private String address ;	
	private String terminal_type ;	
	private String unit ;	
	private String hasten_stop_flag ;	
	private String cust_type ;	
	private String cust_class ;	
	private String cust_colony ;	
	private String sql ;	
	private String message ;	
	private String prod_id ;	
	private Date prod_invalid_bdate ;	
	private Date prod_invalid_edate ;	
	private String send_type ;	
	private String optr_id ;	
	private String dept_id ;	
	private String aud_optr ;	
	private String aud_dept ;	
	private String aud_status ;	
	private String area_id ;	
	private String county_id ;
	private String mail_title;
	
	private String status_text;
	private String msg_type_text;
	private String task_code_text;
	
	/**
	 * default empty constructor
	 */
	public JSendMsg() {}
	
	
	// job_id getter and setter
	public Integer getJob_id(){
		return this.job_id ;
	}
	
	public void setJob_id(Integer job_id){
		this.job_id = job_id ;
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// msg_type getter and setter
	public String getMsg_type(){
		return this.msg_type ;
	}
	
	public void setMsg_type(String msg_type){
		this.msg_type = msg_type ;
		msg_type_text = MemoryDict.getDictName(DictKey.JOB_MESSAGE_TYPE, msg_type);
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// exec_time getter and setter
	public Date getExec_time(){
		return this.exec_time ;
	}
	
	public void setExec_time(Date exec_time){
		this.exec_time = exec_time ;
	}
	
	// done_time getter and setter
	public Date getDone_time(){
		return this.done_time ;
	}
	
	public void setDone_time(Date done_time){
		this.done_time = done_time ;
	}
	
	// limit_user_cnt getter and setter
	public Integer getLimit_user_cnt(){
		return this.limit_user_cnt ;
	}
	
	public void setLimit_user_cnt(Integer limit_user_cnt){
		this.limit_user_cnt = limit_user_cnt ;
	}
	
	// user_count getter and setter
	public Integer getUser_count(){
		return this.user_count ;
	}
	
	public void setUser_count(Integer user_count){
		this.user_count = user_count ;
	}
	
	// send_user_count getter and setter
	public Integer getSend_user_count(){
		return this.send_user_count ;
	}
	
	public void setSend_user_count(Integer send_user_count){
		this.send_user_count = send_user_count ;
	}
	
	// subdistrict getter and setter
	public String getSubdistrict(){
		return this.subdistrict ;
	}
	
	public void setSubdistrict(String subdistrict){
		this.subdistrict = subdistrict ;
	}
	
	// district getter and setter
	public String getDistrict(){
		return this.district ;
	}
	
	public void setDistrict(String district){
		this.district = district ;
	}
	
	// address getter and setter
	public String getAddress(){
		return this.address ;
	}
	
	public void setAddress(String address){
		this.address = address ;
	}
	
	// terminal_type getter and setter
	public String getTerminal_type(){
		return this.terminal_type ;
	}
	
	public void setTerminal_type(String terminal_type){
		this.terminal_type = terminal_type ;
	}
	
	// unit getter and setter
	public String getUnit(){
		return this.unit ;
	}
	
	public void setUnit(String unit){
		this.unit = unit ;
	}
	
	// hasten_stop_flag getter and setter
	public String getHasten_stop_flag(){
		return this.hasten_stop_flag ;
	}
	
	public void setHasten_stop_flag(String hasten_stop_flag){
		this.hasten_stop_flag = hasten_stop_flag ;
	}
	
	// cust_type getter and setter
	public String getCust_type(){
		return this.cust_type ;
	}
	
	public void setCust_type(String cust_type){
		this.cust_type = cust_type ;
	}
	
	// cust_class getter and setter
	public String getCust_class(){
		return this.cust_class ;
	}
	
	public void setCust_class(String cust_class){
		this.cust_class = cust_class ;
	}
	
	// cust_colony getter and setter
	public String getCust_colony(){
		return this.cust_colony ;
	}
	
	public void setCust_colony(String cust_colony){
		this.cust_colony = cust_colony ;
	}
	
	// sql getter and setter
	public String getSql(){
		return this.sql ;
	}
	
	public void setSql(String sql){
		this.sql = sql ;
	}
	
	// message getter and setter
	public String getMessage(){
		return this.message ;
	}
	
	public void setMessage(String message){
		this.message = message ;
	}
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// prod_invalid_bdate getter and setter
	public Date getProd_invalid_bdate(){
		return this.prod_invalid_bdate ;
	}
	
	public void setProd_invalid_bdate(Date prod_invalid_bdate){
		this.prod_invalid_bdate = prod_invalid_bdate ;
	}
	
	// prod_invalid_edate getter and setter
	public Date getProd_invalid_edate(){
		return this.prod_invalid_edate ;
	}
	
	public void setProd_invalid_edate(Date prod_invalid_edate){
		this.prod_invalid_edate = prod_invalid_edate ;
	}
	
	// send_type getter and setter
	public String getSend_type(){
		return this.send_type ;
	}
	
	public void setSend_type(String send_type){
		this.send_type = send_type ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// dept_id getter and setter
	public String getDept_id(){
		return this.dept_id ;
	}
	
	public void setDept_id(String dept_id){
		this.dept_id = dept_id ;
	}
	
	// aud_optr getter and setter
	public String getAud_optr(){
		return this.aud_optr ;
	}
	
	public void setAud_optr(String aud_optr){
		this.aud_optr = aud_optr ;
	}
	
	// aud_dept getter and setter
	public String getAud_dept(){
		return this.aud_dept ;
	}
	
	public void setAud_dept(String aud_dept){
		this.aud_dept = aud_dept ;
	}
	
	// aud_status getter and setter
	public String getAud_status(){
		return this.aud_status ;
	}
	
	public void setAud_status(String aud_status){
		this.aud_status = aud_status ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}


	public String getStatus_text() {
		return status_text;
	}


	public void setStatus_text(String statusText) {
		status_text = statusText;
	}


	public String getMsg_type_text() {
		return msg_type_text;
	}


	public void setMsg_type_text(String msgTypeText) {
		msg_type_text = msgTypeText;
	}


	public String getTask_code() {
		return task_code;
	}


	public void setTask_code(String taskCode) {
		task_code = taskCode;
		task_code_text = MemoryDict.getDictName(DictKey.JOB_TASK_CODE, task_code);
	}


	public String getTask_code_text() {
		return task_code_text;
	}


	public void setTask_code_text(String taskCodeText) {
		task_code_text = taskCodeText;
	}


	public String getMail_title() {
		return mail_title;
	}


	public void setMail_title(String mailTitle) {
		mail_title = mailTitle;
	}

}