/**
 * CProdInclude.java	2010/10/15
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * CProdInclude -> C_PROD_INCLUDE mapping
 */
@POJO(
	tn="C_PROD_INCLUDE",
	sn="",
	pk="")
public class CProdInclude implements Serializable {

	// CProdInclude all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -344364877942678323L;
	private String cust_id ;
	private String user_id ;
	private String prod_sn ;
	private String include_prod_sn ;

	/**
	 * default empty constructor
	 */
	public CProdInclude() {}


	// cust_id getter and setter
	public String getCust_id(){
		return cust_id ;
	}

	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}

	// user_id getter and setter
	public String getUser_id(){
		return user_id ;
	}

	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}

	// prod_sn getter and setter
	public String getProd_sn(){
		return prod_sn ;
	}

	public void setProd_sn(String prod_sn){
		this.prod_sn = prod_sn ;
	}

	// include_prod_sn getter and setter
	public String getInclude_prod_sn(){
		return include_prod_sn ;
	}

	public void setInclude_prod_sn(String include_prod_sn){
		this.include_prod_sn = include_prod_sn ;
	}

}