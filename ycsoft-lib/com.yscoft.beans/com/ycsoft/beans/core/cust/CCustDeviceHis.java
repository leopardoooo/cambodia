/**
 * CCustDeviceHis.java	2011/04/14
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO;

/**
 * CCustDeviceHis -> C_CUST_DEVICE_HIS mapping
 */
@POJO(tn = "C_CUST_DEVICE_HIS", sn = "", pk = "")
public class CCustDeviceHis extends CCustDevice implements Serializable {

	// CCustDeviceHis all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -3576623494816607578L;
	private Integer buy_done_code;
	private String is_reclaim;

	/**
	 * default empty constructor
	 */
	public CCustDeviceHis() {
	}

	public Integer getBuy_done_code() {
		return buy_done_code;
	}

	public void setBuy_done_code(Integer buy_done_code) {
		this.buy_done_code = buy_done_code;
	}

	public String getIs_reclaim() {
		return is_reclaim;
	}

	public void setIs_reclaim(String is_reclaim) {
		this.is_reclaim = is_reclaim;
	}

}