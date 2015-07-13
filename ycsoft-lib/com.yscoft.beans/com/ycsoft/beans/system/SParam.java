/**
 * SParam.java	2010/11/02
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * SParam -> S_PARAM mapping
 */
@POJO(
	tn="S_PARAM",
	sn="",
	pk="")
public class SParam implements Serializable {

	// SParam all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8132900188823656480L;
	private String param_name ;
	private String param_value ;

	/**
	 * default empty constructor
	 */
	public SParam() {}


	// param_name getter and setter
	public String getParam_name(){
		return param_name ;
	}

	public void setParam_name(String param_name){
		this.param_name = param_name ;
	}

	// param_value getter and setter
	public String getParam_value(){
		return param_value ;
	}

	public void setParam_value(String param_value){
		this.param_value = param_value ;
	}

}