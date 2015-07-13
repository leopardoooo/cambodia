/**
 * RProjectAddr.java	2012/05/07
 */
 
package com.ycsoft.beans.project; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * RProjectAddr -> R_PROJECT_ADDR mapping 
 */
@POJO(
	tn="R_PROJECT_ADDR",
	sn="",
	pk="PROJECT_ID, ADDR_ID")
public class RProjectAddr implements Serializable {
	
	// RProjectAddr all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -7114844890204568478L;
	private String project_id ;	
	private String addr_id ;	
	
	private String addr_name;
	
	/**
	 * default empty constructor
	 */
	public RProjectAddr() {}
	
	
	// project_id getter and setter
	public String getProject_id(){
		return this.project_id ;
	}
	
	public void setProject_id(String project_id){
		this.project_id = project_id ;
	}
	
	// addr_id getter and setter
	public String getAddr_id(){
		return this.addr_id ;
	}
	
	public void setAddr_id(String addr_id){
		this.addr_id = addr_id ;
	}


	public String getAddr_name() {
		return addr_name;
	}


	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}

}