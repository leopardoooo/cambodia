/**
 * CCustDevice.java	2010/05/05
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CCustDevice -> C_CUST_DEVICE mapping
 */
@POJO(tn = "C_CUST_DEVICE", sn = "", pk = "DEVICE_ID")
public class CCustDevice extends CountyBase implements Serializable {

	// CCustDevice all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1312808391549703008L;
	private String cust_id;
	private String device_type;
	private String device_id;
	private String device_code;
	private String pair_card_id;
	private String pair_card_code;
	private String pair_modem_id;
	private String pair_modem_code;
	private String buy_mode;
	private Date buy_time;
	private String status;
	private Date status_date;
	private String loss_reg;
	private Integer done_code;
	private Date replacover_date ;
	private String change_reason;

	private String device_type_text;
	private String buy_mode_text;
	private String status_text;
	private String loss_reg_text;
	private String change_reason_text;

	/**
	 * default empty constructor
	 */
	public CCustDevice() {
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// device_type getter and setter
	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
		device_type_text = MemoryDict.getDictName(DictKey.DEVICE_TYPE, device_type);
	}

	// device_id getter and setter
	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	// buy_mode getter and setter
	public String getBuy_mode() {
		return buy_mode;
	}

	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
		buy_mode_text = MemoryDict.getDictName(DictKey.DEVICE_BUY_MODE, buy_mode);
	}

	// buy_time getter and setter
	public Date getBuy_time() {
		return buy_time;
	}

	public void setBuy_time(Date buy_time) {
		this.buy_time = buy_time;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	// status_date getter and setter
	public Date getStatus_date() {
		return status_date;
	}

	public void setStatus_date(Date status_date) {
		this.status_date = status_date;
	}
	public String getDevice_type_text() {
		return device_type_text;
	}

	public void setDevice_type_text(String device_type_text) {
		this.device_type_text = device_type_text;
	}

	public String getBuy_mode_text() {
		return buy_mode_text;
	}

	public void setBuy_mode_text(String buy_mode_text) {
		this.buy_mode_text = buy_mode_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getPair_card_id() {
		return pair_card_id;
	}

	public void setPair_card_id(String pair_card_id) {
		this.pair_card_id = pair_card_id;
	}

	public String getPair_card_code() {
		return pair_card_code;
	}

	public void setPair_card_code(String pair_card_code) {
		this.pair_card_code = pair_card_code;
	}

	public String getLoss_reg() {
		return loss_reg;
	}

	public void setLoss_reg(String loss_reg) {
		loss_reg_text = MemoryDict.getDictName(DictKey.BOOLEAN, loss_reg);
		this.loss_reg = loss_reg;
	}

	public String getLoss_reg_text() {
		return loss_reg_text;
	}

	public Integer getDone_code() {
		return done_code;
	}

	public void setDone_code(Integer doneCode) {
		done_code = doneCode;
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

	public Date getReplacover_date() {
		return replacover_date;
	}

	public void setReplacover_date(Date replacover_date) {
		this.replacover_date = replacover_date;
	}

	public String getChange_reason() {
		return change_reason;
	}

	public void setChange_reason(String change_reason) {
		this.change_reason = change_reason;
		change_reason_text = MemoryDict.getDictName(DictKey.CHANGE_REASON, change_reason);
	}

	/**
	 * @return the change_reason_text
	 */
	public String getChange_reason_text() {
		return change_reason_text;
	}
	
}