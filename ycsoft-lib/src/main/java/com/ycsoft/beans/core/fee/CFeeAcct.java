/**
 * CFeeAcct.java	2010/10/20
 */

package com.ycsoft.beans.core.fee;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * CFeeAcct -> C_FEE_ACCT mapping
 */
@POJO(
	tn="C_FEE_ACCT",
	sn="",
	pk="")
public class CFeeAcct extends CFee implements Serializable {

	// CFeeAcct all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3097355722882243757L;
	private String fee_sn ;
	private String disct_id ;
	private String prod_sn ;
	private Integer prod_free_days ;
	private Date begin_date;
	private Date prod_invalid_date ;
	private String tariff_id;

	/**
	 * default empty constructor
	 */
	public CFeeAcct() {}


	// fee_sn getter and setter
	@Override
	public String getFee_sn(){
		return fee_sn ;
	}

	@Override
	public void setFee_sn(String fee_sn){
		this.fee_sn = fee_sn ;
	}

	// disct_id getter and setter
	public String getDisct_id(){
		return disct_id ;
	}

	public void setDisct_id(String disct_id){
		this.disct_id = disct_id ;
	}

	// prod_sn getter and setter
	public String getProd_sn(){
		return prod_sn ;
	}

	public void setProd_sn(String prod_sn){
		this.prod_sn = prod_sn ;
	}

	// prod_free_days getter and setter
	public Integer getProd_free_days(){
		return prod_free_days ;
	}

	public void setProd_free_days(Integer prod_free_days){
		this.prod_free_days = prod_free_days ;
	}

	// prod_invalid_date getter and setter
	public Date getProd_invalid_date(){
		return prod_invalid_date ;
	}

	public void setProd_invalid_date(Date prod_invalid_date){
		this.prod_invalid_date = prod_invalid_date ;
	}


	public String getTariff_id() {
		return tariff_id;
	}


	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}


	public Date getBegin_date() {
		return begin_date;
	}


	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}


}