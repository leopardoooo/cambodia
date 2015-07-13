package com.ycsoft.beans.prod;

/**
 * PPromFeeCounty.java	2012/07/13
 */
 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PPromFeeCounty -> P_PROM_FEE_COUNTY mapping 
 */
@POJO(
	tn="P_PROM_FEE_COUNTY",
	sn="",
	pk="")
public class PPromFeeCounty implements Serializable {
	
	// PPromFeeCounty all properties 

	private String prom_fee_id ;	
	private String county_id ;
	private String county_name;
	
	/**
	 * default empty constructor
	 */
	public PPromFeeCounty() {}
	
	
	// prom_fee_id getter and setter
	public String getProm_fee_id(){
		return this.prom_fee_id ;
	}
	
	public void setProm_fee_id(String prom_fee_id){
		this.prom_fee_id = prom_fee_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		setCounty_name(MemoryDict.getDictName(DictKey.COUNTY, this.county_id));
	}


	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String countyName) {
		county_name = countyName;
	}
}