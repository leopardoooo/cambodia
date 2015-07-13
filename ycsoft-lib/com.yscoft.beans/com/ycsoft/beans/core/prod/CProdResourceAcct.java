/**
 * CProdResourceAcct.java	2010/07/13
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CProdResourceAcct -> C_PROD_RESOURCE_ACCT mapping
 */
@POJO(tn = "C_PROD_RESOURCE_ACCT", sn = "", pk = "")
public class CProdResourceAcct extends BusiBase implements Serializable {

	// CProdResourceAcct all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 859213931418174431L;
	private String prod_sn;
	private String free_type;
	private Date begin_date;
	private Date end_date;
	private String free_unit;
	private Integer amount;
	private Integer use_amount;

	/**
	 * default empty constructor
	 */
	public CProdResourceAcct() {
	}

	// sn getter and setter
	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

	// free_type getter and setter
	public String getFree_type() {
		return free_type;
	}

	public void setFree_type(String free_type) {
		this.free_type = free_type;
	}

	// begin_date getter and setter
	public Date getBegin_date() {
		return begin_date;
	}

	public void setBegin_date(Date begin_date) {
		this.begin_date = begin_date;
	}

	// end_date getter and setter
	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	// free_unit getter and setter
	public String getFree_unit() {
		return free_unit;
	}

	public void setFree_unit(String free_unit) {
		this.free_unit = free_unit;
	}

	// amount getter and setter
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	// use_amount getter and setter
	public Integer getUse_amount() {
		return use_amount;
	}

	public void setUse_amount(Integer use_amount) {
		this.use_amount = use_amount;
	}

}