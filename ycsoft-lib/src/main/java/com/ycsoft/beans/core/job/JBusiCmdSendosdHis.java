/**
 * JBusiCmdSendosdHis.java	2012/03/01
 */
 
package com.ycsoft.beans.core.job; 

import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;


/**
 * JBusiCmdSendosdHis -> J_BUSI_CMD_SENDOSD_HIS mapping 
 */
@POJO(
	tn="J_BUSI_CMD_SENDOSD_HIS",
	sn="",
	pk="")
public class JBusiCmdSendosdHis extends BusiBase {
	
	// JBusiCmdSendosdHis all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -3292632322874054217L;
	private Integer job_id ;
	private String cas_id ;	
	private String supplier_id ;	
	private Date send_time ;	
	private String message ;
	private Date done_date ;	
	private String ca_type;

	private String server_name;
	private String num;
	private Date next_time;
	
	/**
	 * default empty constructor
	 */
	public JBusiCmdSendosdHis() {}
	
	
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
	
	// supplier_id getter and setter
	public String getSupplier_id(){
		return this.supplier_id ;
	}
	
	public void setSupplier_id(String supplier_id){
		this.supplier_id = supplier_id ;
	}
	
	// send_time getter and setter
	public Date getSend_time(){
		return this.send_time ;
	}
	
	public void setSend_time(Date send_time){
		this.send_time = send_time ;
	}
	
	// message getter and setter
	public String getMessage(){
		return this.message ;
	}
	
	public void setMessage(String message){
		this.message = message ;
	}


	public Date getDone_date() {
		return done_date;
	}


	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}


	public String getCa_type() {
		return ca_type;
	}


	public void setCa_type(String ca_type) {
		this.ca_type = ca_type;
	}


	public String getServer_name() {
		return server_name;
	}


	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}


	public String getNum() {
		return num;
	}


	public void setNum(String num) {
		this.num = num;
	}


	public Date getNext_time() {
		return next_time;
	}


	public void setNext_time(Date next_time) {
		this.next_time = next_time;
	}

}