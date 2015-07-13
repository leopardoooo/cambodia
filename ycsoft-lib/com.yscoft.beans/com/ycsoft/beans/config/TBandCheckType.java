/**
 * TBandCheckType.java	2010/06/30
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TBandCheckType -> T_BAND_CHECK_TYPE mapping
 */
@POJO(tn = "T_BAND_CHECK_TYPE", sn = "", pk = "")
public class TBandCheckType implements Serializable {

	// TBandCheckType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7179605912453252689L;
	private String check_type;
	private String check_type_name;
	private String is_need_login_info;

	/**
	 * default empty constructor
	 */
	public TBandCheckType() {
	}

	// check_type getter and setter
	public String getCheck_type() {
		return check_type;
	}

	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}

	// check_type_name getter and setter
	public String getCheck_type_name() {
		return check_type_name;
	}

	public void setCheck_type_name(String check_type_name) {
		this.check_type_name = check_type_name;
	}

	// is_need_login_info getter and setter
	public String getIs_need_login_info() {
		return is_need_login_info;
	}

	public void setIs_need_login_info(String is_need_login_info) {
		this.is_need_login_info = is_need_login_info;
	}

}