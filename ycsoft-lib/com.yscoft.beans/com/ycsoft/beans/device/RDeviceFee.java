/**
 * RDeviceFee.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * RDeviceFee -> R_DEVICE_FEE mapping
 */
@POJO(tn = "R_DEVICE_FEE", sn = "", pk = "")
public class RDeviceFee extends RDeviceModel implements Serializable {

	// RDeviceFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -548836757537836765L;
	private String buy_mode;
	private String fee_id;
	private Integer fee_value;
	private Integer min_fee_value;
	private Integer max_fee_value;
	private String fee_std_id;

	private String fee_name;
	private String buy_mode_text;

	/**
	 * @return the fee_value
	 */
	public Integer getFee_value() {
		return fee_value;
	}

	/**
	 * @param fee_value the fee_value to set
	 */
	public void setFee_value(Integer fee_value) {
		this.fee_value = fee_value;
	}

	/**
	 * default empty constructor
	 */
	public RDeviceFee() {
	}


	// buy_mode getter and setter
	public String getBuy_mode() {
		return buy_mode;
	}

	public void setBuy_mode(String buy_mode) {
		buy_mode_text = MemoryDict.getDictName(DictKey.DEVICE_BUY_MODE,
				buy_mode);
		this.buy_mode = buy_mode;
	}

	// fee_id getter and setter
	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}

	/**
	 * @return the fee_name
	 */
	public String getFee_name() {
		return fee_name;
	}

	/**
	 * @param fee_name the fee_name to set
	 */
	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}

	/**
	 * @return the buy_mode_text
	 */
	public String getBuy_mode_text() {
		return buy_mode_text;
	}

	public String getFee_std_id() {
		return fee_std_id;
	}

	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}

	public Integer getMin_fee_value() {
		return min_fee_value;
	}

	public void setMin_fee_value(Integer min_fee_value) {
		this.min_fee_value = min_fee_value;
	}

	public Integer getMax_fee_value() {
		return max_fee_value;
	}

	public void setMax_fee_value(Integer max_fee_value) {
		this.max_fee_value = max_fee_value;
	}


}