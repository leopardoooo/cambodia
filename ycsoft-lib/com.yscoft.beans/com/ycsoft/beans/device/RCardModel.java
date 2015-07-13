/**
 * RCardModel.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RCardModel -> R_CARD_MODEL mapping
 */
@POJO(tn = "R_CARD_MODEL", sn = "", pk = "DEVICE_MODEL")
public class RCardModel extends RDeviceModel implements Serializable {

	// RCardModel all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6332524022406627527L;
	private String ca_type;

	/**
	 * default empty constructor
	 */
	public RCardModel() {
	}

	// ca_type getter and setter
	public String getCa_type() {
		return ca_type;
	}

	public void setCa_type(String ca_type) {
		this.ca_type = ca_type;
	}

}