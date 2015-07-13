/**
 * TProdStatusRent.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TProdStatusRent -> T_PROD_STATUS_RENT mapping
 */
@POJO(tn = "T_PROD_STATUS_RENT", sn = "", pk = "")
public class TProdStatusRent implements Serializable {

	// TProdStatusRent all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1194625475989821147L;
	private String template_id;
	private String status_id;
	private Integer is_cal_rent;

	private String status_desc;
	private String is_cal_rent_text;

	public String getStatus_desc() {
		return status_desc;
	}

	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}

	public String getIs_cal_rent_text() {
		return is_cal_rent_text;
	}

	public void setIs_cal_rent_text(String is_cal_rent_text) {
		this.is_cal_rent_text = is_cal_rent_text;
	}

	/**
	 * default empty constructor
	 */
	public TProdStatusRent() {
	}

	// template_id getter and setter
	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	// status_id getter and setter
	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}

	// is_cal_rent getter and setter
	public Integer getIs_cal_rent() {
		return is_cal_rent;
	}

	public void setIs_cal_rent(Integer is_cal_rent) {
		this.is_cal_rent = is_cal_rent;
		if(this.is_cal_rent ==1){
			is_cal_rent_text = "是";
		}else{
			is_cal_rent_text = "否";
		}
	}

}