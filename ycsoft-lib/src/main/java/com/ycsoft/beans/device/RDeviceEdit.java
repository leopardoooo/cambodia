/**
 * RDeviceEdit.java	2010/11/15
 */
 
package com.ycsoft.beans.device; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * RDeviceEdit -> R_DEVICE_EDIT mapping 
 */
@POJO(
	tn="R_DEVICE_EDIT",
	sn="",
	pk="DEVICE_DONE_CODE")
public class RDeviceEdit extends RDeviceDoneDetail implements Serializable {
	
	// RDeviceEdit all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 3266936821506751118L;
	private Integer device_done_code ;	
	private String depot_id ;	
	private String edit_type ;	//数据字典中对应key
	private String optr_id ;	
	private Date create_time ;	
	private String remark ;	
	private String old_value;
	private String new_value;
	
	private String depot_id_text;
	private String old_value_text;
	private String new_value_text;
	
	public String getOld_value_text() {
		return MemoryDict.getDictName(getEdit_type(), getOld_value());
	}


	public String getNew_value_text() {
		return MemoryDict.getDictName(getEdit_type(), getNew_value());
	}


	public String getOld_value() {
		return old_value;
	}


	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}


	public String getNew_value() {
		return new_value;
	}


	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}


	/**
	 * default empty constructor
	 */
	public RDeviceEdit() {}
	
	
	// device_done_code getter and setter
	public Integer getDevice_done_code(){
		return this.device_done_code ;
	}
	
	public void setDevice_done_code(Integer device_done_code){
		this.device_done_code = device_done_code ;
	}
	
	// depot_id getter and setter
	public String getDepot_id(){
		return this.depot_id ;
	}
	
	public void setDepot_id(String depot_id){
		depot_id_text = MemoryDict.getDictName(DictKey.DEPOT, depot_id);
		this.depot_id = depot_id ;
	}
	
	// edit_type getter and setter
	public String getEdit_type(){
		return this.edit_type ;
	}
	
	public void setEdit_type(String edit_type){
		this.edit_type = edit_type ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}


	public String getDepot_id_text() {
		return depot_id_text;
	}

}