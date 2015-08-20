package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.beans.device.RDeviceProcure;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class DeviceProcureDto extends RDeviceProcure {
	
	private String device_type;
	private String device_model;
	private Integer count;
	private String device_code;
	private String device_id;
	
	private String device_type_text;
	private String device_model_text;
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
		this.device_type_text = MemoryDict.getDictName(DictKey.DEVICE_TYPE, device_type);
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getDevice_type_text() {
		return device_type_text;
	}
	public String getDevice_model_text() {
		String deviceType = getDevice_type();
		if(!"STB".equals(deviceType) && !"CARD".equals(deviceType) && !"MODEM".equals(deviceType)){
			deviceType = "CTL";
		}
		return MemoryDict.getDictName(deviceType+"_MODEL", getDevice_model())+"("+getDevice_model()+")";
	}
	public String getDevice_code() {
		return device_code;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
}
