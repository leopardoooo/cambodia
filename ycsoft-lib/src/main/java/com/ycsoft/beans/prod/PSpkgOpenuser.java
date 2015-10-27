/**
 * PSpkgOpenuser.java	2015/09/05
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PSpkgOpenuser -> P_SPKG_OPENUSER mapping 
 */
@POJO(
	tn="P_SPKG_OPENUSER",
	sn="",
	pk="ID")
public class PSpkgOpenuser implements Serializable {
	
	// PSpkgOpenuser all properties 
	private String id;
	private String sp_id ;	
	private String user_type ;	
	private String device_model ;	
	private String buy_type ;	
	private Integer open_num ;	
	private String fee_id ;	
	private Integer fee ;	
	private String status ;	
	private Date status_date ;	
	private Date create_time ;	
	private String optr_id ;	
	private Integer use_done_code ;	
	
	private String status_text;
	private String device_model_text;
	private String buy_mode_name;
	private String fee_name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getStatus_text() {
		return status_text;
	}
	
	public String getBuy_mode_name() {
		return buy_mode_name;
	}


	public void setBuy_mode_name(String buy_mode_name) {
		this.buy_mode_name = buy_mode_name;
	}


	public String getFee_name() {
		return fee_name;
	}


	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}


	public String getDevice_model_text() {
		return device_model_text;
	}


	/**
	 * default empty constructor
	 */
	public PSpkgOpenuser() {}
	
	
	// sp_id getter and setter
	public String getSp_id(){
		return this.sp_id ;
	}
	
	public void setSp_id(String sp_id){
		this.sp_id = sp_id ;
	}
	
	// user_type getter and setter
	public String getUser_type(){
		return this.user_type ;
	}
	
	public void setUser_type(String user_type){
		this.user_type = user_type ;
	}
	
	// device_model getter and setter
	public String getDevice_model(){
		return this.device_model ;
	}
	
	public void setDevice_model(String device_model){
		this.device_model = device_model ;
		/*String deviceType = "";
		if(getUser_type().equals("BAND")){
			deviceType = "MODEM";
		} else {
			deviceType = "STB";
		}
		this.device_model_text = MemoryDict.getDictName(deviceType+"_MODEL", device_model);*/
		this.device_model_text = MemoryDict.getDictName(DictKey.DEVICE_MODEL, device_model);
	}
	
	// buy_type getter and setter
	public String getBuy_type(){
		return this.buy_type ;
	}
	
	public void setBuy_type(String buy_type){
		this.buy_type = buy_type ;
	}
	
	// open_num getter and setter
	public Integer getOpen_num(){
		return this.open_num ;
	}
	
	public void setOpen_num(Integer open_num){
		this.open_num = open_num ;
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