/**
 * JUserStop.java	2010/06/08
 */

package com.ycsoft.beans.core.job;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * JUserStop -> J_USER_STOP mapping
 */
@POJO(tn = "J_USER_STOP", sn = "", pk = "")
public class JUserStop extends BusiBase implements Serializable {

	// JUserStop all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 3084087561219878145L;
	private Integer job_id;
	private String user_id;
	private Date stop_date;

	/**
	 * default empty constructor
	 */
	public JUserStop() {
	}

	// job_id getter and setter
	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	// stop_date getter and setter
	public Date getStop_date() {
		return stop_date;
	}

	public void setStop_date(Date stop_date) {
		this.stop_date = stop_date;
	}

}