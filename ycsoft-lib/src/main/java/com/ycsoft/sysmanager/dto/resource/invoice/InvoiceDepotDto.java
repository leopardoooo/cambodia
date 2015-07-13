package com.ycsoft.sysmanager.dto.resource.invoice;

import java.util.Date;

public class InvoiceDepotDto {

	private String optr_type;
	private String optr_name;
	private String depot_name;
	private Date create_time;
	
	
	public String getOptr_type() {
		return optr_type;
	}
	public void setOptr_type(String optrType) {
		optr_type = optrType;
	}
	public String getOptr_name() {
		return optr_name;
	}
	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}
	public String getDepot_name() {
		return depot_name;
	}
	public void setDepot_name(String depotName) {
		depot_name = depotName;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
}
