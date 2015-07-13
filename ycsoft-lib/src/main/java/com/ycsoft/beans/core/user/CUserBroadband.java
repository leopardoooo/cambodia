/**
 * CUserBroadband.java	2010/03/17
 */

package com.ycsoft.beans.core.user;

import java.io.Serializable;

import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.daos.config.POJO;

/**
 * CUserBroadband -> C_USER_BROADBAND mapping
 */
@POJO(tn = "C_USER_BROADBAND", sn = "", pk = "USER_ID")
public class CUserBroadband extends UserDto implements Serializable {
	// CUserBroadband all properties
	private static final long serialVersionUID = -7399652523712036656L;
	

	/**
	 * default empty constructor
	 */
	public CUserBroadband() {
	}

	
}