/**
 * TTemplate.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TTemplate -> T_TEMPLATE mapping
 */
@POJO(tn = "T_TEMPLATE", sn = "", pk = "TEMPLATE_ID")
public class TTemplate extends CountyBase implements Serializable {

	// TTemplate all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -9081334333554495829L;
	private String template_id;
	private String template_name;
	private String template_type;
	private String remark;
	
	private String optr_id;

	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optrId) {
		optr_id = optrId;
	}

	// 显示值
	private String template_type_text;

	public String getTemplate_type_text() {
		return template_type_text;
	}

	public void setTemplate_type_text(String template_type_text) {
		this.template_type_text = template_type_text;
	}

	/**
	 * default empty constructor
	 */
	public TTemplate() {
	}

	// tempalte_id getter and setter

	// template_name getter and setter
	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	// template_type getter and setter
	public String getTemplate_type() {
		return template_type;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
		template_type_text = MemoryDict.getDictName(DictKey.TEMPLATE_TYPE,
				template_type);
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

}