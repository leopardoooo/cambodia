package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.beans.device.RDeviceReclaim;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class DeviceReclaimDto extends RDeviceReclaim {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1188101391370236145L;
	
	private String device_type;
	private String device_code;
	private String depot_status;
	private String pair_device_code;
	private String pair_device_modem_code;
	
	private String device_type_text;
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
		device_type_text = MemoryDict.getDictName(DictKey.DEVICE_TYPE, device_type);
	}
	public String getDevice_code() {
		return device_code;
	}
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	public String getDevice_type_text() {
		return device_type_text;
	}
	public String getDepot_status() {
		return depot_status;
	}
	public void setDepot_status(String depot_status) {
		this.depot_status = depot_status;
	}
	public String getPair_device_code() {
		return pair_device_code;
	}
	public void setPair_device_code(String pair_device_code) {
		this.pair_device_code = pair_device_code;
	}
	public String getPair_device_modem_code() {
		return pair_device_modem_code;
	}
	public void setPair_device_modem_code(String pair_device_modem_code) {
		this.pair_device_modem_code = pair_device_modem_code;
	}
	
}
