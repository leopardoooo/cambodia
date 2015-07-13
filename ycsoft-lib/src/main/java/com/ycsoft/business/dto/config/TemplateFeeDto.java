package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TTemplateColumn;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class TemplateFeeDto extends TTemplateColumn {

	private String template_name;
	private String fee_std_id;
	private String fee_id;
	private String fee_name;
	
	private Integer fee_min_value;
	private Integer fee_max_value;
	private Integer fee_default_value;
	private String device_buy_mode;
	private String device_type;
	
	private String device_buy_mode_text;
	private String device_type_text;
	
	public String getDevice_buy_mode() {
		return device_buy_mode;
	}
	public void setDevice_buy_mode(String device_buy_mode) {
		this.device_buy_mode = device_buy_mode;
		this.device_buy_mode_text = MemoryDict.getDictName(DictKey.BUSI_BUY_MODE, device_buy_mode);
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
		this.device_type_text = MemoryDict.getDictName(DictKey.DEVICE_TYPE, device_type);
	}
	public String getDevice_buy_mode_text() {
		return device_buy_mode_text;
	}
	public String getDevice_type_text() {
		return device_type_text;
	}
	public String getFee_std_id() {
		return fee_std_id;
	}
	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public String getFee_name() {
		return fee_name;
	}
	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public Integer getFee_min_value() {
		return fee_min_value;
	}
	public void setFee_min_value(Integer fee_min_value) {
		this.fee_min_value = fee_min_value;
	}
	public Integer getFee_max_value() {
		return fee_max_value;
	}
	public void setFee_max_value(Integer fee_max_value) {
		this.fee_max_value = fee_max_value;
	}
	public Integer getFee_default_value() {
		return fee_default_value;
	}
	public void setFee_default_value(Integer fee_default_value) {
		this.fee_default_value = fee_default_value;
	}
	
	
}
