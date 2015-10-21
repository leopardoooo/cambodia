package com.ycsoft.beans.task;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "W_TASK_USER", sn = "", pk = "task_id,user_id")
public class WTaskUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3877059523414671043L;
	private String task_id;
	private String user_id;
	private String device_model;
	private String device_id;
	private String recycle_device;
	private String recycle_result;
	private String user_type;
	
	private String user_type_text;
	private String device_model_text;
	private String recycle_result_text;
	
	private String occ_no;
	private String pos_no;
	
	
	public String getOcc_no() {
		return occ_no;
	}
	public void setOcc_no(String occ_no) {
		this.occ_no = occ_no;
	}
	public String getPos_no() {
		return pos_no;
	}
	public void setPos_no(String pos_no) {
		this.pos_no = pos_no;
	}
	public String getUser_type_text() {
		return user_type_text;
	}
	public void setUser_type_text(String user_type_text) {
		this.user_type_text = user_type_text;
	}

	public String getRecycle_result_text() {
		return recycle_result_text;
	}
	public void setRecycle_result_text(String recycle_result_text) {
		this.recycle_result_text = recycle_result_text;
	}
	public String getDevice_model_text() {
		return device_model_text;
	}
	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
		device_model_text = MemoryDict.getDictName(DictKey.DEVICE_MODEL, this.device_model);
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
	
	public String getRecycle_device() {
		return recycle_device;
	}
	public void setRecycle_device(String recycle_device) {
		this.recycle_device = recycle_device;
	}
	public String getRecycle_result() {
		return recycle_result;
	}
	public void setRecycle_result(String recycle_result) {
		this.recycle_result = recycle_result;
		recycle_result_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.recycle_result);
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
		user_type_text = MemoryDict.getDictName(DictKey.USER_TYPE, this.user_type);
	}

}
