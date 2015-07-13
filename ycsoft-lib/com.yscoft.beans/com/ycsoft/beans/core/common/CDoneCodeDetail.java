/**
 * CDoneCodeDetail.java	2010/03/16
 */

package com.ycsoft.beans.core.common;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CDoneCodeDetail -> C_DONE_CODE_DETAIL mapping
 */
@POJO(tn = "C_DONE_CODE_DETAIL", sn = "", pk = "")
public class CDoneCodeDetail extends BusiBase implements Serializable {

	// CDoneCodeDetail all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2882177591599841021L;
	private String cust_id;
	private String user_id;

	/**
	 * default empty constructor
	 */
	public CDoneCodeDetail() {
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}