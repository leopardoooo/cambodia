package com.ycsoft.beans.prod;

/**
 * PProdCounty.java	2011/09/15
 */
 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PProdCounty -> P_PROD_COUNTY mapping 
 */
@POJO(
	tn="P_PROD_COUNTY",
	sn="",
	pk="")
public class PProdCounty implements Serializable {
	
	// PProdCounty all properties 

	private String prod_id ;	
	private String county_id ;	
	
	private String county_name;
	
	/**
	 * @return the county_name
	 */
	public String getCounty_name() {
		return county_name;
	}


	/**
	 * @param countyName the county_name to set
	 */
	public void setCounty_name(String countyName) {
		county_name = countyName;
	}


	/**
	 * default empty constructor
	 */
	public PProdCounty() {}
	
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		this.county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}

}