/**
 * TAdjustData.java	2012/08/13
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * TAdjustData -> T_ADJUST_DATA mapping 
 */
@POJO(
	tn="T_ADJUST_DATA",
	sn="",
	pk="")
public class TAdjustData implements Serializable {
	
	// TAdjustData all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 2198250156308665593L;
	private String optr_id ;	
	private Date adjust_date ;	
	
	/**
	 * default empty constructor
	 */
	public TAdjustData() {}
	
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// adjust_date getter and setter
	public Date getAdjust_date(){
		return this.adjust_date ;
	}
	
	public void setAdjust_date(Date adjust_date){
		this.adjust_date = adjust_date ;
	}

}