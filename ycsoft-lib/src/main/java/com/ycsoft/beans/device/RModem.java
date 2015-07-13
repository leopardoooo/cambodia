/**
 * RModem.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RModem -> R_MODEM mapping
 */
@POJO(tn = "R_MODEM", sn = "", pk = "DEVICE_ID")
public class RModem extends RDevice implements Serializable {

	// RModem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -9119961448849868034L;
	private String modem_mac;
	private String modem_id;
	private String modem_type;
	
	private String modem_type_name;

	/**
	 * default empty constructor
	 */
	public RModem() {
	}

	// modem_mac getter and setter
	public String getModem_mac() {
		return modem_mac;
	}

	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}

	// modem_id getter and setter
	public String getModem_id() {
		return modem_id;
	}

	public void setModem_id(String modem_id) {
		this.modem_id = modem_id;
	}

	public String getModem_type() {
		return modem_type;
	}

	public void setModem_type(String modemType) {
		modem_type_name = MemoryDict.getDictName(DictKey.MODEM_TYPE, modemType);
		modem_type = modemType;
	}

	public String getModem_type_name() {
		return modem_type_name;
	}

	public void setModem_type_name(String modemTypeName) {
		modem_type_name = modemTypeName;
	}

}