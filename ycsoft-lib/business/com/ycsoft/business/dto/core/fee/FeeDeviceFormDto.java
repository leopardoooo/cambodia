package com.ycsoft.business.dto.core.fee;

import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.commons.pojo.DTO;

/**
 * 封装设备费用信息Form
 */
public class FeeDeviceFormDto implements DTO<CFeeDevice>{

	private String device_type ;
	private String device_id ;
	private String pair_card_id;
	private String fee_id ;
	private Integer should_pay;
	private Integer real_pay;

	public CFeeDevice transform() {
		CFeeDevice t = new CFeeDevice();
		t.setDevice_id(device_id);
		t.setDevice_type(device_type);
		t.setFee_id(fee_id);
		t.setShould_pay(should_pay);
		t.setReal_pay(real_pay);

		return t;
	}

	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public Integer getShould_pay() {
		return should_pay;
	}
	public void setShould_pay(Integer should_pay) {
		this.should_pay = should_pay;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}

	public String getPair_card_id() {
		return pair_card_id;
	}

	public void setPair_card_id(String pair_card_id) {
		this.pair_card_id = pair_card_id;
	}


}
