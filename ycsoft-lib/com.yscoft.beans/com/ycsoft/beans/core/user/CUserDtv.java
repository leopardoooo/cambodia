/**
 * CUserDtv.java	2010/03/17
 */

package com.ycsoft.beans.core.user;

import java.io.Serializable;

import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.daos.config.POJO;

/**
 * CUserDtv -> C_USER_DTV mapping
 */
@POJO(tn = "C_USER_DTV", sn = "", pk = "USER_ID")
public class CUserDtv extends UserDto implements Serializable {

	// CUserDtv all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4424263292735761770L;
	

	/**
	 * default empty constructor
	 */
	public CUserDtv() {
	}


}