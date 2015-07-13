/**
 * RDeviceSupplier.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * RDeviceSupplier -> R_DEVICE_SUPPLIER mapping
 */
@POJO(
	tn="R_DEVICE_SUPPLIER",
	sn="SEQ_SUPPLIER_ID",
	pk="SUPPLIER_ID")
public class RDeviceSupplier implements Serializable {

	// RDeviceSupplier all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7176352856692294545L;
	private String supplier_id ;
	private String supplier_name ;

	/**
	 * default empty constructor
	 */
	public RDeviceSupplier() {}


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