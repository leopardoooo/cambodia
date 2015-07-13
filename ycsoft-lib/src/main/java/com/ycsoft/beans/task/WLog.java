/**
 * WLog.java	2013/09/03
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.daos.config.POJO ;


/**
 * WLog -> W_LOG mapping 
 */
@POJO(
	tn="W_LOG",
	sn="",
	pk="")
public class WLog extends OptrBase implements Serializable {
	
	// WLog all properties 
	private Integer done_code ;
	private String work_id ;	
	private Date done_date ;	
	private String done_type ;		
	private String info ;	
	
	/**
	 * default empty constructor
	 */
	public WLog() {}
	
	
	public Integer getDone_code() {
		return done_code;
	}


	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}


	// work_id getter and setter
	public String getWork_id(){
		return this.work_id ;
	}
	
	public void setWork_id(String work_id){
		this.work_id = work_id ;
	}
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// done_type getter and setter
	public String getDone_type(){
		return this.done_type ;
	}
	
	public void setDone_type(String done_type){
		this.done_type = done_type ;
	}
	
	// info getter and setter
	public String getInfo(){
		return this.info ;
	}
	
	public void setInfo(String info){
		this.info = info ;
	}

}