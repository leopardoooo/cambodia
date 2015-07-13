package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;

import com.ycsoft.beans.core.promotion.CPromotion;
import com.ycsoft.beans.prod.PPromotion;

public class CPromotionDto extends CPromotion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7072927682676702678L;
	
	private String is_necessary;	//促销是否有可选节目
	
	private Integer total_acct_fee ;
	private Integer repetition_times;
	private Integer total_acct_count ;

	public String getIs_necessary() {
		return is_necessary;
	}

	public void setIs_necessary(String is_necessary) {
		this.is_necessary = is_necessary;
	}

	public Integer getTotal_acct_fee() {
		return total_acct_fee;
	}

	public void setTotal_acct_fee(Integer total_acct_fee) {
		this.total_acct_fee = total_acct_fee;
	}

	public Integer getRepetition_times() {
		return repetition_times;
	}

	public void setRepetition_times(Integer repetition_times) {
		this.repetition_times = repetition_times;
	}

	public Integer getTotal_acct_count() {
		return total_acct_count;
	}

	public void setTotal_acct_count(Integer total_acct_count) {
		this.total_acct_count = total_acct_count;
	}

}
