/**
 * PProdTariffCounty.java	2012/08/09
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * PProdTariffDisctCounty -> P_PROD_TARIFF_DISCT_COUNTY mapping
 */
@POJO(
	tn="P_PROD_TARIFF_DISCT_COUNTY",
	sn="",
	pk="")
public class PProdTariffDisctCounty implements Serializable {

	// PProdTariffDisctCounty all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -3208882100330157391L;
	private String disct_id ;
	private String county_id ;

	/**
	 * default empty constructor
	 */
	public PProdTariffDisctCounty() {}
	public PProdTariffDisctCounty(String disctId,String countyId) {
		disct_id = disctId;
		county_id = countyId;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}
	public String getDisct_id() {
		return disct_id;
	}
	public void setDisct_id(String disct_id) {
		this.disct_id = disct_id;
	}

}