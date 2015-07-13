package com.ycsoft.beans.core.job;

/**
 * JDataSync.java	2011/02/23
 */
 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * JDataSync -> J_DATA_SYNC mapping 
 */
@POJO(
	tn="J_DATA_SYNC",
	sn="",
	pk="")
public class JDataSync implements Serializable {
	
	// JDataSync all properties 

	private String sync_cmd_id ;	
	private Integer job_id ;	
	private String supplyier_id ;	
	private String server_id ;
	private String cmd_type;
	private String table_name ;	
	private String detail_params ;	
	private Date create_time ;	
	private String is_send ;	
	private Date send_time ;	
	private String is_success ;	
	private String error_info ;	
	
	/**
	 * default empty constructor
	 */
	public JDataSync() {}
	
	
	// sync_cmd_id getter and setter
	public String getSync_cmd_id(){
		return this.sync_cmd_id ;
	}
	
	public void setSync_cmd_id(String sync_cmd_id){
		this.sync_cmd_id = sync_cmd_id ;
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
	
	// table_name getter and setter
	public String getTable_name(){
		return this.table_name ;
	}
	
	public void setTable_name(String table_name){
		this.table_name = table_name ;
	}
	
	// detail_params getter and setter
	public String getDetail_params(){
		return this.detail_params ;
	}
	
	public void setDetail_params(String detail_params){
		this.detail_params = detail_params ;
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
	}
	
	// error_info getter and setter
	public String getError_info(){
		return this.error_info ;
	}
	
	public void setError_info(String error_info){
		this.error_info = error_info ;
	}


	public String getCmd_type() {
		return cmd_type;
	}


	public void setCmd_type(String cmd_type) {
		this.cmd_type = cmd_type;
	}

}