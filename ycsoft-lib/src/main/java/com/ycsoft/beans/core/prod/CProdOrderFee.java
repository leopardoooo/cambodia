package com.ycsoft.beans.core.prod;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "C_PROD_ORDER_FEE", sn = "SEQ_ORDER_PAY_SN", pk = "ORDER_FEE_SN")
public class CProdOrderFee extends BusiBase implements Serializable {
	private String order_fee_sn;
	private String order_sn;
	private String from_type;
	private String from_sn;
	private String fee_type;
	private Integer fee;
	private Integer writeoff_fee;
	private Integer output_fee;
	public String getOrder_fee_sn() {
		return order_fee_sn;
	}
	
	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public void setOrder_fee_sn(String order_fee_sn) {
		this.order_fee_sn = order_fee_sn;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getFrom_type() {
		return from_type;
	}
	public void setFrom_type(String from_type) {
		this.from_type = from_type;
	}
	public String getFrom_sn() {
		return from_sn;
	}
	public void setFrom_sn(String from_sn) {
		this.from_sn = from_sn;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public Integer getWriteoff_fee() {
		return writeoff_fee;
	}
	public void setWriteoff_fee(Integer writeoff_fee) {
		this.writeoff_fee = writeoff_fee;
	}
	public Integer getOutput_fee() {
		return output_fee;
	}
	public void setOutput_fee(Integer output_fee) {
		this.output_fee = output_fee;
	}
	
}
