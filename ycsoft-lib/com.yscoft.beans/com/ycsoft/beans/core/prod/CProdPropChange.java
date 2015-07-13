/**
 * CProdPropChange.java	2010/07/13
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.beans.core.common.PropertyChange;
import com.ycsoft.daos.config.POJO;

/**
 * CProdPropChange -> C_PROD_PROP_CHANGE mapping
 */
@POJO(tn = "C_PROD_PROP_CHANGE", sn = "", pk = "")
public class CProdPropChange extends PropertyChange implements Serializable {

	// CProdPropChange all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = 4437868660893338757L;
	private String prod_sn;

	/**
	 * default empty constructor
	 */
	public CProdPropChange() {
	}

	public CProdPropChange(String column_name, String old_value,
			String new_value) {
		setColumn_name(column_name);
		setOld_value(old_value);
		setNew_value(new_value);
	}

	// sn getter and setter
	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

}