/**
 * RDeviceOrderDetail.java	2010/09/06
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RDeviceOrderDetail -> R_DEVICE_ORDER_DETAIL mapping
 */
@POJO(tn = "R_DEVICE_ORDER_DETAIL", sn = "", pk = "")
public class RDeviceOrderDetail implements Serializable {

	// RDeviceOrderDetail all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2393177023750978948L;
	private Integer device_done_code;
	private String device_type;
	private String device_model;
	private Integer price;
	private Integer order_num;
	private Integer supply_num;

	private String device_type_text;
	@SuppressWarnings("unused")
	private String device_model_text;

	/**
	 * default empty constructor
	 */
	public RDeviceOrderDetail() {
	}

	// device_done_code getter and setter
	public Integer getDevice_done_code() {
		return device_done_code;
	}

	public void setDevice_done_code(Integer device_done_code) {
		this.device_done_code = device_done_code;
	}

	// device_type getter and setter
	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE, device_type);
		this.device_type = device_type;
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

	// price getter and setter
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	// order_num getter and setter
	public Integer getOrder_num() {
		return order_num;
	}

	public void setOrder_num(Integer order_num) {
		this.order_num = order_num;
	}

	// supply_num getter and setter
	public Integer getSupply_num() {
		return supply_num;
	}

	public void setSupply_num(Integer supply_num) {
		this.supply_num = supply_num;
	}

	public String getDevice_type_text() {
		return device_type_text;
	}

	public String getDevice_model_text() {
		return MemoryDict.getDictName(getDevice_type()+"_MODEL", getDevice_model())+"("+getDevice_model()+")";
	}

}