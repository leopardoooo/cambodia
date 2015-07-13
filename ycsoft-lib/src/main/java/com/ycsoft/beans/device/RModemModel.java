/**
 * RModemModel.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RModemModel -> R_MODEM_MODEL mapping
 */
@POJO(tn = "R_MODEM_MODEL", sn = "", pk = "DEVICE_MODEL")
public class RModemModel extends RDeviceModel implements Serializable {

	// RModemModel all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2502691683075497815L;
	private String net_type;
	
	private String net_type_name;


	public String getNet_type_name() {
		return net_type_name;
	}

	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		net_type_name = MemoryDict.getDictName(DictKey.USER_NET_TYPE, net_type);
		this.net_type = net_type;
	}

	/**
	 * @param net_type_name the net_type_name to set
	 */
	public void setNet_type_name(String net_type_name) {
		this.net_type_name = net_type_name;
	}


}