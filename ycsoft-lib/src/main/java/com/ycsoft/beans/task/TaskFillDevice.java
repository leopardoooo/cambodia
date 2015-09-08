package com.ycsoft.beans.task;

public class TaskFillDevice {
	private String deviceId;
	private String deviceModel;
	private String deviceCode;
	
	String oldDeviceCode;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getOldDeviceCode() {
		return oldDeviceCode;
	}

	public void setOldDeviceCode(String oldDeviceCode) {
		this.oldDeviceCode = oldDeviceCode;
	}

	
}
