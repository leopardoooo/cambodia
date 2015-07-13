package com.ycsoft.beans.prod;

/**
 * PPromFeeProd.java	2012/06/28
 */
 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PPromFeeProd -> P_PROM_FEE_PROD mapping 
 */
@POJO(
	tn="P_PROM_FEE_PROD",
	sn="",
	pk="")
public class PPromFeeProd implements Serializable {
	
	// PPromFeeProd all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 2779436408429295249L;
	private String prom_fee_id ;	
	private String user_no ;	
	private String prod_id ;	
	private String tariff_id ;	
	private Integer real_pay ;	
	private Integer months ;	
	private String force_select ;
	
	private String prod_name;
	private String tariff_name;
	private String force_select_text;
	
	private String bind_prod_id;
	private String bind_prod_name;
	
	public String getBind_prod_id() {
		return bind_prod_id;
	}


	public void setBind_prod_id(String bind_prod_id) {
		this.bind_prod_id = bind_prod_id;
	}


	public String getBind_prod_name() {
		return bind_prod_name;
	}


	public void setBind_prod_name(String bind_prod_name) {
		this.bind_prod_name = bind_prod_name;
	}


	/**
	 * default empty constructor
	 */
	public PPromFeeProd() {}
	
	
	// prom_fee_id getter and setter
	public String getProm_fee_id(){
		return this.prom_fee_id ;
	}
	
	public void setProm_fee_id(String prom_fee_id){
		this.prom_fee_id = prom_fee_id ;
	}
	
	// user_no getter and setter
	public String getUser_no(){
		return this.user_no ;
	}
	
	public void setUser_no(String user_no){
		this.user_no = user_no ;
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
	
	// real_pay getter and setter
	public Integer getReal_pay(){
		return this.real_pay ;
	}
	
	public void setReal_pay(Integer real_pay){
		this.real_pay = real_pay ;
	}
	
	// months getter and setter
	public Integer getMonths(){
		return this.months ;
	}
	
	public void setMonths(Integer months){
		this.months = months ;
	}
	
	// force_select getter and setter
	public String getForce_select(){
		return this.force_select ;
	}
	
	public void setForce_select(String force_select){
		this.force_select = force_select ;
		this.force_select_text = MemoryDict.getDictName(DictKey.BOOLEAN, force_select);
	}

	// prod_name getter and setter
	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}


	public String getForce_select_text() {
		return force_select_text;
	}


	public void setForce_select_text(String forceSelectText) {
		force_select_text = forceSelectText;
	}


	public String getTariff_name() {
		return tariff_name;
	}


	public void setTariff_name(String tariffName) {
		tariff_name = tariffName;
	}
	
	
}