/**
 * PProdTariff.java	2010/06/22
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PProdTariff -> P_PROD_TARIFF mapping
 */
@POJO(tn = "P_PROD_TARIFF", sn = "SEQ_TARIFF_ID", pk = "TARIFF_ID")
public class PProdTariff extends OptrBase implements Serializable {

	// PProdTariff all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8442687161650615939L;
	private String tariff_id;
	private String tariff_name;
	private String tariff_desc;
	private String prod_id;
	private Integer billing_cycle;
	private String billing_type;
	private Integer rent;
	private String month_rent_cal_type;
	private String day_rent_cal_type;
	private String use_fee_rule;
	private String bill_rule;
	private String status;
	private Date create_time;
	private String rule_id;
	private String tariff_type;
	private String spkg_sn;
	private Date eff_date;
	private Date exp_date;

	private String billing_type_text;
	private String status_text;
	private String rule_id_text;
	private String month_rent_cal_type_text;
	private String day_rent_cal_type_text;
	private String use_fee_rule_text;
	private String bill_rule_text;
	private String tariff_type_text;

	
	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	public String getRule_id_text() {
		return rule_id_text;
	}

	public String getSpkg_sn() {
		return spkg_sn;
	}

	public void setSpkg_sn(String spkg_sn) {
		this.spkg_sn = spkg_sn;
	}

	public void setRule_id_text(String rule_id_text) {
		this.rule_id_text = rule_id_text;
	}

	public String getMonth_rent_cal_type_text() {
		return month_rent_cal_type_text;
	}

	public void setMonth_rent_cal_type_text(String month_rent_cal_type_text) {
		this.month_rent_cal_type_text = month_rent_cal_type_text;
	}

	public String getDay_rent_cal_type_text() {
		return day_rent_cal_type_text;
	}

	public void setDay_rent_cal_type_text(String day_rent_cal_type_text) {
		this.day_rent_cal_type_text = day_rent_cal_type_text;
	}

	public String getUse_fee_rule_text() {
		return use_fee_rule_text;
	}

	public void setUse_fee_rule_text(String use_fee_rule_text) {
		this.use_fee_rule_text = use_fee_rule_text;
	}

	public String getBill_rule_text() {
		return bill_rule_text;
	}

	public void setBill_rule_text(String bill_rule_text) {
		this.bill_rule_text = bill_rule_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public void setBilling_type_text(String billing_type_text) {
		this.billing_type_text = billing_type_text;
	}

	/**
	 * default empty constructor
	 */
	public PProdTariff() {
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// tariff_name getter and setter
	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	// tariff_desc getter and setter
	public String getTariff_desc() {
		return tariff_desc;
	}

	public void setTariff_desc(String tariff_desc) {
		this.tariff_desc = tariff_desc;
	}

	// prod_id getter and setter
	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	// billing_cycle getter and setter
	public Integer getBilling_cycle() {
		return billing_cycle;
	}

	public void setBilling_cycle(Integer billing_cycle) {
		this.billing_cycle = billing_cycle;
	}

	// billing_type getter and setter
	public String getBilling_type() {
		return billing_type;
	}

	public void setBilling_type(String billing_type) {
		this.billing_type = billing_type;
		billing_type_text = MemoryDict
				.getDictName(DictKey.BILLING_TYPE, billing_type);
	}

	// rent getter and setter
	public Integer getRent() {
		return rent;
	}

	public void setRent(Integer rent) {
		this.rent = rent;
	}

	// month_rent_cal_type getter and setter
	public String getMonth_rent_cal_type() {
		return month_rent_cal_type;
	}

	public void setMonth_rent_cal_type(String month_rent_cal_type) {
		this.month_rent_cal_type = month_rent_cal_type;
	}

	// day_rent_cal_type getter and setter
	public String getDay_rent_cal_type() {
		return day_rent_cal_type;
	}

	public void setDay_rent_cal_type(String day_rent_cal_type) {
		this.day_rent_cal_type = day_rent_cal_type;
	}

	public String getBill_rule() {
		return bill_rule;
	}

	public void setBill_rule(String bill_rule) {
		this.bill_rule = bill_rule;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	// create_time getter and setter
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getBilling_type_text() {
		return billing_type_text;
	}

	/**
	 * @return the rule_id
	 */
	public String getRule_id() {
		return rule_id;
	}

	/**
	 * @param rule_id
	 *            the rule_id to set
	 */
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	public String getUse_fee_rule() {
		return use_fee_rule;
	}

	public void setUse_fee_rule(String use_fee_rule) {
		this.use_fee_rule = use_fee_rule;
	}

	public String getTariff_type() {
		return tariff_type;
	}

	public void setTariff_type(String tariff_type) {
		this.tariff_type = tariff_type;
		this.tariff_type_text = MemoryDict.getDictName(DictKey.TARIFF_TYPE, tariff_type);
	}

	public String getTariff_type_text() {
		return tariff_type_text;
	}

	public void setTariff_type_text(String tariff_type_text) {
		this.tariff_type_text = tariff_type_text;
	}
	
	
}