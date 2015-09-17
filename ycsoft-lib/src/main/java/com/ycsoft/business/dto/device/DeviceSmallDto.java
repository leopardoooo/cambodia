/**
 *
 */
package com.ycsoft.business.dto.device;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 *
 */
public class DeviceSmallDto {
	/**
	 *
	 */
	private String device_code;			//设备编号
	private String device_id;
	private String device_model;
	private String device_model_text;
	
	
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
		device_model_text = MemoryDict.getDictName(DictKey.DEVICE_MODEL, device_model);
	}
	public String getDevice_model_text() {
		return device_model_text;
	}
	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
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
