package com.ycsoft.sysmanager.dto.config;

import com.ycsoft.beans.config.TExtendAttribute;

@SuppressWarnings("serial")
public class ExtendTableDto extends TExtendAttribute {

	private String column_id;
	private String extend_table;
	private String extend_type;
	private String group_name;
	private boolean isPK;
	private String busi_code;

	public String getExtend_table() {
		return extend_table;
	}

	public void setExtend_table(String extend_table) {
		this.extend_table = extend_table;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getColumn_id() {
		return column_id;
	}

	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	public String getExtend_type() {
		return extend_type;
	}

	public void setExtend_type(String extend_type) {
		this.extend_type = extend_type;
	}

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

}
