/**
 * RDeviceInput.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RDeviceInput -> R_DEVICE_INPUT mapping
 */
@POJO(tn = "R_DEVICE_INPUT", sn = "", pk = "")
public class RDeviceInput extends RDeviceDoneDetail implements Serializable {

	// RDeviceInput all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8907362449108591336L;
	private Integer device_done_code;
	private String input_no;
	private String depot_id;
	private Integer order_done_code;
	private String supplier_id;
	private String backup;
	private String ownership;
	private Date create_time;
	private String optr_id;
	private String remark;
	
	private String is_new_stb;
	private String order_no;
	private String supplier_name;
	private String depot_name;
	private String optr_name;
	private String ownership_text;

	/**
	 * default empty constructor
	 */
	public RDeviceInput() {
	}

	public String getIs_new_stb() {
		return is_new_stb;
	}

	public void setIs_new_stb(String isNewStb) {
		is_new_stb = isNewStb;
	}

	// device_done_code getter and setter
	public Integer getDevice_done_code() {
		return device_done_code;
	}

	public void setDevice_done_code(Integer device_done_code) {
		this.device_done_code = device_done_code;
	}

	// input_no getter and setter
	public String getInput_no() {
		return input_no;
	}

	public void setInput_no(String input_no) {
		this.input_no = input_no;
	}

	// depot_id getter and setter
	public String getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(String depot_id) {
		depot_name = MemoryDict.getDictName(DictKey.DEPOT, depot_id);
		this.depot_id = depot_id;
	}

	/**
	 * @return the order_done_code
	 */
	public Integer getOrder_done_code() {
		return order_done_code;
	}

	/**
	 * @param order_done_code the order_done_code to set
	 */
	public void setOrder_done_code(Integer order_done_code) {
		this.order_done_code = order_done_code;
	}

	// supplier_id getter and setter
	public String getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the backup
	 */
	public String getBackup() {
		return backup;
	}

	/**
	 * @param backup
	 *            the backup to set
	 */
	public void setBackup(String backup) {
		this.backup = backup;
	}

	// ownership getter and setter
	public String getOwnership() {
		return ownership;
	}

	public void setOwnership(String ownership) {
		ownership_text = MemoryDict.getDictName("DEVICE_OWNERSHIP", ownership);
		this.ownership = ownership;
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
		optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
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
	 * @return the depot_name
	 */
	public String getDepot_name() {
		return depot_name;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}

	/**
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getOwnership_text() {
		return ownership_text;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

}