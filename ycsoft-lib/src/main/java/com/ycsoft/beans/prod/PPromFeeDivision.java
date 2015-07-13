package com.ycsoft.beans.prod;

/**
 * PPromFeeDivision.java	2012/08/06
 */
 

import java.io.Serializable ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PPromFeeDivision -> P_PROM_FEE_DIVISION mapping 
 */
@POJO(
	tn="P_PROM_FEE_DIVISION",
	sn="",
	pk="")
public class PPromFeeDivision implements Serializable {
	
	// PPromFeeDivision all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -2071966709273556160L;
	
	private String prom_fee_id ;	
	private String user_no ;	
	private String prod_id ;	
	private String tariff_id ;	
	private Integer percent_value ;	
	private Integer percent ;	
	private String type ;	
	
	private String prod_name;
	private String tariff_name;
	private Integer real_pay;
	private String type_text;
	
	/**
	 * default empty constructor
	 */
	public PPromFeeDivision() {}
	
	
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
	
	// persent_value getter and setter
	public Integer getPercent_value(){
		return this.percent_value ;
	}
	
	public void setPercent_value(Integer percent_value){
		this.percent_value = percent_value ;
	}
	
	// percent getter and setter
	public Integer getPercent(){
		return this.percent ;
	}
	
	public void setPercent(Integer percent){
		this.percent = percent ;
	}
	
	// type getter and setter
	public String getType(){
		return this.type ;
	}
	
	public void setType(String type){
		this.type = type ;
		this.type_text = MemoryDict.getDictName(DictKey.SEPARATE_TYPE, type);
	}


	public String getProd_name() {
		return prod_name;
	}


	public void setProd_name(String prodName) {
		prod_name = prodName;
	}


	public String getTariff_name() {
		return tariff_name;
	}


	public void setTariff_name(String tariffName) {
		tariff_name = tariffName;
	}


	public Integer getReal_pay() {
		return real_pay;
	}


	public void setReal_pay(Integer realPay) {
		real_pay = realPay;
	}


	public String getType_text() {
		return type_text;
	}


	public void setType_text(String typeText) {
		type_text = typeText;
	}

	
}