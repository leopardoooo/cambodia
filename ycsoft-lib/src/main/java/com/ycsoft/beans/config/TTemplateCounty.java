/**
 * TTemplateCounty.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TTemplateCounty -> T_TEMPLATE_COUNTY mapping
 */
@POJO(tn = "T_TEMPLATE_COUNTY", sn = "", pk = "COUNTY_ID,TEMPLATE_TYPE")
public class TTemplateCounty extends TTemplate implements Serializable {

	// TTemplateCounty all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1211959915150558871L;

	/**
	 * default empty constructor
	 */
	public TTemplateCounty() {
	}


}