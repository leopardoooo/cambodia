package com.ycsoft.beans.core.prod;

import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "C_PROD_ORDER_HIS", pk = "ORDER_SN")
public class CProdOrderHis  extends CProdOrder{
	private Integer delete_done_code;
	private Date delete_time;
	
	public Date getDelete_time() {
		return delete_time;
	}

	public void setDelete_time(Date delete_time) {
		this.delete_time = delete_time;
	}

	public Integer getDelete_done_code() {
		return delete_done_code;
	}

	public void setDelete_done_code(Integer delete_done_code) {
		this.delete_done_code = delete_done_code;
	}
}
