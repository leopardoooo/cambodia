package com.ycsoft.beans.task;

import com.ycsoft.business.dto.device.DeviceDto;

public class TaskFillDevice {
	
	private String deviceCode;//设备编号
	private String oldDeviceCode;//老设备编号
	
	private boolean fcPort;//是不是光口设备 如果有是光猫，如果没有是OTT机顶盒
	private String occNo;//交接箱编号  CFOCN工单有光口的设备必须设置
	private String posNo;//分光器编号 CFOCN工单有光口的设备必须设置
	
	private DeviceDto device;
	private DeviceDto oldDevice;
	
	private String userId;
	private String recycle_result;
	
	

	public String getRecycle_result() {
		return recycle_result;
	}

	public void setRecycle_result(String recycle_result) {
		this.recycle_result = recycle_result;
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

	

	public boolean isFcPort() {
		return fcPort;
	}

	public void setFcPort(boolean fcPort) {
		this.fcPort = fcPort;
	}

	public String getOccNo() {
		return occNo;
	}

	public void setOccNo(String occNo) {
		this.occNo = occNo;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public DeviceDto getDevice() {
		return device;
	}

	public void setDevice(DeviceDto device) {
		this.device = device;
	}

	public DeviceDto getOldDevice() {
		return oldDevice;
	}

	public void setOldDevice(DeviceDto oldDevice) {
		this.oldDevice = oldDevice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
