/**
 * CFeeDevice.java	2010/07/30
 */

package com.ycsoft.beans.core.fee;

import java.io.Serializable;

import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CFeeDevice -> C_FEE_DEVICE mapping
 */
@POJO(tn = "C_FEE_DEVICE", sn = "SEQ_FEE_SN", pk = "FEE_SN")
public class CFeeDevice extends CFee implements Serializable {

	// CFeeDevice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7443421094179165615L;
	private String device_type;
	private String device_id;
	private String pair_card_id;
	private String pair_modem_id;
	private String pair_modem_code;

	private String device_code;
	private String pair_card_code;
	private String fee_std_id;
	private String buy_mode;
	private Integer buy_num;
	private String device_model;
	private String device_model_text;
	

	/**
	 * @return the buy_mode
	 */
	public String getBuy_mode() {
		return buy_mode;
	}

	/**
	 * @param buy_mode the buy_mode to set
	 */
	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
	}

	/**
	 * default empty constructor
	 */
	public CFeeDevice() {
	}

	// device_type getter and setter
	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	// device_id getter and setter
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	// pair_card_id getter and setter
	public String getPair_card_id() {
		return pair_card_id;
	}

	public void setPair_card_id(String pair_card_id) {
		this.pair_card_id = pair_card_id;
	}

	/**
	 * @return the device_code
	 */
	public String getDevice_code() {
		return device_code;
	}

	/**
	 * @param device_code the device_code to set
	 */
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	/**
	 * @return the pair_card_code
	 */
	public String getPair_card_code() {
		return pair_card_code;
	}

	/**
	 * @param pair_card_code the pair_card_code to set
	 */
	public void setPair_card_code(String pair_card_code) {
		this.pair_card_code = pair_card_code;
	}

	public String getFee_std_id() {
		return fee_std_id;
	}

	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}

	public String getPair_modem_id() {
		return pair_modem_id;
	}

	public void setPair_modem_id(String pair_modem_id) {
		this.pair_modem_id = pair_modem_id;
	}

	public String getPair_modem_code() {
		return pair_modem_code;
	}

	public void setPair_modem_code(String pair_modem_code) {
		this.pair_modem_code = pair_modem_code;
	}

	public Integer getBuy_num() {
		return buy_num;
	}

	public void setBuy_num(Integer buy_num) {
		this.buy_num = buy_num;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public void setDevice_model_text(String device_model_text) {
		this.device_model_text = device_model_text;
	}

	public String getDevice_model_text() {
		String deviceType = getDevice_type();
		if(!"STB".equals(deviceType) && !"CARD".equals(deviceType) && !"MODEM".equals(deviceType)){
			deviceType = "CTL";
		}
		device_model_text=MemoryDict.getDictName(deviceType+"_MODEL", device_model);
		return device_model_text;
	}

}