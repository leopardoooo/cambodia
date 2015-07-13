/**
 * CProdNextTariff.java	2010/07/13
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * CProdNextTariff -> C_PROD_NEXT_TARIFF mapping
 */
@POJO(tn = "C_PROD_NEXT_TARIFF", sn = "", pk = "prod_sn")
public class CProdNextTariff implements Serializable {

	// CProdNextTariff all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7582143903643174162L;
	private String prod_sn;
	private String tariff_id;
	private Date eff_date;
	private String area_id;
	private String county_id;

	/**
	 * default empty constructor
	 */
	public CProdNextTariff() {
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

	// area_id getter and setter
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	// county_id getter and setter
	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

}