package com.ycsoft.business.dto.core.fee;

import java.util.Date;

public class BbillingcycleCfgDto {
	private String billing_cycle_id;
	private Date begin_date;
	private Date end_date;
	private String status;
	
	public String getBilling_cycle_id() {
		return billing_cycle_id;
	}
	public void setBilling_cycle_id(String billing_cycle_id) {
		this.billing_cycle_id = billing_cycle_id;
	}
	public Date getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
