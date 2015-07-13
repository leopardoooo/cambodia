/**
 * PProdResChange.java	2010/10/10
 */


package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.daos.config.POJO;


/**
 * PProdResChange -> P_PROD_RES_CHANGE mapping
 */
@POJO(
	tn="P_PROD_RES_CHANGE",
	sn="",
	pk="")
public class PProdResChange implements Serializable {

	// PProdResChange all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7962759001175780449L;
	private String prod_id ;
	private String county_id ;
	private String res_id ;
	private String optr_id ;
	private String change_type ;
	private Date create_time ;
	private Date deal_time ;

	/**
	 * default empty constructor
	 */
	public PProdResChange() {}
	public PProdResChange(String prodId, String countyId,String resId, String optrId,String changeType) {
		prod_id = prodId;
		county_id = countyId;
		res_id = resId;
		optr_id = optrId;
		change_type = changeType;
		create_time =DateHelper.now();
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
	}

	// res_id getter and setter
	public String getRes_id(){
		return res_id ;
	}

	public void setRes_id(String res_id){
		this.res_id = res_id ;
	}

	// optr_id getter and setter
	public String getOptr_id(){
		return optr_id ;
	}

	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

	// change_type getter and setter
	public String getChange_type(){
		return change_type ;
	}

	public void setChange_type(String change_type){
		this.change_type = change_type ;
	}

	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

	// deal_time getter and setter
	public Date getDeal_time(){
		return deal_time ;
	}

	public void setDeal_time(Date deal_time){
		this.deal_time = deal_time ;
	}

}