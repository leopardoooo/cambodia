/**
 * TServerSupplier.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TServerSupplier -> T_SERVER_SUPPLIER mapping
 */
@POJO(
	tn="T_SERVER_SUPPLIER",
	sn="",
	pk="")
public class TServerSupplier implements Serializable {

	// TServerSupplier all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2230683687576207628L;
	private String serverer_type ;
	private String supplier_id ;
	private String supplier_name ;

	/**
	 * default empty constructor
	 */
	public TServerSupplier() {}


	// serverer_type getter and setter
	public String getServerer_type(){
		return serverer_type ;
	}

	public void setServerer_type(String serverer_type){
		this.serverer_type = serverer_type ;
	}

	// supplier_id getter and setter
	public String getSupplier_id(){
		return supplier_id ;
	}

	public void setSupplier_id(String supplier_id){
		this.supplier_id = supplier_id ;
	}

	// supplier_name getter and setter
	public String getSupplier_name(){
		return supplier_name ;
	}

	public void setSupplier_name(String supplier_name){
		this.supplier_name = supplier_name ;
	}

}