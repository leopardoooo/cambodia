/**
 * TConfig.java	2010/09/05
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TConfig -> T_CONFIG mapping
 */
@POJO(
	tn="T_CONFIG",
	sn="",
	pk="CONFIG_NAME")
public class TConfig implements Serializable {

	// TConfig all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8790005956539664164L;
	private String config_name ;
	private String param_name ;
	private String form_type;
	private String remark ;

	/**
	 * default empty constructor
	 */
	public TConfig() {}


	// config_name getter and setter
	public String getConfig_name(){
		return config_name ;
	}

	public void setConfig_name(String config_name){
		this.config_name = config_name ;
	}

	// param_name getter and setter
	public String getParam_name(){
		return param_name ;
	}

	public void setParam_name(String param_name){
		this.param_name = param_name ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}


	public String getForm_type() {
		return form_type;
	}


	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}

}