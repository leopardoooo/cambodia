/**
 * CCustUnitToResidentHis.java	2010/10/19
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * CCustUnitToResidentHis -> C_CUST_UNIT_TO_RESIDENT_HIS mapping
 */
@POJO(
	tn="C_CUST_UNIT_TO_RESIDENT_HIS",
	sn="",
	pk="")
public class CCustUnitToResidentHis extends CCustUnitToResident implements Serializable {

	// CCustUnitToResidentHis all properties


	/**
	 *
	 */
	private static final long serialVersionUID = 5219452295285656780L;
	private Integer done_code ;

	/**
	 * default empty constructor
	 */
	public CCustUnitToResidentHis() {}

	/**
	 * @return the done_code
	 */
	public Integer getDone_code() {
		return done_code;
	}

	/**
	 * @param done_code the done_code to set
	 */
	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}




}