/**
 * CUserHis.java	2010/06/07
 */

package com.ycsoft.beans.core.user;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CUserHis -> C_USER_HIS mapping
 */
@POJO(tn = "C_USER_HIS", sn = "", pk = "USER_ID")
public class CUserHis extends CUser implements Serializable {
	// CUserHis all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5854596844628631834L;

	private String need_check;
	private String done_date;
	
	
	public String getNeed_check() {
		return need_check;
	}


	public void setNeed_check(String need_check) {
		this.need_check = need_check;
	}


	/**
	 * default empty constructor
	 */
	public CUserHis() {
	}


	/**
	 * @return the done_date
	 */
	public String getDone_date() {
		return done_date;
	}


	/**
	 * @param done_date the done_date to set
	 */
	public void setDone_date(String done_date) {
		this.done_date = done_date;
	}

}