/**
 * WVisit.java	2013/08/23
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * WVisit -> W_VISIT mapping 
 */
@POJO(
	tn="W_VISIT",
	sn="",
	pk="")
public class WVisit implements Serializable {
	
	// WVisit all properties 

	private String work_id ;	
	private Date visit_time ;	
	private String visit_optr ;	
	private String satisfaction ;	
	private String satisfaction_remak ;	
	
	/**
	 * default empty constructor
	 */
	public WVisit() {}
	
	
	// work_id getter and setter
	public String getWork_id(){
		return this.work_id ;
	}
	
	public void setWork_id(String work_id){
		this.work_id = work_id ;
	}
	
	// visit_time getter and setter
	public Date getVisit_time(){
		return this.visit_time ;
	}
	
	public void setVisit_time(Date visit_time){
		this.visit_time = visit_time ;
	}
	
	// visit_optr getter and setter
	public String getVisit_optr(){
		return this.visit_optr ;
	}
	
	public void setVisit_optr(String visit_optr){
		this.visit_optr = visit_optr ;
	}
	
	// satisfaction getter and setter
	public String getSatisfaction(){
		return this.satisfaction ;
	}
	
	public void setSatisfaction(String satisfaction){
		this.satisfaction = satisfaction ;
	}
	
	// satisfaction_remak getter and setter
	public String getSatisfaction_remak(){
		return this.satisfaction_remak ;
	}
	
	public void setSatisfaction_remak(String satisfaction_remak){
		this.satisfaction_remak = satisfaction_remak ;
	}

}