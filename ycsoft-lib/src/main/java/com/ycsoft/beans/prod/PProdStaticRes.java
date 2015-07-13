/**
 * PProdStaticRes.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PProdStaticRes -> P_PROD_STATIC_RES mapping
 */
@POJO(tn = "P_PROD_STATIC_RES", sn = "", pk = "")
public class PProdStaticRes implements Serializable {

	// PProdStaticRes all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2584812404474201656L;
	private String prod_id;
	private String res_id;

	private PRes pRes;
	/**
	 * default empty constructor
	 */
	public PProdStaticRes() {
	}
	public PProdStaticRes(String prodId,String resId) {
		prod_id = prodId;
		res_id = resId;
	}

	// prod_id getter and setter
	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	public PRes getPRes() {
		return pRes;
	}

	public void setPRes(PRes res) {
		pRes = res;
	}

}