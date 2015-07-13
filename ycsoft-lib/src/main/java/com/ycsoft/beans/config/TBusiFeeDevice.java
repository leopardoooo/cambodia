/**
 * TBusiFeeDevice.java	2010/10/30
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TBusiFeeDevice -> T_BUSI_FEE_DEVICE mapping
 */
@POJO(
	tn="T_BUSI_FEE_DEVICE",
	sn="",
	pk="")
public class TBusiFeeDevice implements Serializable {

	// TBusiFeeDevice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3727977797947573574L;
	private String fee_std_id ;
	private String device_buy_mode ;
	private String device_type ;
	private String device_model ;

	private String model_name;
	/**
	 * default empty constructor
	 */
	public TBusiFeeDevice() {}


	// fee_std_id getter and setter
	public String getFee_std_id(){
		return fee_std_id ;
	}

	public void setFee_std_id(String fee_std_id){
		this.fee_std_id = fee_std_id ;
	}

	// device_buy_mode getter and setter
	public String getDevice_buy_mode(){
		return device_buy_mode ;
	}

	public void setDevice_buy_mode(String device_buy_mode){
		this.device_buy_mode = device_buy_mode ;
	}

	// device_type getter and setter
	public String getDevice_type(){
		return device_type ;
	}

	public void setDevice_type(String device_type){
		this.device_type = device_type ;
	}

	// device_model getter and setter
	public String getDevice_model(){
		return device_model ;
	}

	public void setDevice_model(String device_model){
		this.device_model = device_model ;
	}


	public String getModel_name() {
		return model_name;
	}


	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

}