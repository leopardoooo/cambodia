/**
 * CProdPropChange.java	2010/07/13
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.beans.core.common.PropertyChange;
import com.ycsoft.daos.config.POJO;

/**
 * CProdPropChange -> C_PROD_PROP_CHANGE mapping
 */
@POJO(tn = "C_PROD_PROP_PAT", sn = "", pk = "")
public class CProdPropPat extends PropertyChange implements Serializable {

	// CProdPropChange all properties

	private String prod_sn;
	private String status;
	private Integer cancel_done_code;
	private String cust_id;

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCancel_done_code() {
		return cancel_done_code;
	}

	public void setCancel_done_code(Integer cancel_done_code) {
		this.cancel_done_code = cancel_done_code;
	}

	/**
	 * default empty constructor
	 */
	public CProdPropPat() {
	}

	// sn getter and setter
	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

}