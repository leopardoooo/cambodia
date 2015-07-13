package com.ycsoft.business.dto.core.fee;

import java.io.Serializable;

public class FeeInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5819981286180070067L;
	private String fee_id;
	private String fee_std_id;
	private Integer fee;
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public String getFee_std_id() {
		return fee_std_id;
	}
	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
}
