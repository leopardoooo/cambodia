/**
 * RDeviceOutput.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * RDeviceOutput -> R_DEVICE_OUTPUT mapping
 */
@POJO(
	tn="R_DEVICE_OUTPUT",
	sn="",
	pk="")
public class RDeviceOutput extends RDeviceDoneDetail implements Serializable {

	// RDeviceOutput all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2290684723998068616L;
	private Integer device_done_code ;
	private String output_no ;
	private String depot_id ;
	private String supplier_id ;
	private String output_type ;
	private Date create_time ;
	private String optr_id ;
	private String remark ;

	private String supplier_name ;
	private String output_type_text;

	/**
	 * default empty constructor
	 */
	public RDeviceOutput() {}


	// device_done_code getter and setter
	public Integer getDevice_done_code(){
		return device_done_code ;
	}

	public void setDevice_done_code(Integer device_done_code){
		this.device_done_code = device_done_code ;
	}

	// output_no getter and setter
	public String getOutput_no(){
		return output_no ;
	}

	public void setOutput_no(String output_no){
		this.output_no = output_no ;
	}

	// depot_id getter and setter
	public String getDepot_id(){
		return depot_id ;
	}

	public void setDepot_id(String depot_id){
		this.depot_id = depot_id ;
	}

	// supplier_id getter and setter
	public String getSupplier_id(){
		return supplier_id ;
	}

	public void setSupplier_id(String supplier_id){
		this.supplier_id = supplier_id ;
	}

	// output_type getter and setter
	public String getOutput_type(){
		return output_type ;
	}

	public void setOutput_type(String output_type){
		output_type_text = MemoryDict.getDictName(DictKey.DEVICE_OUT_TYPE, output_type);
		this.output_type = output_type ;
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


	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}


	/**
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}


	/**
	 * @return the output_type_text
	 */
	public String getOutput_type_text() {
		return output_type_text;
	}

}