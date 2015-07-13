package com.ycsoft.beans.core.user;

import com.ycsoft.business.dto.core.user.UserDto;

public class CUserStb extends UserDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6119387855545147370L;
	private Integer buy_fee;
	private String buy_mode;
	private String stb_definition_type;
	private String change_reason;
	private String buy_time;
	
	public Integer getBuy_fee() {
		return buy_fee;
	}
	public void setBuy_fee(Integer buy_fee) {
		this.buy_fee = buy_fee;
	}
	public String getBuy_mode() {
		return buy_mode;
	}
	public void setBuy_mode(String buy_mode) {
		this.buy_mode = buy_mode;
	}
	/**
	 * @return the stb_definition_type
	 */
	public String getStb_definition_type() {
		return stb_definition_type;
	}
	/**
	 * @param stb_definition_type the stb_definition_type to set
	 */
	public void setStb_definition_type(String stb_definition_type) {
		this.stb_definition_type = stb_definition_type;
	}
	/**
	 * @return the change_reason
	 */
	public String getChange_reason() {
		return change_reason;
	}
	/**
	 * @param change_reason the change_reason to set
	 */
	public void setChange_reason(String change_reason) {
		this.change_reason = change_reason;
	}
	/**
	 * @return the buy_time
	 */
	public String getBuy_time() {
		return buy_time;
	}
	/**
	 * @param buy_time the buy_time to set
	 */
	public void setBuy_time(String buy_time) {
		this.buy_time = buy_time;
	}
	
	
}
