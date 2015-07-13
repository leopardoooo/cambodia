/**
 * CUserAtv.java	2010/03/17
 */

package com.ycsoft.beans.core.user;

import java.io.Serializable;

import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.daos.config.POJO;

/**
 * CUserAtv -> C_USER_ATV mapping
 */
@POJO(tn = "C_USER_ATV", sn = "", pk = "USER_ID")
public class CUserAtv extends UserDto implements Serializable {
	private static final long serialVersionUID = -792484702063954203L;
	

	/**
	 * default empty constructor
	 */
	public CUserAtv() {
	}

	
}