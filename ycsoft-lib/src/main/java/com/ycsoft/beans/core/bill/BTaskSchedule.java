/**
 * BTaskSchedule.java	2011/05/27
 */
 
package com.ycsoft.beans.core.bill; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * BTaskSchedule -> B_TASK_SCHEDULE mapping 
 */
@POJO(
	tn="B_TASK_SCHEDULE",
	sn="",
	pk="")
public class BTaskSchedule implements Serializable {
	
	// BTaskSchedule all properties 

	private String task_code ;	
	private String schedule_time ;	
	private String status ;	
	private String county_id ;	
	private String area_id ;	
	private Date create_time ;	
	private String optr_id ;	
	private Integer hst_day ;	
	
	/**
	 * default empty constructor
	 */
	public BTaskSchedule() {}
	
	
	// task_code getter and setter
	public String getTask_code(){
		return this.task_code ;
	}
	
	public void setTask_code(String task_code){
		this.task_code = task_code ;
	}
	
	// schedule_time getter and setter
	public String getSchedule_time(){
		return this.schedule_time ;
	}
	
	public void setSchedule_time(String schedule_time){
		this.schedule_time = schedule_time ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
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
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// hst_day getter and setter
	public Integer getHst_day(){
		return this.hst_day ;
	}
	
	public void setHst_day(Integer hst_day){
		this.hst_day = hst_day ;
	}

}