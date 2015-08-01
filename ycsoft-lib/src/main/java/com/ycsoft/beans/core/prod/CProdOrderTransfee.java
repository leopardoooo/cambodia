package com.ycsoft.beans.core.prod;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

@POJO(tn = "C_PROD_ORDER_TRANSFEE")
public class CProdOrderTransfee extends BusiBase implements Serializable  {
	
//	  cust_id         VARCHAR2(16),
	private String cust_id;
//	  order_sn        varchar2(16),
	private String order_sn;
//	  FROM_CUST_ID     VARCHAR2(16),
	private String from_cust_id;
//	  from_order_sn VARCHAR2(16),
	private String from_order_sn;
//	  FEE_TYPE         VARCHAR2(8),
	private String fee_type;
//	  BALANCE          NUMBER(12),
	private Integer balance;
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getFrom_cust_id() {
		return from_cust_id;
	}
	public void setFrom_cust_id(String from_cust_id) {
		this.from_cust_id = from_cust_id;
	}
	public String getFrom_order_sn() {
		return from_order_sn;
	}
	public void setFrom_order_sn(String from_order_sn) {
		this.from_order_sn = from_order_sn;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}

}
