/**
 * JBusiCmdProd.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * JBusiCmdProd -> J_BUSI_CMD_PROD mapping
 */
@POJO(tn = "J_BUSI_CMD_PROD", sn = "", pk = "")
public class JBusiCmdProd implements Serializable {

	// JBusiCmdProd all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -811042361311810842L;
	private Integer job_id;
	private String sn;
	private String time_stamp;

	/**
	 * default empty constructor
	 */
	public JBusiCmdProd() {
	}

	// job_id getter and setter
	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	// sn getter and setter
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	// time_stamp getter and setter
	public String getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(String time_stamp) {
		this.time_stamp = time_stamp;
	}

}