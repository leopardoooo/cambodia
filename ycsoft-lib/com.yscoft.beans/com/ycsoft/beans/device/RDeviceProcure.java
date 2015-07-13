/**
 * RDeviceProcure.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * RDeviceProcure -> R_DEVICE_PROCURE mapping
 */
@POJO(
	tn="R_DEVICE_PROCURE",
	sn="",
	pk="")
public class RDeviceProcure implements Serializable {

	// RDeviceProcure all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8161376910470688227L;
	private Integer device_done_code ;
	private String procure_no;
	private String depot_id ;
	private String procure_dept ;
	private String procurer ;
	private String procure_type ;
	private String doc_type ;
	private String doc_no ;
	private String optr_id ;
	private Date create_time ;
	private String remark ;

	private String procure_type_text;
	/**
	 * default empty constructor
	 */
	public RDeviceProcure() {}


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

	// procure_dept getter and setter
	public String getProcure_dept(){
		return procure_dept ;
	}

	public void setProcure_dept(String procure_dept){
		this.procure_dept = procure_dept ;
	}

	// procurer getter and setter
	public String getProcurer(){
		return procurer ;
	}

	public void setProcurer(String procurer){
		this.procurer = procurer ;
	}

	// procure_type getter and setter
	public String getProcure_type(){
		return procure_type ;
	}

	public void setProcure_type(String procure_type){
		procure_type_text = MemoryDict.getDictName("DEPOT_BUY_MODE", procure_type);
		this.procure_type = procure_type ;
	}

	// doc_type getter and setter
	public String getDoc_type(){
		return doc_type ;
	}

	public void setDoc_type(String doc_type){
		this.doc_type = doc_type ;
	}

	// doc_no getter and setter
	public String getDoc_no(){
		return doc_no ;
	}

	public void setDoc_no(String doc_no){
		this.doc_no = doc_no ;
	}

	// optr_id getter and setter
	public String getOptr_id(){
		return optr_id ;
	}

	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}


	public String getProcure_type_text() {
		return procure_type_text;
	}


	public String getProcure_no() {
		return procure_no;
	}


	public void setProcure_no(String procure_no) {
		this.procure_no = procure_no;
	}

}