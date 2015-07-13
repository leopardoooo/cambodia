/**
 * TStbFilled.java	2010/10/11
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TStbFilled -> T_STB_FILLED mapping
 */
@POJO(
	tn="T_STB_FILLED",
	sn="",
	pk="")
public class TStbFilled implements Serializable {

	// TStbFilled all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7276612512097178698L;
	private String template_id ;
	private String res_id ;
	private Integer months ;

	private String res_name;

	/**
	 * default empty constructor
	 */
	public TStbFilled() {}


	// template_id getter and setter
	public String getTemplate_id(){
		return template_id ;
	}

	public void setTemplate_id(String template_id){
		this.template_id = template_id ;
	}

	// res_id getter and setter
	public String getRes_id(){
		return res_id ;
	}

	public void setRes_id(String res_id){
		this.res_id = res_id ;
	}

	// months getter and setter
	public Integer getMonths(){
		return months ;
	}

	public void setMonths(Integer months){
		this.months = months ;
	}


	public String getRes_name() {
		return res_name;
	}


	public void setRes_name(String res_name) {
		this.res_name = res_name;
	}

}