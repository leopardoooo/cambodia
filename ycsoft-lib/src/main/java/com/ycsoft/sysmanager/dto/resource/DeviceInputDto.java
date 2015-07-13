package com.ycsoft.sysmanager.dto.resource;

import java.util.List;

import com.ycsoft.beans.device.RDeviceInput;
import com.ycsoft.business.dto.device.DeviceDto;

public class DeviceInputDto {
	private RDeviceInput deviceInput;
	private List<DeviceDto> deviceDtoList;
	public RDeviceInput getDeviceInput() {
		return deviceInput;
	}
	public void setDeviceInput(RDeviceInput deviceInput) {
		this.deviceInput = deviceInput;
	}
	public List<DeviceDto> getDeviceDtoList() {
		return deviceDtoList;
	}
	public void setDeviceDtoList(List<DeviceDto> deviceDtoList) {
		this.deviceDtoList = deviceDtoList;
	}
}
