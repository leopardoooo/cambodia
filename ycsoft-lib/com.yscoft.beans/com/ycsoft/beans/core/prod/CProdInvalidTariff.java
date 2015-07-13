/**
 * CProdInvalidTariff.java	2010/07/13
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * CProdInvalidTariff -> C_PROD_INVALID_TARIFF mapping
 */
@POJO(tn = "C_PROD_INVALID_TARIFF", sn = "", pk = "")
public class CProdInvalidTariff implements Serializable {

	// CProdInvalidTariff all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6009567463775707206L;
	private String prod_sn;
	private String tariff_id;
	private String new_tariff_id;
	private Date eff_date;
	private Date exp_date;
	private String county_id;
	private String area_id;

	private String tariff_name;
	private String old_tariff_name;
	private String new_tariff_name;

	/**
	 * default empty constructor
	 */
	public CProdInvalidTariff() {
	}

	// sn getter and setter
	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// eff_date getter and setter
	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}

	// exp_date getter and setter
	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}

	// county_id getter and setter
	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	// area_id getter and setter
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	/**
	 * @return the tariff_name
	 */
	public String getTariff_name() {
		return tariff_name;
	}

	/**
	 * @param tariff_name
	 *            the tariff_name to set
	 */
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	public String getNew_tariff_id() {
		return new_tariff_id;
	}

	public void setNew_tariff_id(String new_tariff_id) {
		this.new_tariff_id = new_tariff_id;
	}

	public String getOld_tariff_name() {
		return old_tariff_name;
	}

	public void setOld_tariff_name(String old_tariff_name) {
		this.old_tariff_name = old_tariff_name;
	}

	public String getNew_tariff_name() {
		return new_tariff_name;
	}

	public void setNew_tariff_name(String new_tariff_name) {
		this.new_tariff_name = new_tariff_name;
	}

}