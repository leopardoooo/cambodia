/**
 * CCustDeviceChange.java	2010/08/13
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;

import com.ycsoft.beans.core.common.PropertyChange;
import com.ycsoft.daos.config.POJO;


/**
 * CCustDeviceChange -> C_CUST_DEVICE_CHANGE mapping
 */
@POJO(
	tn="C_CUST_DEVICE_CHANGE",
	sn="",
	pk="")
public class CCustDeviceChange extends PropertyChange implements Serializable {

	// CCustDeviceChange all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1968651777303386057L;
	private String device_id;

	/**
	 * default empty constructor
	 */
	public CCustDeviceChange() {}

	/**
	 * @return the device_id
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * @param deviceId the device_id to set
	 */
	public void setDevice_id(String deviceId) {
		device_id = deviceId;
	}


}