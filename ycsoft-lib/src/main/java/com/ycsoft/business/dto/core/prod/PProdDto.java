package com.ycsoft.business.dto.core.prod;

import java.util.Date;
import com.ycsoft.beans.prod.PProd;

public class PProdDto extends PProd {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9129495312072552475L;
	private String tariff_id;
	private String tariff_name;
	private String tariff_desc;
	private Integer billing_cycle;
	private String billing_type;
	private Integer rent;
	private String month_rent_cal_type;
	private String day_rent_cal_type;
	private String use_fee_rule;
	private String bill_rule;
	private String pt_status;//资费状态
	private Date create_time;
	private String rule_id;
	private String tariff_type;
	
	private String disct_id;
	private String disct_name;
	private Integer final_rent;
	private Integer disct_rent;
	
	public String getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public String getTariff_desc() {
		return tariff_desc;
	}
	public void setTariff_desc(String tariff_desc) {
		this.tariff_desc = tariff_desc;
	}
	public Integer getBilling_cycle() {
		return billing_cycle;
	}
	public void setBilling_cycle(Integer billing_cycle) {
		this.billing_cycle = billing_cycle;
	}
	public String getBilling_type() {
		return billing_type;
	}
	public void setBilling_type(String billing_type) {
		this.billing_type = billing_type;
	}
	public Integer getRent() {
		return rent;
	}
	public void setRent(Integer rent) {
		this.rent = rent;
	}
	public String getMonth_rent_cal_type() {
		return month_rent_cal_type;
	}
	public void setMonth_rent_cal_type(String month_rent_cal_type) {
		this.month_rent_cal_type = month_rent_cal_type;
	}
	public String getDay_rent_cal_type() {
		return day_rent_cal_type;
	}
	public void setDay_rent_cal_type(String day_rent_cal_type) {
		this.day_rent_cal_type = day_rent_cal_type;
	}
	public String getUse_fee_rule() {
		return use_fee_rule;
	}
	public void setUse_fee_rule(String use_fee_rule) {
		this.use_fee_rule = use_fee_rule;
	}
	public String getBill_rule() {
		return bill_rule;
	}
	public void setBill_rule(String bill_rule) {
		this.bill_rule = bill_rule;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getRule_id() {
		return rule_id;
	}
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}
	public String getTariff_type() {
		return tariff_type;
	}
	public void setTariff_type(String tariff_type) {
		this.tariff_type = tariff_type;
	}
	public String getPt_status() {
		return pt_status;
	}
	public void setPt_status(String pt_status) {
		this.pt_status = pt_status;
	}
	public String getDisct_id() {
		return disct_id;
	}
	public void setDisct_id(String disct_id) {
		this.disct_id = disct_id;
	}
	public String getDisct_name() {
		return disct_name;
	}
	public void setDisct_name(String disct_name) {
		this.disct_name = disct_name;
	}
	public Integer getFinal_rent() {
		return final_rent;
	}
	public void setFinal_rent(Integer final_rent) {
		this.final_rent = final_rent;
	}
	public Integer getDisct_rent() {
		return disct_rent;
	}
	public void setDisct_rent(Integer disct_rent) {
		this.disct_rent = disct_rent;
	}
}
