package com.ycsoft.beans.device;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class RDeviceModel {
	private String device_model;
	private String device_type;
	private String model_name;
	private String supplier_id;
	private String remark;
	private String modem_type;
	private String is_virtual;
	private String is_virtual_text;

	private String modem_type_name;
	private String supplier_name;
	private String device_type_text;
	private String device_model_text;

	/**
	 * @return the device_type_text
	 */
	public String getDevice_type_text() {
		return device_type_text;
	}

	/**
	 * @return the device_model
	 */
	public String getDevice_model() {
		return device_model;
	}

	/**
	 * @param device_model
	 *            the device_model to set
	 */
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id
	 *            the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the device_type
	 */
	public String getDevice_type() {
		return device_type;
	}

	/**
	 * @param device_type the device_type to set
	 */
	public void setDevice_type(String device_type) {
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE,
				device_type);
		this.device_type = device_type;
	}

	/**
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getDevice_model_text() {
		device_model_text=MemoryDict.getDictName(getDevice_type()+"_MODEL", device_model);
		return device_model_text;
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

	public String getIs_virtual() {
		return is_virtual;
	}

	public void setIs_virtual(String is_virtual) {
		this.is_virtual = is_virtual;
		this.is_virtual_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_virtual);
	}

	public String getIs_virtual_text() {
		return is_virtual_text;
	}

}
