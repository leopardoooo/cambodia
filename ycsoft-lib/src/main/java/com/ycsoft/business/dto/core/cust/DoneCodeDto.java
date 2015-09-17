/**
 *
 */
package com.ycsoft.business.dto.core.cust;

import java.util.Date;

import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;


public class DoneCodeDto extends CDoneCode{

	/**
	 *
	 */
	private static final long serialVersionUID = -5504975813284107548L;
	private String attribute_id;
	private String attribute_name;
	private String attribute_value;
	private String input_type;
	private String param_name;
	
	private String cancel;
	private String ignore;
	private String busi_fee;
	
	private String cancel_text;
	private String ignore_text;
	private String done_num;
	private String active_num;
	private String invalid_num;
	private Date last_done_date;
	private Integer real_pay;
	private String fee_id;
	private String attr_remark;
	private Integer reverse_done_code;
	private String user_id;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getActive_num() {
		return active_num;
	}
	public void setActive_num(String active_num) {
		this.active_num = active_num;
	}
	public String getInvalid_num() {
		return invalid_num;
	}
	public void setInvalid_num(String invalid_num) {
		this.invalid_num = invalid_num;
	}
	public Date getLast_done_date() {
		return last_done_date;
	}
	public void setLast_done_date(Date last_done_date) {
		this.last_done_date = last_done_date;
	}
	public String getDone_num() {
		return done_num;
	}
	public void setDone_num(String done_num) {
		this.done_num = done_num;
	}
	public String getAttribute_name() {
		return attribute_name;
	}
	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}
	public String getAttribute_value() {
		return attribute_value;
	}
	public String getInput_type() {
		return input_type;
	}
	public void setInput_type(String input_type) {
		this.input_type = input_type;
	}
	public void setAttribute_value(String attribute_value) {
		this.attribute_value = attribute_value;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getAttribute_id() {
		return attribute_id;
	}
	public void setAttribute_id(String attribute_id) {
		this.attribute_id = attribute_id;
	}
	
	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
		this.cancel_text = MemoryDict.getDictName(DictKey.BOOLEAN, cancel);
	}
	public String getIgnore() {
		return ignore;
	}
	public void setIgnore(String ignore) {
		this.ignore = ignore;
		this.ignore_text = MemoryDict.getDictName(DictKey.BOOLEAN, ignore);
	}
	public String getCancel_text() {
		return cancel_text;
	}
	public String getIgnore_text() {
		return ignore_text;
	}
	public String getBusi_fee() {
		return busi_fee;
	}
	public void setBusi_fee(String busi_fee) {
		this.busi_fee = busi_fee;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public String getAttr_remark() {
		return attr_remark;
	}
	public void setAttr_remark(String attr_remark) {
		this.attr_remark = attr_remark;
	}
	public Integer getReverse_done_code() {
		return reverse_done_code;
	}
	public void setReverse_done_code(Integer reverse_done_code) {
		this.reverse_done_code = reverse_done_code;
	}
	
}
