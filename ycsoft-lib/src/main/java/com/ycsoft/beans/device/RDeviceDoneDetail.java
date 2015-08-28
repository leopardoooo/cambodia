/**
 * RDeviceDoneDetail.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * RDeviceDoneDetail -> R_DEVICE_DONE_DETAIL mapping
 */
@POJO(
	tn="R_DEVICE_DONE_DETAIL",
	sn="",
	pk="")
public class RDeviceDoneDetail implements Serializable {

	// RDeviceDoneDetail all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1893256560998039824L;
	private Integer device_done_code ;
	private String device_type ;
	private String device_model ;
	private Integer count ;

	private String device_type_text ;
	private String device_model_text;
	/**
	 * default empty constructor
	 */
	public RDeviceDoneDetail() {}


	// device_done_code getter and setter
	public Integer getDevice_done_code(){
		return device_done_code ;
	}

	public void setDevice_done_code(Integer device_done_code){
		this.device_done_code = device_done_code ;
	}

	// device_type getter and setter
	public String getDevice_type(){
		return device_type ;
	}

	public void setDevice_type(String device_type){
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE, device_type);
		this.device_type = device_type ;
	}

	// device_model getter and setter
	public String getDevice_model(){
		return device_model ;
	}

	public void setDevice_model(String device_model){
		this.device_model = device_model ;
	}

	// count getter and setter
	public Integer getCount(){
		return count ;
	}

	public void setCount(Integer count){
		this.count = count ;
	}


	/**
	 * @return the device_type_text
	 */
	public String getDevice_type_text() {
		return device_type_text;
	}

	public String getDevice_model_text() {
		return MemoryDict.getDictName(getDevice_type()+"_MODEL", getDevice_model())+"("+getDevice_model()+")";
	}

}