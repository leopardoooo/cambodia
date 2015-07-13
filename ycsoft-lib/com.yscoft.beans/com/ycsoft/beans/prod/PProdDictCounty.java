/**
 * PProdDictCounty.java	2012/03/06
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * PProdDictCounty -> P_PROD_DICT_COUNTY mapping 
 */
@POJO(
	tn="P_PROD_DICT_COUNTY",
	sn="",
	pk="")
public class PProdDictCounty implements Serializable {
	
	// PProdDictCounty all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -2358747124987487962L;
	private String node_id ;	
	private String county_id ;	
	
	/**
	 * default empty constructor
	 */
	public PProdDictCounty() {}
	
	
	// node_id getter and setter
	public String getNode_id(){
		return this.node_id ;
	}
	
	public void setNode_id(String node_id){
		this.node_id = node_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}