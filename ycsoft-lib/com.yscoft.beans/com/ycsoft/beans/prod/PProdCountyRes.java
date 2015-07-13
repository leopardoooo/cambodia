/**
 * PProdCountyRes.java	2010/09/21
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * PProdCountyRes -> P_PROD_COUNTY_RES mapping
 */
@POJO(
	tn="P_PROD_COUNTY_RES",
	sn="",
	pk="")
public class PProdCountyRes implements Serializable {

	// PProdCountyRes all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 632739885967856781L;
	private String prod_id ;
	private String county_id ;
	private String res_id ;

	private String county_name;
	/**
	 * default empty constructor
	 */
	public PProdCountyRes() {}
	public PProdCountyRes(String prodId, String countyId, String resId) {
		prod_id = prodId;
		county_id = countyId;
		res_id = resId;
	}

	// prod_id getter and setter
	public String getProd_id(){
		return prod_id ;
	}

	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}

	// res_id getter and setter
	public String getRes_id(){
		return res_id ;
	}

	public void setRes_id(String res_id){
		this.res_id = res_id ;
	}


	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

}