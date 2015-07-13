/**
 * RDeviceOrder.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * RDeviceOrder -> R_DEVICE_ORDER mapping
 */
@POJO(tn = "R_DEVICE_ORDER", sn = "", pk = "DEVICE_DONE_CODE")
public class RDeviceOrder extends RDeviceOrderDetail implements Serializable {

	// RDeviceOrder all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2066300989039823858L;
	private Integer device_done_code;
	private String order_no;
	private String depot_id;
	private String confirm_id;
	private String supplier_id;
	private Date supply_date;
	private Date create_time;
	private String optr_id;
	private String is_history;
	private String remark;

	private String supplier_name;
	@SuppressWarnings("unused")
	private String order_info;
	/**
	 * default empty constructor
	 */
	public RDeviceOrder() {
	}

	// device_done_code getter and setter
	public Integer getDevice_done_code() {
		return device_done_code;
	}

	public void setDevice_done_code(Integer device_done_code) {
		this.device_done_code = device_done_code;
	}

	// order_no getter and setter
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	// depot_id getter and setter
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}

	// confirm_id getter and setter
	public String getConfirm_id() {
		return confirm_id;
	}

	public void setConfirm_id(String confirm_id) {
		this.confirm_id = confirm_id;
	}

	// supplier_id getter and setter
	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	// supply_date getter and setter
	public Date getSupply_date() {
		return supply_date;
	}

	public void setSupply_date(Date supply_date) {
		this.supply_date = supply_date;
	}

	// create_time getter and setter
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	// optr_id getter and setter
	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name
	 *            the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getOrder_info() {
		return supplier_name+ order_no;
	}

	public String getIs_history() {
		return is_history;
	}

	public void setIs_history(String is_history) {
		this.is_history = is_history;
	}

}