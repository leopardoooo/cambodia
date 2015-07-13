/**
 * TTemplateType.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TTemplateType -> T_TEMPLATE_TYPE mapping
 */
@POJO(tn = "T_TEMPLATE_TYPE", sn = "", pk = "")
public class TTemplateType implements Serializable {

	// TTemplateType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3929715366232989743L;
	private String template_type;
	private String type_name;

	/**
	 * default empty constructor
	 */
	public TTemplateType() {
	}

	// template_type getter and setter
	public String getTemplate_type() {
		return template_type;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	// type_name getter and setter
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

}