/**
 * JExecResult.java	2010/09/23
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * JExecResult -> J_EXEC_RESULT mapping
 */
@POJO(
	tn="J_EXEC_RESULT",
	sn="",
	pk="")
public class JExecResult implements Serializable {

	// JExecResult all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5052009321155771273L;
	private Integer job_id ;
	private Date exec_time ;
	private String success ;
	private String county_id ;
	private String area_id ;
	private String error_info ;

	/**
	 * default empty constructor
	 */
	public JExecResult() {}


	// job_id getter and setter
	public Integer getJob_id(){
		return job_id ;
	}

	public void setJob_id(Integer job_id){
		this.job_id = job_id ;
	}

	// exec_time getter and setter
	public Date getExec_time(){
		return exec_time ;
	}

	public void setExec_time(Date exec_time){
		this.exec_time = exec_time ;
	}

	// success getter and setter
	public String getSuccess(){
		return success ;
	}

	public void setSuccess(String success){
		this.success = success ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

	// area_id getter and setter
	public String getArea_id(){
		return area_id ;
	}

	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}

	// error_info getter and setter
	public String getError_info(){
		return error_info ;
	}

	public void setError_info(String error_info){
		this.error_info = error_info ;
	}

}