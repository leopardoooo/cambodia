/**
 * RProjectCounty.java	2012/05/07
 */
 
package com.ycsoft.beans.project; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * RProjectCounty -> R_PROJECT_COUNTY mapping 
 */
@POJO(
	tn="R_PROJECT_COUNTY",
	sn="",
	pk="")
public class RProjectCounty implements Serializable {
	
	// RProjectCounty all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 2861857209574387629L;
	private String project_county_id ;	
	private String county_id ;	
	
	private String county_name;
	
	/**
	 * default empty constructor
	 */
	public RProjectCounty() {}
	
	
	// project_county_id getter and setter
	public String getProject_county_id(){
		return this.project_county_id ;
	}
	
	public void setProject_county_id(String project_county_id){
		this.project_county_id = project_county_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}


	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

}