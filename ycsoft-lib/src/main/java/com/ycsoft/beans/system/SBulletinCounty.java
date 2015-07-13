package com.ycsoft.beans.system;

/**
 * SBulletinCounty.java	2010/10/09
 */

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SBulletinCounty -> S_BULLETIN_COUNTY mapping
 */
@POJO(tn = "S_BULLETIN_COUNTY", sn = "", pk = "")
public class SBulletinCounty implements Serializable {

	// PPromotion all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -2921991635623784340L;
	private String bulletin_id;
	private String county_id;
	private String dept_id;



	public String getBulletin_id() {
		return bulletin_id;
	}

	public void setBulletin_id(String bulletin_id) {
		this.bulletin_id = bulletin_id;
	}

	/**
	 * @return the county_id
	 */
	public String getCounty_id() {
		return county_id;
	}

	/**
	 * @param county_id
	 *            the county_id to set
	 */
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
}