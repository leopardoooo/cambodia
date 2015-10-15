/**
 * CCustPropChange.java	2010/03/24
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;

import com.ycsoft.beans.core.common.PropertyChange;
import com.ycsoft.daos.config.POJO;

/**
 * CCustPropChange -> C_CUST_PROP_CHANGE mapping
 */
@POJO(tn = "C_CUST_PROP_CHANGE", sn = "", pk = "")
public class CCustPropChange extends PropertyChange implements Serializable {
	private static final long serialVersionUID = -6207107753181290108L;
	private String cust_id;

	/**
	 * default empty constructor
	 */
	public CCustPropChange() {
	}
	
	public CCustPropChange(String column_name, String old_value, String new_value) {
		setColumn_name(column_name);
		setOld_value(old_value);
		setNew_value(new_value);
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

}