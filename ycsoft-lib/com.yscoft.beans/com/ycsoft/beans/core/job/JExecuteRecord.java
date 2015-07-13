/**
 * JExecuteRecord.java	2011/07/01
 */
 
package com.ycsoft.beans.core.job; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * JExecuteRecord -> J_EXECUTE_RECORD mapping 
 */
@POJO(
	tn="J_EXECUTE_RECORD",
	sn="",
	pk="")
public class JExecuteRecord implements Serializable {
	
	// JExecuteRecord all properties 

	private String job_name ;	
	private String params ;	
	private Date begin_time ;	
	private Date end_time ;	
	private Integer exec_times ;	
	private Integer job_count ;	
	private Integer valid_count ;	
	private Integer success_count ;	
	private Integer failure_count ;	
	private String remark ;	
	
	/**
	 * default empty constructor
	 */
	public JExecuteRecord() {}
	
	
	// job_name getter and setter
	public String getJob_name(){
		return this.job_name ;
	}
	
	public void setJob_name(String job_name){
		this.job_name = job_name ;
	}
	
	// params getter and setter
	public String getParams(){
		return this.params ;
	}
	
	public void setParams(String params){
		this.params = params ;
	}
	
	// begin_time getter and setter
	public Date getBegin_time(){
		return this.begin_time ;
	}
	
	public void setBegin_time(Date begin_time){
		this.begin_time = begin_time ;
	}
	
	// end_time getter and setter
	public Date getEnd_time(){
		return this.end_time ;
	}
	
	public void setEnd_time(Date end_time){
		this.end_time = end_time ;
	}
	
	// exec_times getter and setter
	public Integer getExec_times(){
		return this.exec_times ;
	}
	
	public void setExec_times(Integer exec_times){
		this.exec_times = exec_times ;
	}
	
	// job_count getter and setter
	public Integer getJob_count(){
		return this.job_count ;
	}
	
	public void setJob_count(Integer job_count){
		this.job_count = job_count ;
	}
	
	// valid_count getter and setter
	public Integer getValid_count(){
		return this.valid_count ;
	}
	
	public void setValid_count(Integer valid_count){
		this.valid_count = valid_count ;
	}
	
	// success_count getter and setter
	public Integer getSuccess_count(){
		return this.success_count ;
	}
	
	public void setSuccess_count(Integer success_count){
		this.success_count = success_count ;
	}
	
	// failure_count getter and setter
	public Integer getFailure_count(){
		return this.failure_count ;
	}
	
	public void setFailure_count(Integer failure_count){
		this.failure_count = failure_count ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}

}