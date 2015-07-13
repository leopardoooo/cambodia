/**
 * TExtendAttribute.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TExtendAttribute -> T_EXTEND_ATTRIBUTE mapping
 */
@POJO(tn = "T_EXTEND_ATTRIBUTE", sn = "SEQ_EXTTABLE_ATTR_ID", pk = "ATTRIBUTE_ID")
public class TExtendAttribute implements Serializable {

	// TExtendAttribute all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3931543246526249453L;
	private String extend_id;
	private String group_id;
	private String attribute_id;
	private String attribute_name;
	private String attribute_order;
	private String col_name;
	private String is_null;
	private String input_type;
	private String param_name;
	private String is_show;
	private String default_value;



	@SuppressWarnings("unused")
	private String col_name_text;
	private String is_null_text;
	private String is_show_text;


	private String input_type_text;
	private String param_name_text;

	/**
	 * default empty constructor
	 */
	public TExtendAttribute() {
	}

	// extend_id getter and setter
	public String getExtend_id() {
		return extend_id;
	}

	public void setExtend_id(String extend_id) {
		this.extend_id = extend_id;
	}

	// group_id getter and setter
	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	// attribute_id getter and setter
	public String getAttribute_id() {
		return attribute_id;
	}

	public void setAttribute_id(String attribute_id) {
		this.attribute_id = attribute_id;
	}

	// attribute_name getter and setter
	public String getAttribute_name() {
		return attribute_name;
	}

	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}

	/**
	 * @return the attribute_order
	 */
	public String getAttribute_order() {
		return attribute_order;
	}

	/**
	 * @param attribute_order
	 *            the attribute_order to set
	 */
	public void setAttribute_order(String attribute_order) {
		this.attribute_order = attribute_order;
	}

	// col_name getter and setter
	public String getCol_name() {
		return col_name;
	}

	public void setCol_name(String col_name) {
		this.col_name = col_name;
	}

	// is_null getter and setter
	public String getIs_null() {
		return is_null;
	}

	public void setIs_null(String is_null) {
		this.is_null = is_null;
		is_null_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_null);
	}

	public String getIs_show() {
		return is_show;
	}

	public void setIs_show(String isShow) {
		this.is_show = isShow;
		is_show_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_show);
	}
	
	public String getIs_show_text() {
		return is_show_text;
	}

	public void setIs_show_text(String isShowText) {
		this.is_show_text = isShowText;
	}
	
	// input_type getter and setter
	public String getInput_type() {
		return input_type;
	}

	public void setInput_type(String input_type) {
		this.input_type = input_type;
		input_type_text = MemoryDict.getDictName(DictKey.EXT_INPUT_TYPE,
				this.input_type);
	}

	// param_name getter and setter
	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getIs_null_text() {
		return is_null_text;
	}

	public void setIs_null_text(String is_null_text) {
		this.is_null_text = is_null_text;
	}

	public String getInput_type_text() {
		return input_type_text;
	}

	public void setInput_type_text(String input_type_text) {
		this.input_type_text = input_type_text;
	}

	public String getCol_name_text() {
		if(StringHelper.isNotEmpty(col_name)){
			return "扩展字段"+col_name.substring(3, col_name.length());
		}
		return null;
	}

	public String getParam_name_text() {
		return param_name_text;
	}

	public void setParam_name_text(String param_name_text) {
		this.param_name_text = param_name_text;
	}

	public String getDefault_value() {
		return default_value;
	}

	public void setDefault_value(String default_value) {
		this.default_value = default_value;
	}
}