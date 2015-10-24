/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.task.WTaskUser;

/**
 * @author YC-SOFT
 *
 */
public class TaskUserDto extends WTaskUser{

	/**
	 *
	 */
	private String device_code;	

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}
	
}
