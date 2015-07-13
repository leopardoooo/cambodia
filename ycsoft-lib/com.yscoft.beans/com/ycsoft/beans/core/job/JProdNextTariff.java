/**
 * JProdNextTariff.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JProdNextTariff -> J_PROD_NEXT_TARIFF mapping
 */
@POJO(tn = "J_PROD_NEXT_TARIFF", sn = "", pk = "job_id")
public class JProdNextTariff extends BusiBase implements Serializable {

	// JProdNextTariff all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1979816489288004293L;
	private Integer job_id;
	private String prod_sn;
	private String tariff_id;
	private String old_tariff_id;
	private Date eff_date;

	/**
	 * default empty constructor
	 */
	public JProdNextTariff() {
	}

	// job_id getter and setter
	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

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

	public String getOld_tariff_id() {
		return old_tariff_id;
	}

	public void setOld_tariff_id(String old_tariff_id) {
		this.old_tariff_id = old_tariff_id;
	}


}