/**
 * TAcctitemToProd.java	2010/08/19
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TAcctitemToProd -> T_ACCTITEM_TO_PROD mapping
 */
@POJO(
	tn="T_ACCTITEM_TO_PROD",
	sn="",
	pk="")
public class TAcctitemToProd implements Serializable {

	// TAcctitemToProd all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4574579642222495228L;
	private String acctitem_id ;
	private String prod_id ;
	private String prod_name;

	/**
	 * default empty constructor
	 */
	public TAcctitemToProd() {}


	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return acctitem_id ;
	}

	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}

	// prod_id getter and setter
	public String getProd_id(){
		return prod_id ;
	}

	public void setProd_id(String prod_id){
		this.prod_id = prod_id ;
	}


	public String getProd_name() {
		return prod_name;
	}


	public void setProd_name(String prodName) {
		prod_name = prodName;
	}

}