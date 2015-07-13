/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TExtendAttribute;

/**
 * @author YC-SOFT
 *
 */
public class ExtendTableAttributeDto extends TExtendAttribute {

	/**
	 *
	 */
	private static final long serialVersionUID = -2762504311256842843L;
	private String edit_value;// 编辑时的值
	private String display_text;// 显示时的值

	private String group_name;



	/**
	 * @return the edit_value
	 */
	public String getEdit_value() {
		return edit_value;
	}

	/**
	 * @param edit_value
	 *            the edit_value to set
	 */
	public void setEdit_value(String edit_value) {
		this.edit_value = edit_value;
	}

	/**
	 * @return the group_name
	 */
	public String getGroup_name() {
		return group_name;
	}

	/**
	 * @param group_name
	 *            the group_name to set
	 */
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}


	public String getDisplay_text() {
		return display_text;
	}

	public void setDisplay_text(String display_text) {
		this.display_text = display_text;
	}

}
