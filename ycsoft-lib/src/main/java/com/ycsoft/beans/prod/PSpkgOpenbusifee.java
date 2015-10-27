/**
 * PSpkgOpenbusifee.java	2015/09/05
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PSpkgOpenbusifee -> P_SPKG_OPENBUSIFEE mapping 
 */
@POJO(
	tn="P_SPKG_OPENBUSIFEE",
	sn="",
	pk="ID")
public class PSpkgOpenbusifee implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5840675314263929282L;
	// PSpkgOpenbusifee all properties 
	private String id;
	private String sp_id ;	
	private String fee_id ;	
	private Integer fee ;	
	private String status ;	
	private Date status_date ;	
	private Date create_time ;	
	private String optr_id ;	
	private Integer use_done_code ;	
	
	private String status_text;
	private String fee_name;
	
	public String getStatus_text() {
		return status_text;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFee_name() {
		return fee_name;
	}


	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}


	/**
	 * default empty constructor
	 */
	public PSpkgOpenbusifee() {}
	
	
	// sp_id getter and setter
	public String getSp_id(){
		return this.sp_id ;
	}
	
	public void setSp_id(String sp_id){
		this.sp_id = sp_id ;
	}
	
	// fee_id getter and setter
	public String getFee_id(){
		return this.fee_id ;
	}
	
	public void setFee_id(String fee_id){
		this.fee_id = fee_id ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}
	
	// status_date getter and setter
	public Date getStatus_date(){
		return this.status_date ;
	}
	
	public void setStatus_date(Date status_date){
		this.status_date = status_date ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// use_done_code getter and setter
	public Integer getUse_done_code(){
		return this.use_done_code ;
	}
	
	public void setUse_done_code(Integer use_done_code){
		this.use_done_code = use_done_code ;
	}

}