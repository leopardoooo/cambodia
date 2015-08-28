package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.beans.device.RDeviceModel;

/**
 * 
 */
public class RDeviceModelTotalDto extends RDeviceModel{
	private Integer total_num;
	private Integer fee_value;
	private String fee_std_id;
	private String fee_id;
	
	
	

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

	public Integer getFee_value() {
		return fee_value;
	}

	public void setFee_value(Integer fee_value) {
		this.fee_value = fee_value;
	}

	public Integer getTotal_num() {
		return total_num;
	}

	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}
	
	
}
