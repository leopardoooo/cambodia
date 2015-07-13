package com.ycsoft.beans.core.cust;

/**
 * CCustAddr.java	2010/10/13
 */


import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;


/**
 * CCustAddr -> C_CUST_ADDR mapping
 */
@POJO(
	tn="C_CUST_ADDR",
	sn="",
	pk="CUST_ID")
public class CCustAddr extends BusiBase implements Serializable {

	// CCustAddr all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2216075460103907093L;
	private String cust_id ;
	private String t1 ;
	private String t2 ;
	private String t3 ;
	private String t4 ;
	private String t5 ;
	private String note ;

	/**
	 * default empty constructor
	 */
	public CCustAddr() {}


	// cust_id getter and setter
	public String getCust_id(){
		return cust_id ;
	}

	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}

	// t1 getter and setter
	public String getT1(){
		return t1 ;
	}

	public void setT1(String t1){
		this.t1 = t1 ;
	}

	// t2 getter and setter
	public String getT2(){
		return t2 ;
	}

	public void setT2(String t2){
		this.t2 = t2 ;
	}

	// t3 getter and setter
	public String getT3(){
		return t3 ;
	}

	public void setT3(String t3){
		this.t3 = t3 ;
	}

	// t4 getter and setter
	public String getT4(){
		return t4 ;
	}

	public void setT4(String t4){
		this.t4 = t4 ;
	}

	// t5 getter and setter
	public String getT5(){
		return t5 ;
	}

	public void setT5(String t5){
		this.t5 = t5 ;
	}

	// note getter and setter
	public String getNote(){
		return note ;
	}

	public void setNote(String note){
		this.note = note ;
	}

}