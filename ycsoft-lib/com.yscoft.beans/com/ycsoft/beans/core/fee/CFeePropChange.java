/**
 * CFeePropChange.java	2011/03/09
 */
 
package com.ycsoft.beans.core.fee; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CFeePropChange -> C_FEE_PROP_CHANGE mapping 
 */
@POJO(
	tn="C_FEE_PROP_CHANGE",
	sn="",
	pk="")
public class CFeePropChange implements Serializable {
	
	// CFeePropChange all properties 

	private String fee_sn ;	
	private String column_name ;	
	private String old_value ;	
	private String new_value ;	
	private Integer done_code ;	
	private Date change_time ;	
	private String county_id ;	
	private String area_id ;	
	
	/**
	 * default empty constructor
	 */
	public CFeePropChange() {}
	
	
	// fee_sn getter and setter
	public String getFee_sn(){
		return this.fee_sn ;
	}
	
	public void setFee_sn(String fee_sn){
		this.fee_sn = fee_sn ;
	}
	
	// column_name getter and setter
	public String getColumn_name(){
		return this.column_name ;
	}
	
	public void setColumn_name(String column_name){
		this.column_name = column_name ;
	}
	
	// old_value getter and setter
	public String getOld_value(){
		return this.old_value ;
	}
	
	public void setOld_value(String old_value){
		this.old_value = old_value ;
	}
	
	// new_value getter and setter
	public String getNew_value(){
		return this.new_value ;
	}
	
	public void setNew_value(String new_value){
		this.new_value = new_value ;
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// change_time getter and setter
	public Date getChange_time(){
		return this.change_time ;
	}
	
	public void setChange_time(Date change_time){
		this.change_time = change_time ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}

}