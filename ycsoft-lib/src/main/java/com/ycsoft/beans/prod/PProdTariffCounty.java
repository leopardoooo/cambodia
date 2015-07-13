/**
 * PProdTariffCounty.java	2010/10/21
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * PProdTariffCounty -> P_PROD_TARIFF_COUNTY mapping
 */
@POJO(
	tn="P_PROD_TARIFF_COUNTY",
	sn="",
	pk="")
public class PProdTariffCounty implements Serializable {

	// PProdTariffCounty all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7706169964004074085L;
	private String tariff_id ;
	private String county_id ;

	/**
	 * default empty constructor
	 */
	public PProdTariffCounty() {}
	public PProdTariffCounty(String tariffId,String countyId) {
		tariff_id = tariffId;
		county_id = countyId;
	}

	// tariff_id getter and setter
	public String getTariff_id(){
		return tariff_id ;
	}

	public void setTariff_id(String tariff_id){
		this.tariff_id = tariff_id ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}