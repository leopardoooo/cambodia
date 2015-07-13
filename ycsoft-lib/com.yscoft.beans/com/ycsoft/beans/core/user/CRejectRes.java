/**
 * CRejectRes.java	2010/12/02
 */
 
package com.ycsoft.beans.core.user; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CRejectRes -> C_REJECT_RES mapping 
 */
@POJO(
	tn="C_REJECT_RES",
	sn="",
	pk="")
public class CRejectRes implements Serializable {
	
	// CRejectRes all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -8579533652239928057L;
	private String user_id ;	
	private String cust_id ;	
	private String res_id ;	
	
	/**
	 * default empty constructor
	 */
	public CRejectRes() {}
	
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// res_id getter and setter
	public String getRes_id(){
		return this.res_id ;
	}
	
	public void setRes_id(String res_id){
		this.res_id = res_id ;
	}

}