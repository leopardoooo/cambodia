/**
 * TGripLog.java	2010/11/25
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * TGripLog -> T_GRIP_LOG mapping 
 */
@POJO(
	tn="T_GRIP_LOG",
	sn="",
	pk="")
public class TGripLog implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8430677036979198173L;

	private String grip_key ;
	private String optr_id;
	private Date grip_date;
	private Date create_time;
	
	/**
	 * default empty constructor
	 */
	public TGripLog() {}
	
	
	// grip_key getter and setter
	public String getGrip_key(){
		return this.grip_key ;
	}
	
	public void setGrip_key(String grip_key){
		this.grip_key = grip_key ;
	}
	
	// grip_date getter and setter
	public Date getGrip_date(){
		return this.grip_date ;
	}
	
	public void setGrip_date(Date grip_date){
		this.grip_date = grip_date ;
	}
	public String getOptr_id() {
		return optr_id;
	}


	public void setOptr_id(String optrId) {
		optr_id = optrId;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}

}