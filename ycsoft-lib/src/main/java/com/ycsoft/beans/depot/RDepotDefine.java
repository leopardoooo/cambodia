/**
 * RDepotDefine.java	2010/06/11
 */

package com.ycsoft.beans.depot;

import java.io.Serializable;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "R_DEPOT_DEFINE", sn = "SEQ_DEPOT_ID", pk = "DEPOT_ID")
public class RDepotDefine extends CountyBase implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8317469331489607883L;
	private String depot_id;
	private String depot_name;
	private String depot_pid;
	private String status;
	private String remark;

	/**
	 * default empty constructor
	 */
	public RDepotDefine() {
	}

	// depot_id getter and setter
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}

	// depot_name getter and setter
	public String getDepot_name() {
		return depot_name;
	}

	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;

	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDepot_pid() {
		return depot_pid;
	}

	public void setDepot_pid(String depot_pid) {
		this.depot_pid = depot_pid;
	}

}