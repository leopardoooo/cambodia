/**
 * RDeviceDoneDeviceid.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * RDeviceDoneDeviceid -> R_DEVICE_DONE_DEVICEID mapping
 */
@POJO(
	tn="R_DEVICE_DONE_DEVICEID",
	sn="",
	pk="")
public class RDeviceDoneDeviceid implements Serializable {

	// RDeviceDoneDeviceid all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1812092076866285627L;
	private Integer device_done_code ;
	private String device_id ;

	/**
	 * default empty constructor
	 */
	public RDeviceDoneDeviceid() {}


	// device_done_code getter and setter
	public Integer getDevice_done_code(){
		return device_done_code ;
	}

	public void setDevice_done_code(Integer device_done_code){
		this.device_done_code = device_done_code ;
	}

	// device_id getter and setter
	public String getDevice_id(){
		return device_id ;
	}

	public void setDevice_id(String device_id){
		this.device_id = device_id ;
	}

}