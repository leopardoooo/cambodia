package com.ycsoft.beans.core.user;

/**
 * CUserPropChange.java	2010/05/19
 */

import java.io.Serializable;

import com.ycsoft.beans.core.common.PropertyChange;
import com.ycsoft.daos.config.POJO;

/**
 * CUserPropChange -> C_USER_PROP_CHANGE mapping
 */
@POJO(tn = "C_USER_PROP_CHANGE", sn = "", pk = "")
public class CUserPropChange extends PropertyChange implements Serializable {
	private static final long serialVersionUID = 4047769118118712512L;
	private String user_id;

	/**
	 * default empty constructor
	 */
	public CUserPropChange() {
	}
	
	public CUserPropChange(String column_name, String old_value,
			String new_value) {
		setColumn_name(column_name);
		setOld_value(old_value);
		setNew_value(new_value);
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}