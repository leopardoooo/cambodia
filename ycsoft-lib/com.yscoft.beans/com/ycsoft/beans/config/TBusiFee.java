/**
 * TBusiFee.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TBusiFee -> T_BUSI_FEE mapping
 */
@POJO(tn = "T_BUSI_FEE", sn = "SEQ_FEE_ID", pk = "FEE_ID")
public class TBusiFee extends OptrBase implements Serializable {

	// TBusiFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1735735231763854448L;
	private String fee_id;
	private String fee_name;
	private String fee_type;
	private String printitem_id;
	private String link_user;
	private String can_update;
	private String status;
	private String deposit;
	private String remark;
	private String device_fee_type;
	private String fee_big_type;
	private Date create_time;

	private String device_fee_type_text;
	private String printitem_id_text;
	private String link_user_text;
	private String can_update_text;
	private String status_text;
	private String fee_type_text;
	private String deposit_text;
	private String fee_big_type_text;

	public String getDeposit_text() {
		return deposit_text;
	}

	public void setDeposit_text(String deposit_text) {
		this.deposit_text = deposit_text;
	}

	/**
	 * default empty constructor
	 */
	public TBusiFee() {
	}

	// fee_id getter and setter
	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}

	// fee_name getter and setter
	public String getFee_name() {
		return fee_name;
	}

	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}

	// fee_type getter and setter
	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
		fee_type_text = MemoryDict.getDictName(DictKey.FEE_TYPE, this.fee_type);
	}

	// print_item_id getter and setter
	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
		printitem_id_text = MemoryDict.getDictName(DictKey.PRINTITEM_NAME,
				this.printitem_id);
	}

	// link_user getter and setter
	public String getLink_user() {
		return link_user;
	}

	public void setLink_user(String link_user) {
		this.link_user = link_user;
		link_user_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.link_user);
	}

	// can_update getter and setter
	public String getCan_update() {
		return can_update;
	}

	public void setCan_update(String can_update) {
		this.can_update = can_update;
		can_update_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.can_update);
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPrintitem_id_text() {
		return printitem_id_text;
	}

	public void setPrintitem_id_text(String printitem_id_text) {
		this.printitem_id_text = printitem_id_text;
	}

	public String getLink_user_text() {
		return link_user_text;
	}

	public void setLink_user_text(String link_user_text) {
		this.link_user_text = link_user_text;
	}

	public String getCan_update_text() {
		return can_update_text;
	}

	public void setCan_update_text(String can_update_text) {
		this.can_update_text = can_update_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public String getFee_type_text() {
		return fee_type_text;
	}

	public void setFee_type_text(String fee_type_text) {
		this.fee_type_text = fee_type_text;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
		deposit_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.deposit);
	}

	public String getDevice_fee_type() {
		return device_fee_type;
	}

	public void setDevice_fee_type(String device_fee_type) {
		device_fee_type_text = MemoryDict.getDictName("DEVICE_FEE_TYPE", device_fee_type);
		this.device_fee_type = device_fee_type;
	}

	public String getDevice_fee_type_text() {
		return device_fee_type_text;
	}

	public String getFee_big_type() {
		return fee_big_type;
	}

	public void setFee_big_type(String fee_big_type) {
		this.fee_big_type = fee_big_type;
		this.fee_big_type_text = MemoryDict.getDictName(DictKey.FEE_BIG_TYPE, fee_big_type);
	}

	public String getFee_big_type_text() {
		return fee_big_type_text;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}