package com.ycsoft.business.dto.device;

import com.ycsoft.beans.core.fee.CFeeDevice;

public class BuyDeviceDto extends CFeeDevice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5674490096188982053L;
	private String device_model;
	private String buy_mode;

	/**
	 * @return the device_model
	 */
	public String getDevice_model() {
		return device_model;
	}

	/**
	 * @param device_model
	 *            the device_model to set
	 */
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	/**
	 * @return the buy_mode
	 */
	public String getBuy_mode() {
		return buy_mode;
	}

	/**
	 * @param buy_mode
	 *            the buy_mode to set
	 */
	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
	}
}
