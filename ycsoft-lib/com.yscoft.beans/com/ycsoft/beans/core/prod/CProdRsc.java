/**
 * CProdRsc.java	2010/06/25
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO;

/**
 * CProdRsc -> C_PROD_RSC mapping
 */
@POJO(tn = "C_PROD_RSC", sn = "", pk = "PROD_SN")
public class CProdRsc extends CountyBase implements Serializable {

	// CProdRsc all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5624507487653084576L;
	private String prod_sn;
	private String res_id;

	/**
	 * default empty constructor
	 */
	public CProdRsc() {
	}

	// prod_sn getter and setter
	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

}