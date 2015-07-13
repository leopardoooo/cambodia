/**
 * CCustHis.java	2010/07/21
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CCustHis -> C_CUST_HIS mapping
 */
@POJO(tn = "C_CUST_HIS", sn = "", pk = "CUST_ID")
public class CCustHis extends CCust implements Serializable {

	// CCustHis all properties

	private String done_date;
	/**
	 *
	 */
	private static final long serialVersionUID = -1560358761424929767L;
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