package com.ycsoft.sysmanager.dto.depot;

import com.ycsoft.beans.device.RDeviceTransfer;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.store.MemoryDict;

public class RDeviceTransferDto extends RDeviceTransfer {
	/**
	 *
	 */
	private static final long serialVersionUID = -8349198402118606589L;
	private String tran_type;
	private String tran_type_text;
	@SuppressWarnings("unused")
	private String depot_id;
	@SuppressWarnings("unused")
	private String depot_text;
	
	private String device_code;
	private String device_model;
	private String device_type;
	private String device_type_text;
	private String device_model_text;
	private Integer count;

	/**
	 * @return the tran_type
	 */
	public String getTran_type() {
		return tran_type;
	}

	/**
	 * @param tran_type
	 *            the tran_type to set
	 */
	public void setTran_type(String tran_type) {
		tran_type_text = MemoryDict.getDictName(DictKey.DEVICE_TRAN_TYPE,
				tran_type);
		this.tran_type = tran_type;
	}

	/**
	 * @return the tran_type_text
	 */
	public String getTran_type_text() {
		return tran_type_text;
	}

	public String getDepot_id() {
		if (tran_type.equals(SystemConstants.DEVICE_TRANS_TYPE_TRANIN))
			return getDepot_source();
		else
			return getDepot_order();
	}

	public String getDepot_text() {
		if (tran_type.equals(SystemConstants.DEVICE_TRANS_TYPE_TRANIN))
			return getDepot_source_text();
		else
			return getDepot_order_text();
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	
	public String getDevice_model_text() {
		String deviceType = getDevice_type();
		if(!"STB".equals(deviceType) && !"CARD".equals(deviceType) && !"MODEM".equals(deviceType)){
			deviceType = "CTL";
		}
		return MemoryDict.getDictName(deviceType+"_MODEL", getDevice_model())+"("+getDevice_model()+")";
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
		device_type_text = MemoryDict.getDictName(DictKey.ALL_DEVICE_TYPE, device_type);
	}

	public String getDevice_type_text() {
		return device_type_text;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
