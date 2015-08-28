package com.ycsoft.business.dto.core.fee;

import com.ycsoft.beans.config.TBusiFeeStd;

public class BusiFeeDto extends TBusiFeeStd {
	/**
	 *
	 */
	private static final long serialVersionUID = 6478104904172114757L;
	private String keyname;
	private String busi_code;
	private String county_id;

	private String busi_name;
	private Integer buy_num;
	private String addr_id;
	
	private Integer fee_count;//费用倍数，IP加挂方案使用
	
	public Integer getFee_count() {
		return fee_count;
	}

	public void setFee_count(Integer fee_count) {
		this.fee_count = fee_count;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	/**
	 * @return the busi_name
	 */
	public String getBusi_name() {
		return busi_name;
	}

	/**
	 * @param busi_name the busi_name to set
	 */
	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busiCode) {
		busi_code = busiCode;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String countyId) {
		county_id = countyId;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	public Integer getBuy_num() {
		return buy_num;
	}

	public void setBuy_num(Integer buy_num) {
		this.buy_num = buy_num;
	}
}
