/**
 * CCustUnitToResident.java	2010/03/23
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * CCustUnitToResident -> C_CUST_UNIT_TO_RESIDENT mapping
 */
@POJO(tn = "C_CUST_UNIT_TO_RESIDENT", sn = "", pk = "")
public class CCustUnitToResident implements Serializable {

	// CCustUnitToResident all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5402297778222455001L;
	private String unit_cust_id;
	private String resident_cust_id;
	private Date create_time;

	/**
	 * default empty constructor
	 */
	public CCustUnitToResident() {
	}

	// unit_cust_id getter and setter
	public String getUnit_cust_id() {
		return unit_cust_id;
	}

	public void setUnit_cust_id(String unit_cust_id) {
		this.unit_cust_id = unit_cust_id;
	}

	// resident_cust_id getter and setter
	public String getResident_cust_id() {
		return resident_cust_id;
	}

	public void setResident_cust_id(String resident_cust_id) {
		this.resident_cust_id = resident_cust_id;
	}

	// create_time getter and setter
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}