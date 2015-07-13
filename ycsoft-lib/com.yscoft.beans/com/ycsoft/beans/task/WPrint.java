/**
 * WPrint.java	2013/08/23
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * WPrint -> W_PRINT mapping 
 */
@POJO(
	tn="W_PRINT",
	sn="",
	pk="")
public class WPrint implements Serializable {
	
	// WPrint all properties 

	private String work_id ;	
	private Date print_time ;	
	private String print_optr ;	
	
	/**
	 * default empty constructor
	 */
	public WPrint() {}
	
	
	// work_id getter and setter
	public String getWork_id(){
		return this.work_id ;
	}
	
	public void setWork_id(String work_id){
		this.work_id = work_id ;
	}
	
	// print_time getter and setter
	public Date getPrint_time(){
		return this.print_time ;
	}
	
	public void setPrint_time(Date print_time){
		this.print_time = print_time ;
	}
	
	// print_optr getter and setter
	public String getPrint_optr(){
		return this.print_optr ;
	}
	
	public void setPrint_optr(String print_optr){
		this.print_optr = print_optr ;
	}

}