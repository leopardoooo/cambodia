package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TBusiFee;

public class TBusiFeeDto extends TBusiFee {
	/**
	 *
	 */
	private static final long serialVersionUID = -5592904264718110518L;
	private String busi_code;
	private String busi_name;
	private String buy_mode;
	private String buy_mode_name;
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
	public String getBusi_name() {
		return busi_name;
	}
	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}
	public String getBuy_mode() {
		return buy_mode;
	}
	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
	}
	public String getBuy_mode_name() {
		return buy_mode_name;
	}
	public void setBuy_mode_name(String buy_mode_name) {
		this.buy_mode_name = buy_mode_name;
	}
}
