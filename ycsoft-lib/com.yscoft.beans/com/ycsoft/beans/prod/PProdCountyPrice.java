/**
 * PProdCountyPrice.java	2012/10/24
 */
 
package com.ycsoft.beans.prod; 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PProdCountyPrice -> P_PROD_COUNTY_PRICE mapping 
 */
@POJO(
	tn="P_PROD_COUNTY_PRICE",
	sn="",
	pk="")
public class PProdCountyPrice implements Serializable {
	
	// PProdCountyPrice all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -3779246510702355885L;
	private String prod_id ;	
	private String county_id ;	
	private String price ;	
	private String area_price ;	
	private String county_price ;	
	
	private String county_name;
	private String prod_name;
	
	/**
	 * default empty constructor
	 */
	public PProdCountyPrice() {}
	
	public PProdCountyPrice(String prodId, String countyId, String priceId,String areaPrice,String countyPrice) {
		prod_id = prodId;
		county_id = countyId;
		price = priceId;
		area_price = areaPrice;
		county_price = countyPrice;
	}
	
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
	
	


	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getArea_price() {
		return area_price;
	}

	public void setArea_price(String areaPrice) {
		area_price = areaPrice;
	}

	public String getCounty_price() {
		return county_price;
	}

	public void setCounty_price(String countyPrice) {
		county_price = countyPrice;
	}

	public String getCounty_name() {
		return county_name;
	}


	public void setCounty_name(String countyName) {
		county_name = countyName;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

}