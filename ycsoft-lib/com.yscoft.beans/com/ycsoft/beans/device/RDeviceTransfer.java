/**
 * RDeviceTransfer.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RDeviceTransfer -> R_DEVICE_TRANSFER mapping
 */
@POJO(tn = "R_DEVICE_TRANSFER", sn = "", pk = "DEVICE_DONE_CODE")
public class RDeviceTransfer implements Serializable {

	// RDeviceTransfer all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7578472901113615477L;
	private Integer device_done_code;
	private String transfer_no;
	private String depot_source;
	private String depot_order;
	private String status;
	private String optr_id;
	private Date create_time;
	private String confirm_optr_id;
	private Date confirm_date;
	private String confirm_info;
	private String remark;
	private String is_history;
	private String transfer_status;

	private String transfer_status_text;
	private String depot_source_text;
	private String depot_order_text;
	private String status_text;
	private String optr_name;
	private String confirm_optr_name;
	/**
	 * @return the depot_source_text
	 */
	public String getDepot_source_text() {
		return depot_source_text;
	}

	/**
	 * @return the depot_order_text
	 */
	public String getDepot_order_text() {
		return depot_order_text;
	}

	/**
	 * @return the status_text
	 */
	public String getStatus_text() {
		return status_text;
	}

	/**
	 * default empty constructor
	 */
	public RDeviceTransfer() {
	}

	// device_done_code getter and setter
	public Integer getDevice_done_code() {
		return device_done_code;
	}

	public void setDevice_done_code(Integer device_done_code) {
		this.device_done_code = device_done_code;
	}

	// transfer_no getter and setter
	public String getTransfer_no() {
		return transfer_no;
	}

	public void setTransfer_no(String transfer_no) {
		this.transfer_no = transfer_no;
	}

	// depot_source getter and setter
	public String getDepot_source() {
		return depot_source;
	}

	public void setDepot_source(String depot_source) {
		depot_source_text = MemoryDict.getDictName(DictKey.DEPOT, depot_source);
		this.depot_source = depot_source;
	}

	// depot_order getter and setter
	public String getDepot_order() {
		return depot_order;
	}

	public void setDepot_order(String depot_order) {
		depot_order_text = MemoryDict.getDictName(DictKey.DEPOT, depot_order);
		this.depot_order = depot_order;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
		this.status = status;
	}

	// optr_id getter and setter
	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
		this.optr_id = optr_id;
	}

	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	// confirm_optr_id getter and setter
	public String getConfirm_optr_id() {
		return confirm_optr_id;
	}

	public void setConfirm_optr_id(String confirm_optr_id) {
		confirm_optr_name = MemoryDict.getDictName(DictKey.OPTR, confirm_optr_id);
		this.confirm_optr_id = confirm_optr_id;
	}

	// confirm_date getter and setter
	public Date getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}

	// confirm_info getter and setter
	public String getConfirm_info() {
		return confirm_info;
	}

	public void setConfirm_info(String confirm_info) {
		this.confirm_info = confirm_info;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}

	/**
	 * @return the confirm_optr_name
	 */
	public String getConfirm_optr_name() {
		return confirm_optr_name;
	}

	public String getIs_history() {
		return is_history;
	}

	public void setIs_history(String is_history) {
		this.is_history = is_history;
	}

	public String getTransfer_status() {
		return transfer_status;
	}

	public void setTransfer_status(String transfer_status) {
		this.transfer_status = transfer_status;
		transfer_status_text = MemoryDict.getDictName(DictKey.TRANSFER_STATUS, transfer_status);
	}

	public void setTransfer_status_text(String transfer_status_text) {
		this.transfer_status_text = transfer_status_text;
	}

	public String getTransfer_status_text() {
		return transfer_status_text;
	}

}