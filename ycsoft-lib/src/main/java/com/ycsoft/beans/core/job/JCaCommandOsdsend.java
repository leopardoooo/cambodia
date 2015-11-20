/**
 * JCaCommandOsdsend.java	2015/11/18
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import java.util.Date ;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO ;


/**
 * JCaCommandOsdsend -> J_CA_COMMAND_OSDSEND mapping 
 */
@POJO(
	tn="J_CA_COMMAND_OSDSEND",
	sn="SEQ_CA_TRANSNUM",
	pk="transnum")
public class JCaCommandOsdsend extends BusiBase implements Serializable {
	
	// JCaCommandOsdsend all properties 

	private Long transnum ;	
	private Integer job_id ;	
	private String cas_id ;	
	private String cas_type ;	
	private String user_id ;	
	private String cust_id ;	
	private Integer done_code ;	
	private String cmd_type ;	
	private String stb_id ;	
	private String card_id ;	
	private String prg_name ;	
	private String boss_res_id ;	
	private String control_id ;	
	private String auth_begin_date ;	
	private String auth_end_date ;	
	private String result_flag ;	
	private String error_info ;	
	private String area_id ;	
	private String is_sent ;	
	private Date record_date ;	
	private Date send_date ;	
	private String detail_params ;	
	private Integer priority ;	
	private Date ret_date ;	
	
	/**
	 * default empty constructor
	 */
	public JCaCommandOsdsend() {}

	
	public Long getTransnum() {
		return transnum;
	}

	public void setTransnum(Long transnum) {
		this.transnum = transnum;
	}

	// job_id getter and setter
	public Integer getJob_id(){
		return this.job_id ;
	}
	
	public void setJob_id(Integer job_id){
		this.job_id = job_id ;
	}
	
	// cas_id getter and setter
	public String getCas_id(){
		return this.cas_id ;
	}
	
	public void setCas_id(String cas_id){
		this.cas_id = cas_id ;
	}
	
	// cas_type getter and setter
	public String getCas_type(){
		return this.cas_type ;
	}
	
	public void setCas_type(String cas_type){
		this.cas_type = cas_type ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// cmd_type getter and setter
	public String getCmd_type(){
		return this.cmd_type ;
	}
	
	public void setCmd_type(String cmd_type){
		this.cmd_type = cmd_type ;
	}
	
	// stb_id getter and setter
	public String getStb_id(){
		return this.stb_id ;
	}
	
	public void setStb_id(String stb_id){
		this.stb_id = stb_id ;
	}
	
	// card_id getter and setter
	public String getCard_id(){
		return this.card_id ;
	}
	
	public void setCard_id(String card_id){
		this.card_id = card_id ;
	}
	
	// prg_name getter and setter
	public String getPrg_name(){
		return this.prg_name ;
	}
	
	public void setPrg_name(String prg_name){
		this.prg_name = prg_name ;
	}
	
	// boss_res_id getter and setter
	public String getBoss_res_id(){
		return this.boss_res_id ;
	}
	
	public void setBoss_res_id(String boss_res_id){
		this.boss_res_id = boss_res_id ;
	}
	
	// control_id getter and setter
	public String getControl_id(){
		return this.control_id ;
	}
	
	public void setControl_id(String control_id){
		this.control_id = control_id ;
	}
	
	// auth_begin_date getter and setter
	public String getAuth_begin_date(){
		return this.auth_begin_date ;
	}
	
	public void setAuth_begin_date(String auth_begin_date){
		this.auth_begin_date = auth_begin_date ;
	}
	
	// auth_end_date getter and setter
	public String getAuth_end_date(){
		return this.auth_end_date ;
	}
	
	public void setAuth_end_date(String auth_end_date){
		this.auth_end_date = auth_end_date ;
	}
	
	// result_flag getter and setter
	public String getResult_flag(){
		return this.result_flag ;
	}
	
	public void setResult_flag(String result_flag){
		this.result_flag = result_flag ;
	}
	
	// error_info getter and setter
	public String getError_info(){
		return this.error_info ;
	}
	
	public void setError_info(String error_info){
		this.error_info = error_info ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}
	
	// is_sent getter and setter
	public String getIs_sent(){
		return this.is_sent ;
	}
	
	public void setIs_sent(String is_sent){
		this.is_sent = is_sent ;
	}
	
	// record_date getter and setter
	public Date getRecord_date(){
		return this.record_date ;
	}
	
	public void setRecord_date(Date record_date){
		this.record_date = record_date ;
	}
	
	// send_date getter and setter
	public Date getSend_date(){
		return this.send_date ;
	}
	
	public void setSend_date(Date send_date){
		this.send_date = send_date ;
	}
	
	// detail_params getter and setter
	public String getDetail_params(){
		return this.detail_params ;
	}
	
	public void setDetail_params(String detail_params){
		this.detail_params = detail_params ;
	}
	
	// priority getter and setter
	public Integer getPriority(){
		return this.priority ;
	}
	
	public void setPriority(Integer priority){
		this.priority = priority ;
	}
	
	// ret_date getter and setter
	public Date getRet_date(){
		return this.ret_date ;
	}
	
	public void setRet_date(Date ret_date){
		this.ret_date = ret_date ;
	}

}