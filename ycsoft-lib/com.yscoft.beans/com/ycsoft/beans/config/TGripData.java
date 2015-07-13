/**
 * TGripData.java	2010/11/25
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * TGripData -> T_GRIP_DATA mapping 
 */
@POJO(
	tn="T_GRIP_DATA",
	sn="",
	pk="")
public class TGripData implements Serializable {
	
	// TGripData all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 119711684975526864L;
	private String grip_key ;	
	private Date grip_date ;	
	
	/**
	 * default empty constructor
	 */
	public TGripData() {}
	
	
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

}