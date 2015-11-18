package com.ycsoft.beans.ott;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(
		tn="T_SERVER_OTTAUTH_PROD",
		sn="",
		pk="ID")
public class TServerOttauthProd extends TServerOttauthFee implements Serializable {
	
	private String status;
	private String domain;// 
	private String need_sync;//NEED_SYNC
	private Date sync_date;
	
	private String fee_id;
	private String fee_name;

	
	public String getFee_id() {
		return fee_id;
	}
	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}
	public String getFee_name() {
		return fee_name;
	}
	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domian) {
		this.domain = domian;
	}
	public String getNeed_sync() {
		return need_sync;
	}
	public void setNeed_sync(String need_sync) {
		this.need_sync = need_sync;
	}
	public Date getSync_date() {
		return sync_date;
	}
	public void setSync_date(Date sync_date) {
		this.sync_date = sync_date;
	}
	
}
