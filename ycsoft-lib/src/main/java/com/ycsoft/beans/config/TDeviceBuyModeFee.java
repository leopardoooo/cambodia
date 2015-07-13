/**
 * TDeviceBuyModeFee.java	2010/10/31
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TDeviceBuyModeFee -> T_DEVICE_BUY_MODE_FEE mapping
 */
@POJO(
	tn="T_DEVICE_BUY_MODE_FEE",
	sn="",
	pk="buy_mode")
public class TDeviceBuyModeFee implements Serializable {

	// TDeviceBuyModeFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3623117757454270291L;
	private String buy_mode ;
	private String fee_id ;

	/**
	 * default empty constructor
	 */
	public TDeviceBuyModeFee() {}


	// buy_mode getter and setter
	public String getBuy_mode(){
		return buy_mode ;
	}

	public void setBuy_mode(String buy_mode){
		this.buy_mode = buy_mode ;
	}

	// fee_id getter and setter
	public String getFee_id(){
		return fee_id ;
	}

	public void setFee_id(String fee_id){
		this.fee_id = fee_id ;
	}

}