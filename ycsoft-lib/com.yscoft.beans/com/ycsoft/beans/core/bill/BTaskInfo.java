/**
 * BTaskInfo.java	2011/05/27
 */
 
package com.ycsoft.beans.core.bill; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * BTaskInfo -> B_TASK_INFO mapping 
 */
@POJO(
	tn="B_TASK_INFO",
	sn="",
	pk="")
public class BTaskInfo implements Serializable {
	
	// BTaskInfo all properties 

	private String task_code ;	
	private String county_id ;	
	private String area_id ;	
	private String task_info ;	
	private String mail_title ;	
	
	/**
	 * default empty constructor
	 */
	public BTaskInfo() {}
	
	
	// task_code getter and setter
	public String getTask_code(){
		return this.task_code ;
	}
	
	public void setTask_code(String task_code){
		this.task_code = task_code ;
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
	
	// task_info getter and setter
	public String getTask_info(){
		return this.task_info ;
	}
	
	public void setTask_info(String task_info){
		this.task_info = task_info ;
	}
	
	// mail_title getter and setter
	public String getMail_title(){
		return this.mail_title ;
	}
	
	public void setMail_title(String mail_title){
		this.mail_title = mail_title ;
	}

}