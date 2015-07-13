/**
 * TOpenTemp.java	2010/10/11
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TOpenTemp -> T_OPEN_TEMP mapping
 */
@POJO(
	tn="T_OPEN_TEMP",
	sn="",
	pk="")
public class TOpenTemp implements Serializable {

	// TOpenTemp all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1811306631465717072L;
	private String template_id ;
	private String user_type;
	private Integer cycle ;
	private Integer times ;
	private Integer days ;

	/**
	 * default empty constructor
	 */
	public TOpenTemp() {}


	// template_id getter and setter
	public String getTemplate_id(){
		return template_id ;
	}

	public void setTemplate_id(String template_id){
		this.template_id = template_id ;
	}

	// cycle getter and setter
	public Integer getCycle(){
		return cycle ;
	}

	public void setCycle(Integer cycle){
		this.cycle = cycle ;
	}

	// times getter and setter
	public Integer getTimes(){
		return times ;
	}

	public void setTimes(Integer times){
		this.times = times ;
	}

	// days getter and setter
	public Integer getDays(){
		return days ;
	}

	public void setDays(Integer days){
		this.days = days ;
	}


	public String getUser_type() {
		return user_type;
	}


	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

}