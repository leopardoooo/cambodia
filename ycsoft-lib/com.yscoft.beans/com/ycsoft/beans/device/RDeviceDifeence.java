/**
 * RDeviceDifeence.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * RDeviceDifeence -> R_DEVICE_DIFEENCE mapping
 */
@POJO(
	tn="R_DEVICE_DIFEENCE",
	sn="",
	pk="")
public class RDeviceDifeence extends RDevice implements Serializable {

	// RDeviceDifeence all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	private Integer device_done_code ;
	private String device_Id;
	private String depot_id ;
	private Date create_time ;
	private String optr_id ;
	private String remark ;

	public String getDevice_Id() {
		return device_Id;
	}


	public void setDevice_Id(String device_Id) {
		this.device_Id = device_Id;
	}


	/**
	 * default empty constructor
	 */
	public RDeviceDifeence() {}


	// device_done_code getter and setter
	public Integer getDevice_done_code(){
		return device_done_code ;
	}

	public void setDevice_done_code(Integer device_done_code){
		this.device_done_code = device_done_code ;
	}

	// depot_id getter and setter
	public String getDepot_id(){
		return depot_id ;
	}

	public void setDepot_id(String depot_id){
		this.depot_id = depot_id ;
	}


	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

	// optr_id getter and setter
	public String getOptr_id(){
		return optr_id ;
	}

	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}

}