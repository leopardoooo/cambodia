/**
 * TBandNetType.java	2010/06/30
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TNetType -> T_NET_TYPE mapping
 */
@POJO(tn = "T_NET_TYPE", sn = "", pk = "NET_TYPE")
public class TNetType implements Serializable {

	// TBandNetType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5676193793111976874L;
	private String net_type;
	private String net_type_name;
	private String need_device;
	private String user_type;

	/**
	 * @return the user_type
	 */
	public String getUser_type() {
		return user_type;
	}

	/**
	 * @param user_type
	 *            the user_type to set
	 */
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	/**
	 * default empty constructor
	 */
	public TNetType() {
	}

	// net_type getter and setter
	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	// net_type_name getter and setter
	public String getNet_type_name() {
		return net_type_name;
	}

	public void setNet_type_name(String net_type_name) {
		this.net_type_name = net_type_name;
	}

	/**
	 * @return the need_device
	 */
	public String getNeed_device() {
		return need_device;
	}

	/**
	 * @param need_device the need_device to set
	 */
	public void setNeed_device(String need_device) {
		this.need_device = need_device;
	}

}