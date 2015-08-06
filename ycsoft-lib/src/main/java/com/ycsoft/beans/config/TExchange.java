package com.ycsoft.beans.config;

import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;
@POJO(tn = "T_EXCHANGE")
public class TExchange extends BusiBase{
	private Date eff_date;
	private String status;
	private Integer exchange;
	public Date getEff_date() {
		return eff_date;
	}
	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getExchange() {
		return exchange;
	}
	public void setExchange(Integer exchange) {
		this.exchange = exchange;
	}
	
}
