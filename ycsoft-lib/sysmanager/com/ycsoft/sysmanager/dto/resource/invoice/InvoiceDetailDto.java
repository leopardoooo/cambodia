package com.ycsoft.sysmanager.dto.resource.invoice;

import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class InvoiceDetailDto {
	private String cust_name;
	private String cust_no;
	private String busi_name;
	private String fee_name;
	private Integer real_pay;
	private Date create_time;
	private String status;
	private String optr_id;
	
	private String status_text;
	private String optr_name;
	
	
	
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String custName) {
		cust_name = custName;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String custNo) {
		cust_no = custNo;
	}
	public String getBusi_name() {
		return busi_name;
	}
	public void setBusi_name(String busiName) {
		busi_name = busiName;
	}
	public String getFee_name() {
		return fee_name;
	}
	public void setFee_name(String feeName) {
		fee_name = feeName;
	}
	public Integer getReal_pay() {
		return real_pay;
	}
	public void setReal_pay(Integer realPay) {
		real_pay = realPay;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}
	public String getStatus_text() {
		return status_text;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	public String getOptr_name() {
		return optr_name;
	}
	
}
