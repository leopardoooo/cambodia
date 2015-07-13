/**
 * TDeviceBuyMode.java	2010/06/24
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TDeviceBuyMode -> T_DEVICE_BUY_MODE mapping
 */
/**
 * @author liujiaqi
 *
 */
/**
 * @author liujiaqi
 *
 */
@POJO(tn = "T_DEVICE_BUY_MODE", sn = "", pk = "BUY_MODE")
public class TDeviceBuyMode implements Serializable {

	// TDeviceBuyMode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4127277066380144327L;
	private String buy_mode;
	private String buy_mode_name;
	private String change_ownship;
	private String buy;
	private String buy_type;

	private String fee_id;
	private String change_ownship_text;
	private String buy_text;
	private String buy_type_text;
	private String buy_mode_text;
	private String fee_name;
	private String ownship_change_mode;
	private String module;
	private String is_fee;

	/**
	 * default empty constructor
	 */
	public TDeviceBuyMode() {
	}

	public String getIs_fee() {
		return is_fee;
	}

	public void setIs_fee(String isFee) {
		is_fee = isFee;
	}

	// buy_mode getter and setter
	public String getBuy_mode() {
		return buy_mode;
	}

	public void setBuy_mode(String buy_mode) {
		buy_mode_text = MemoryDict.getDictName(DictKey.DEVICE_BUY_MODE, buy_mode);
		this.buy_mode = buy_mode;
	}

	// buy_mode_name getter and setter
	public String getBuy_mode_name() {
		return buy_mode_name;
	}

	public void setBuy_mode_name(String buy_mode_name) {
		this.buy_mode_name = buy_mode_name;
	}

	/**
	 * @return the change_ownship
	 */
	public String getChange_ownship() {
		return change_ownship;
	}

	/**
	 * @param change_ownship
	 *            the change_ownship to set
	 */
	public void setChange_ownship(String change_ownship) {
		change_ownship_text = MemoryDict.getDictName(DictKey.BOOLEAN, change_ownship);
		this.change_ownship = change_ownship;
	}

	/**
	 * @return the buy
	 */
	public String getBuy() {
		return buy;
	}

	/**
	 * @param buy
	 *            the buy to set
	 */
	public void setBuy(String buy) {
		buy_text = MemoryDict.getDictName(DictKey.BOOLEAN, buy);
		this.buy = buy;
	}

	/**
	 * @return the buy_type
	 */
	public String getBuy_type() {
		return buy_type;
	}

	/**
	 * @param buy_type
	 *            the buy_type to set
	 */
	public void setBuy_type(String buy_type) {
		buy_type_text = MemoryDict.getDictName(DictKey.DEVICE_BUY_TYPE,
				buy_type);
		this.buy_type = buy_type;
	}

	/**
	 * @return the buy_type_text
	 */
	public String getBuy_type_text() {
		return buy_type_text;
	}

	/**
	 * @return the buy_text
	 */
	public String getBuy_text() {
		return buy_text;
	}

	/**
	 * @return the change_ownship_text
	 */
	public String getChange_ownship_text() {
		return change_ownship_text;
	}

	public String getBuy_mode_text() {
		return buy_mode_text;
	}


	public String getFee_name() {
		return fee_name;
	}

	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}

	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}

	public String getOwnship_change_mode() {
		return ownship_change_mode;
	}

	public void setOwnship_change_mode(String ownship_change_mode) {
		this.ownship_change_mode = ownship_change_mode;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	

}