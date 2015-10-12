/**
 * PPackageProd.java	2010/07/05
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;
import com.ycsoft.daos.config.POJO;

/**
 * PPackageProd -> P_PACKAGE_PROD mapping
 */
@POJO(tn = "P_PACKAGE_PROD", sn = "", pk = "PACKAGE_GROUP_ID")
public class PPackageProd implements Serializable {

	// PPackageProd all properties

	/**
	 * 
	 */ 
	private static final long serialVersionUID = -2407441487593995420L;
	private String package_id;
	private String package_group_id;
	private String package_group_name;
	private String user_type;
	private String terminal_type;
	private Integer max_user_cnt;
	private String prod_list;
	private Integer precent;
	
	private String prod_type;
	


	public String getProd_type() {
		return prod_type;
	}



	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
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



	/**
	 * default empty constructor
	 */
	public PPackageProd() {
	}



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



	public String getPackage_group_name() {
		return package_group_name;
	}



	public void setPackage_group_name(String package_group_name) {
		this.package_group_name = package_group_name;
	}



	public String getUser_type() {
		return user_type;
	}



	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}



	public String getTerminal_type() {
		return terminal_type;
	}



	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}



	public Integer getMax_user_cnt() {
		return max_user_cnt;
	}



	public void setMax_user_cnt(Integer max_user_cnt) {
		this.max_user_cnt = max_user_cnt;
	}

}