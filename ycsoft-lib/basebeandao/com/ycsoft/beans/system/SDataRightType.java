/**
 * SDataType.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SDataType -> S_DATA_TYPE mapping
 */
@POJO(tn = "S_DATA_RIGHT_TYPE", sn = "", pk = "DATA_RIGHT_TYPE")
public class SDataRightType implements Serializable {

	// SDataType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4729912362820970904L;
	private String data_right_type;
	private String type_name;
	private String table_name;
	private String result_column;
	private String select_column;
	private String null_is_all;
	private String is_level;

	public String getIs_level() {
		return is_level;
	}

	public void setIs_level(String isLevel) {
		is_level = isLevel;
	}

	public String getResult_column() {
		return result_column;
	}

	public void setResult_column(String result_column) {
		this.result_column = result_column;
	}

	public String getSelect_column() {
		return select_column;
	}

	public void setSelect_column(String select_column) {
		this.select_column = select_column;
	}

	/**
	 * default empty constructor
	 */
	public SDataRightType() {
	}

	public String getData_right_type() {
		return data_right_type;
	}

	public void setData_right_type(String data_right_type) {
		this.data_right_type = data_right_type;
	}

	// type_name getter and setter
	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	// table_name getter and setter
	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getNull_is_all() {
		return null_is_all;
	}

	public void setNull_is_all(String null_is_all) {
		this.null_is_all = null_is_all;
	}



}