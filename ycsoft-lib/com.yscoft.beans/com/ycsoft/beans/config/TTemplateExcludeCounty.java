/**
 * TTemplateExcludeCounty.java	2013/01/23
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable;
import com.ycsoft.daos.config.POJO;

/**
 * TTemplateExcludeCounty -> T_TEMPLATE_EXCLUDE_COUNTY mapping 
 */
@POJO(
	tn="T_TEMPLATE_EXCLUDE_COUNTY",
	sn="",
	pk="")
public class TTemplateExcludeCounty implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5239015105783621712L;
	private String template_id;
	private String county_id;
	private String is_include;
	
	/**
	 * default empty constructor
	 */
	public TTemplateExcludeCounty() {}
	
	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	public String getIs_include() {
		return is_include;
	}

	public void setIs_include(String is_include) {
		this.is_include = is_include;
	}

}