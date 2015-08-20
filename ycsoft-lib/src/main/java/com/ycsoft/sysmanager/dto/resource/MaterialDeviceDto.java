package com.ycsoft.sysmanager.dto.resource;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class MaterialDeviceDto {
	private String device_type;
	private String device_type_text;
	private List<MaterialModelDto> materialList = new ArrayList<MaterialModelDto>();
	
	
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE,
				device_type);
		this.device_type = device_type;
	}
	public String getDevice_type_text() {
		return device_type_text;
	}
	public void setDevice_type_text(String device_type_text) {
		this.device_type_text = device_type_text;
	}
	public List<MaterialModelDto> getMaterialList() {
		return materialList;
	}
	public void setMaterialList(List<MaterialModelDto> materialList) {
		this.materialList = materialList;
	}
	

	
}
