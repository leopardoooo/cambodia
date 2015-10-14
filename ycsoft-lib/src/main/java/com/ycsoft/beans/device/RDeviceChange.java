/**
 * RDeviceChange.java	2010/06/23
 */

package com.ycsoft.beans.device;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * RDeviceChange -> R_DEVICE_CHANGE mapping
 */
@POJO(tn = "R_DEVICE_CHANGE", sn = "", pk = "")
public class RDeviceChange extends BusiBase implements Serializable {

	// RDeviceChange all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6257653242883781174L;
	private String device_id;
	private String column_name;
	private String old_value;
	private String new_value;
	private Date change_date;
	private String pair_card_id;
	private String pair_modem_id;
	private String buy_mode;

	/**
	 * default empty constructor
	 */
	public RDeviceChange() {
	}

	
	
	public String getBuy_mode() {
		return buy_mode;
	}

	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
	}

	// device_id getter and setter
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	/**
	 * @return the column_name
	 */
	public String getColumn_name() {
		return column_name;
	}

	/**
	 * @param column_name
	 *            the column_name to set
	 */
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	// old_value getter and setter
	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}

	// new_value getter and setter
	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	// change_date getter and setter
	public Date getChange_date() {
		return change_date;
	}

	public void setChange_date(Date change_date) {
		this.change_date = change_date;
	}

	public String getPair_card_id() {
		return pair_card_id;
	}

	public void setPair_card_id(String pair_card_id) {
		this.pair_card_id = pair_card_id;
	}

	public String getPair_modem_id() {
		return pair_modem_id;
	}

	public void setPair_modem_id(String pair_modem_id) {
		this.pair_modem_id = pair_modem_id;
	}

}