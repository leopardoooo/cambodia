/**
 * JBusiCmdSendosdCfg.java	2012/08/07
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import java.util.Date ;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * JBusiCmdSendosdCfg -> J_BUSI_CMD_SENDOSD_CFG mapping 
 */
@POJO(
	tn="J_BUSI_CMD_SENDOSD_CFG",
	sn="SEQ_DONE_CODE",
	pk="DONE_CODE")
public class JBusiCmdSendosdCfg extends OptrBase {
	
	// JBusiCmdSendosdCfg all properties 

	private Integer done_code;
	private String cas_id ;	
	private String supplier_id ;	
	private String cmd_type ;	
	private Date send_start_date ;	
	private Date send_end_date ;	
	private String send_start_time ;	
	private String send_end_time ;	
	private Integer send_cycle ;	
	private Integer send_times ;	
	private Integer send_repeat_times ;	
	private String message ;	
	private Date done_date ;	
	
	private String server_name;
	private String supplier_name;
	
	/**
	 * default empty constructor
	 */
	public JBusiCmdSendosdCfg() {}
	
	
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
		this.supplier_name = MemoryDict.getDictName(DictKey.CA_SUPPLIER, supplier_id);
	}
	
	// cmd_type getter and setter
	public String getCmd_type(){
		return this.cmd_type ;
	}
	
	public void setCmd_type(String cmd_type){
		this.cmd_type = cmd_type ;
	}
	
	// send_start_date getter and setter
	public Date getSend_start_date(){
		return this.send_start_date ;
	}
	
	public void setSend_start_date(Date send_start_date){
		this.send_start_date = send_start_date ;
	}
	
	// send_end_date getter and setter
	public Date getSend_end_date(){
		return this.send_end_date ;
	}
	
	public void setSend_end_date(Date send_end_date){
		this.send_end_date = send_end_date ;
	}
	
	// send_start_time getter and setter
	public String getSend_start_time(){
		return this.send_start_time ;
	}
	
	public void setSend_start_time(String send_start_time){
		this.send_start_time = send_start_time ;
	}
	
	// send_end_time getter and setter
	public String getSend_end_time(){
		return this.send_end_time ;
	}
	
	public void setSend_end_time(String send_end_time){
		this.send_end_time = send_end_time ;
	}
	
	// send_cycle getter and setter
	public Integer getSend_cycle(){
		return this.send_cycle ;
	}
	
	public void setSend_cycle(Integer send_cycle){
		this.send_cycle = send_cycle ;
	}
	
	// send_times getter and setter
	public Integer getSend_times(){
		return this.send_times ;
	}
	
	public void setSend_times(Integer send_times){
		this.send_times = send_times ;
	}
	
	// send_repeat_times getter and setter
	public Integer getSend_repeat_times(){
		return this.send_repeat_times ;
	}
	
	public void setSend_repeat_times(Integer send_repeat_times){
		this.send_repeat_times = send_repeat_times ;
	}
	
	// message getter and setter
	public String getMessage(){
		return this.message ;
	}
	
	public void setMessage(String message){
		this.message = message ;
	}
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}


	public Integer getDone_code() {
		return done_code;
	}


	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}


	public String getServer_name() {
		return server_name;
	}


	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}


	public String getSupplier_name() {
		return supplier_name;
	}

}