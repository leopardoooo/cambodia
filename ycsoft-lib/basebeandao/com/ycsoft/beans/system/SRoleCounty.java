package com.ycsoft.beans.system;

/**
 * SRoleCounty.java	2011/11/24
 */
 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * SRoleCounty -> S_ROLE_COUNTY mapping 
 */
@POJO(
	tn="S_ROLE_COUNTY",
	sn="",
	pk="")
public class SRoleCounty implements Serializable {
	
	// SRoleCounty all properties 

	private String role_id ;	
	private String county_id ;	
	
	/**
	 * default empty constructor
	 */
	public SRoleCounty() {}
	
	
	// role_id getter and setter
	public String getRole_id(){
		return this.role_id ;
	}
	
	public void setRole_id(String role_id){
		this.role_id = role_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}