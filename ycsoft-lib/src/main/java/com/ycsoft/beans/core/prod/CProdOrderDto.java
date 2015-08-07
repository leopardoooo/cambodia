package com.ycsoft.beans.core.prod;

import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class CProdOrderDto extends CProdOrder {
	private String tariff_name;//display(4)
	private String disct_name;
	private String prod_type;
	private String prod_name; //display(1)
	private String prod_type_text;//display(3)
	private String serv_id;
	private String serv_id_text;
	private String is_base;
	private String is_base_text;
	private String public_acctitem_type_text;
	private String package_name;//display(2)
	private String user_name;
	//nvl(折扣周期月数，资费周期月数)
	private Integer billing_cycle;
	//用户协议期限
	private Date protocol_date;
	
	public Integer getBilling_cycle() {
		return billing_cycle;
	}
	public void setBilling_cycle(Integer billing_cycle) {
		this.billing_cycle = billing_cycle;
	}
	public Date getProtocol_date() {
		return protocol_date;
	}
	public void setProtocol_date(Date protocol_date) {
		this.protocol_date = protocol_date;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getProd_type() {
		return prod_type;
	}
	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
		this.prod_type_text = MemoryDict.getDictName(DictKey.PROD_TYPE, this.prod_type);
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public String getDisct_name() {
		return disct_name;
	}
	public void setDisct_name(String disct_name) {
		this.disct_name = disct_name;
	}
	
	public String getProd_type_text() {
		return prod_type_text;
	}
	
	public String getServ_id() {
		return serv_id;
	}
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
		this.serv_id_text = MemoryDict.getDictName(DictKey.SERV_ID, this.serv_id);
	}
	public String getServ_id_text() {
		return serv_id_text;
	}
	public String getIs_base() {
		return is_base;
	}
	public void setIs_base(String is_base) {
		this.is_base = is_base;
		this.is_base_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_base);
	}
	public String getIs_base_text() {
		return is_base_text;
	}
	public String getPublic_acctitem_type_text() {
		return public_acctitem_type_text;
	}
	@Override
	public void setPublic_acctitem_type(String public_acctitem_type) {
		// TODO Auto-generated method stub
		super.setPublic_acctitem_type(public_acctitem_type);
		this.public_acctitem_type_text = MemoryDict.getDictName(DictKey.PUBLIC_ACCTITEM_TYPE, public_acctitem_type);
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	
}
