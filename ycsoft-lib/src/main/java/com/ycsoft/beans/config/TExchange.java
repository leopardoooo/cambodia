package com.ycsoft.beans.config;

import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;
@POJO(tn = "T_EXCHANGE",pk="EXCHANGE_ID",sn="SEQ_JOB_ID")
public class TExchange extends BusiBase{
	private Date eff_date;
	private String status;
	private Integer exchange;
	
	private String status_text;
	
	private String exchange_id;
	
	
	public String getStatus_text() {
		return status_text;
	}
	public String getExchange_id() {
		return exchange_id;
	}
	public void setExchange_id(String exchange_id) {
		this.exchange_id = exchange_id;
	}
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
		status_text=MemoryDict.getDictName(DictKey.STATUS, status);
		this.status = status;
	}
	public Integer getExchange() {
		return exchange;
	}
	public void setExchange(Integer exchange) {
		this.exchange = exchange;
	}
	
}
