package com.ycsoft.beans.core.bill;

/**
 * TvOrder.java	2011/09/02
 */
 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * TvOrder -> TV_ORDER mapping 
 */
@POJO(
	tn="TV_ORDER",
	sn="",
	pk="")
public class TvOrder implements Serializable {
	
	// TvOrder all properties 

	private String user_id ;	
	private String prod_id ;	
	private String tariff_id ;	
	private Integer fee ;	
	private Date order_date ;	
	private String county_id ;	
	
	
	
	/**
	 * default empty constructor
	 */
	public TvOrder() {}
	
	public TvOrder(String user_id,String prod_id,String tariff_id,Integer fee,String county_id){
		this.user_id = user_id;
		this.prod_id = prod_id;
		this.tariff_id = tariff_id;
		this.fee = fee;
		this.county_id = county_id;
	}
	
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// prod_id getter and setter
	public String getProd_id(){
		return this.prod_id ;
	}
	
	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}
	
	// tariff_id getter and setter
	public String getTariff_id(){
		return this.tariff_id ;
	}
	
	public void setTariff_id(String tariff_id){
		this.tariff_id = tariff_id ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
	}
	
	// order_date getter and setter
	public Date getOrder_date(){
		return this.order_date ;
	}
	
	public void setOrder_date(Date order_date){
		this.order_date = order_date ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}