package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class DeviceDetailInputDto {
	/**
	 *
	 */
	private String device_code;
	private String pair_device_code;
	private String box_no;
	private String device_model;
	private String device_model_text;
	
	
	public String getDevice_code() {
		return device_code;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
		device_model_text = MemoryDict.getDictName(DictKey.DEVICE_MODEL, this.device_model);
	}
	public String getDevice_model_text() {
		return device_model_text;
	}
	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public String getPair_device_code() {
		return pair_device_code;
	}
	public void setPair_device_code(String pair_device_code) {
		this.pair_device_code = pair_device_code;
	}
	public String getBox_no() {
		return box_no;
	}
	public void setBox_no(String box_no) {
		this.box_no = box_no;
	}

}
