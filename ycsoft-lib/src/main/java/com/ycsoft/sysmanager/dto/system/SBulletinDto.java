/**
 * TBulletin.java	2010/11/26
 */

package com.ycsoft.sysmanager.dto.system;

import java.util.Date;

import com.ycsoft.beans.system.SBulletin;

/**
 * SBulletin -> S_BULLETIN mapping
 */
public class SBulletinDto extends SBulletin {

	// TBulletin all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -6511110446932005045L;

	private Date check_date;

	/**
	 * @return the check_date
	 */
	public Date getCheck_date() {
		return check_date;
	}

	/**
	 * @param check_date
	 *            the check_date to set
	 */
	public void setCheck_date(Date check_date) {
		this.check_date = check_date;
	}

	/**
	 * default empty constructor
	 */

}