/**
 * TOptrLogin.java	2010/11/26
 */
 
package com.ycsoft.beans.system; 

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TOptrLogin -> T_OPTR_LOGIN mapping 
 */
@POJO(
	tn="T_OPTR_LOGIN",
	sn="",
	pk="LOGIN_ID")
public class SOptrLogin implements Serializable {
	
	// TOptrLogin all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 3822838051693178979L;
	private Integer login_id ;	
	private String optr_id ;	
	private String login_date ;	
	private String login_ip ;	
	
	/**
	 * default empty constructor
	 */
	public SOptrLogin() {}
	
	
	// login_id getter and setter
	public Integer getLogin_id(){
		return this.login_id ;
	}
	
	public void setLogin_id(Integer login_id){
		this.login_id = login_id ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// login_date getter and setter
	public String getLogin_date(){
		return this.login_date ;
	}
	
	public void setLogin_date(String login_date){
		this.login_date = login_date ;
	}
	
	// login_ip getter and setter
	public String getLogin_ip(){
		return this.login_ip ;
	}
	
	public void setLogin_ip(String login_ip){
		this.login_ip = login_ip ;
	}

}