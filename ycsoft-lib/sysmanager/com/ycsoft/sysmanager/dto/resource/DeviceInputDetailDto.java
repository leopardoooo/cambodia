package com.ycsoft.sysmanager.dto.resource;

import java.util.Date;

import com.ycsoft.beans.device.RDeviceDoneDetail;

public class DeviceInputDetailDto extends RDeviceDoneDetail {
	/**
	 *
	 */
	private static final long serialVersionUID = -8821077987650450662L;
	private Date create_time;

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
