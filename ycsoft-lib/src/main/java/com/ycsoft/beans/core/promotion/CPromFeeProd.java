package com.ycsoft.beans.core.promotion;

/**
 * CPromFeeProd.java	2012/07/11
 */
 
import java.io.Serializable ;
import java.util.Date;

import com.ycsoft.daos.config.POJO ;


/**
 * CPromFeeProd -> C_PROM_FEE_PROD mapping 
 */
@POJO(
	tn="C_PROM_FEE_PROD",
	sn="",
	pk="")
public class CPromFeeProd implements Serializable {
	
	// CPromFeeProd all properties 

	private String prom_fee_sn ;	
	private String user_id ;	
	private String prod_id ;	
	private Integer months ;	
	private Integer real_pay ;	
	private Integer should_pay ;
	private String prod_sn;
	private Date bind_invalid_date;
	private String tariff_id ;	
	
	
	public String getTariff_id() {
		return tariff_id;
	}


	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}


	/**
	 * default empty constructor
	 */
	public CPromFeeProd() {}
	
	
	// prom_fee_sn getter and setter
	public String getProm_fee_sn(){
		return this.prom_fee_sn ;
	}
	
	public void setProm_fee_sn(String prom_fee_sn){
		this.prom_fee_sn = prom_fee_sn ;
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
	
	// months getter and setter
	public Integer getMonths(){
		return this.months ;
	}
	
	public void setMonths(Integer months){
		this.months = months ;
	}
	
	// real_pay getter and setter
	public Integer getReal_pay(){
		return this.real_pay ;
	}
	
	public void setReal_pay(Integer real_pay){
		this.real_pay = real_pay ;
	}
	
	// should_pay getter and setter
	public Integer getShould_pay(){
		return this.should_pay ;
	}
	
	public void setShould_pay(Integer should_pay){
		this.should_pay = should_pay ;
	}


	public String getProd_sn() {
		return prod_sn;
	}


	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}


	public Date getBind_invalid_date() {
		return bind_invalid_date;
	}


	public void setBind_invalid_date(Date bind_invalid_date) {
		this.bind_invalid_date = bind_invalid_date;
	}

}