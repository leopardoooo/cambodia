/**
 * JBandCommand.java	2011/05/09
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * JBandCommand -> J_BAND_COMMAND mapping 
 */
@POJO(
	tn="J_BAND_COMMAND",
	sn="",
	pk="")
public class JBandCommand implements Serializable {
	
	// JBandCommand all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 8914092755631544635L;
	private Long transnum ;	
	private Integer job_id ;	
	private String supplyier_id ;	
	private String server_id ;	
	private String cmd_type ;	
	private Integer done_code ;	
	private String cust_id ;	
	private String user_id ;	
	private String prod_id ;	
	private String res_id ;	
	private String detail_param ;	
	private Date create_time ;	
	private String is_send ;	
	private Date send_time ;	
	private String is_success ;	
	private String error_info ;	
	private String county_id ;	
	private String area_id ;
	private Integer return_code;
	
	private String stb_id;
	private String card_id;
	private String modem_mac;
	private String cmd_type_text;
	private String is_success_text;

	/**
	 * default empty constructor
	 */
	public JBandCommand() {}
	
	
	// transnum getter and setter
	public String getIs_success_text() {
		return is_success_text;
	}
	
	public Long getTransnum(){
		return this.transnum ;
	}
	
	public void setTransnum(Long transnum){
		this.transnum = transnum ;
	}
	
	// job_id getter and setter
	public Integer getJob_id(){
		return this.job_id ;
	}
	
	public void setJob_id(Integer job_id){
		this.job_id = job_id ;
	}
	
	// supplyier_id getter and setter
	public String getSupplyier_id(){
		return this.supplyier_id ;
	}
	
	public void setSupplyier_id(String supplyier_id){
		this.supplyier_id = supplyier_id ;
	}
	
	// server_id getter and setter
	public String getServer_id(){
		return this.server_id ;
	}
	
	public void setServer_id(String server_id){
		this.server_id = server_id ;
	}
	
	// cmd_type getter and setter
	public String getCmd_type(){
		return this.cmd_type ;
	}
	
	public void setCmd_type(String cmd_type){
		this.cmd_type = cmd_type ;
		this.cmd_type_text = MemoryDict.getDictName(DictKey.CMD_TYPE, cmd_type);
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
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
	
	// detail_param getter and setter
	public String getDetail_param(){
		return this.detail_param ;
	}
	
	public void setDetail_param(String detail_param){
		this.detail_param = detail_param ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// is_send getter and setter
	public String getIs_send(){
		return this.is_send ;
	}
	
	public void setIs_send(String is_send){
		this.is_send = is_send ;
	}
	
	// send_time getter and setter
	public Date getSend_time(){
		return this.send_time ;
	}
	
	public void setSend_time(Date send_time){
		this.send_time = send_time ;
	}
	
	// is_success getter and setter
	public String getIs_success(){
		return this.is_success ;
	}
	
	public void setIs_success(String is_success){
		this.is_success = is_success ;
		this.is_success_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_success);
	}
	
	// error_info getter and setter
	public String getError_info(){
		return this.error_info ;
	}
	
	public void setError_info(String error_info){
		this.error_info = error_info ;
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


	public Integer getReturn_code() {
		return return_code;
	}


	public void setReturn_code(Integer return_code) {
		this.return_code = return_code;
	}


	public String getModem_mac() {
		return modem_mac;
	}


	public void setModem_mac(String modemMac) {
		modem_mac = modemMac;
	}


	public String getCmd_type_text() {
		return cmd_type_text;
	}


	public void setCmd_type_text(String cmdTypeText) {
		cmd_type_text = cmdTypeText;
	}


	public String getStb_id() {
		return stb_id;
	}


	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}


	public String getCard_id() {
		return card_id;
	}


	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

}