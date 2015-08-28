package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.commons.store.MemoryDict;

public class MaterialModelDto {
	private String device_model;
	private String device_model_text;
	private String device_id;
	
	private Integer total_num;

	
	
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getDevice_model_text() {
		device_model_text=MemoryDict.getDictName("FITTING_MODEL", device_model);
		return device_model_text;
	}

	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
	}

	public Integer getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}
	
	
	
	
}
