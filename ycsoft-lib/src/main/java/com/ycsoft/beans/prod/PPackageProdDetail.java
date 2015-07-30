package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "P_PACKAGE_PROD", sn = "", pk = "PACKAGE_PROD_CONTENT_ID")
public class PPackageProdDetail  implements Serializable {
	private String package_id;
	private String package_group_id;
	private String package_prod_content_id;
	private String package_prod_content_name;
	private Integer prod_select_num;
	private String prod_list;
	private Integer precent;
	public String getPackage_id() {
		return package_id;
	}
	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}
	public String getPackage_group_id() {
		return package_group_id;
	}
	public void setPackage_group_id(String package_group_id) {
		this.package_group_id = package_group_id;
	}
	public String getPackage_prod_content_id() {
		return package_prod_content_id;
	}
	public void setPackage_prod_content_id(String package_prod_content_id) {
		this.package_prod_content_id = package_prod_content_id;
	}
	public String getPackage_prod_content_name() {
		return package_prod_content_name;
	}
	public void setPackage_prod_content_name(String package_prod_content_name) {
		this.package_prod_content_name = package_prod_content_name;
	}
	public Integer getProd_select_num() {
		return prod_select_num;
	}
	public void setProd_select_num(Integer prod_select_num) {
		this.prod_select_num = prod_select_num;
	}
	public String getProd_list() {
		return prod_list;
	}
	public void setProd_list(String prod_list) {
		this.prod_list = prod_list;
	}
	public Integer getPrecent() {
		return precent;
	}
	public void setPrecent(Integer precent) {
		this.precent = precent;
	}
	
}
