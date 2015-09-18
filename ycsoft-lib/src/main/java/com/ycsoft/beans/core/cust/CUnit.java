/**
 * CUnit.java	2015/09/18
 */
package com.ycsoft.beans.core.cust;

import java.io.Serializable;
import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CUnit -> C_UNIT mapping
 */
@POJO(tn = "C_UNIT", sn = "SEQ_UNIT_ID", pk = "UNIT_ID")
public class CUnit extends BusiBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6117136064677475802L;
	private String unit_id;
	private String unit_name;
	private String contact_name;
	private String contact_tel;
	private String cerd_num;
	private String address;
	private String remark;
	
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_tel() {
		return contact_tel;
	}
	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}
	public String getCerd_num() {
		return cerd_num;
	}
	public void setCerd_num(String cerd_num) {
		this.cerd_num = cerd_num;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}