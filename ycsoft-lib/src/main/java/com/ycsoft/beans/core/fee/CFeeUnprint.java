package com.ycsoft.beans.core.fee;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn="C_FEE_UNPRINT",sn="",pk="fee_sn")
public class CFeeUnprint implements Serializable {

	private String fee_sn;
	private Integer create_done_code;
	private String cust_id;
	private String optr_id;
	public String getFee_sn() {
		return fee_sn;
	}
	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}
	public Integer getCreate_done_code() {
		return create_done_code;
	}
	public void setCreate_done_code(Integer create_done_code) {
		this.create_done_code = create_done_code;
	}
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	
	
	
}
