/**
 * RepKeyLevel.java	2010/06/21
 */

package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepKeyLevel -> REP_KEY_LEVEL mapping
 */
@POJO(tn = "REP_KEY_LEVEL", sn = "", pk = "KEY")
public class RepKeyLevel implements Serializable {

	// RepKeyLevel all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2664494056225543073L;
	private String key;
	private String valuesql;//权限不足的取值sql
	private String memorykey;//内存取值键值
	private String systemkey;//系统键值
	private Integer role_level;//报表标准数据权限
	private String s_data_right_type;//自定义数据权限

	public Integer getRole_level() {
		return role_level;
	}

	public void setRole_level(Integer role_level) {
		this.role_level = role_level;
	}

	public String getS_data_right_type() {
		return s_data_right_type;
	}

	public void setS_data_right_type(String s_data_right_type) {
		this.s_data_right_type = s_data_right_type;
	}

	public String getMemorykey() {
		return memorykey;
	}

	public void setMemorykey(String memorykey) {
		this.memorykey = memorykey;
	}

	public String getSystemkey() {
		return systemkey;
	}

	public void setSystemkey(String systemkey) {
		this.systemkey = systemkey;
	}

	/**
	 * default empty constructor
	 */
	public RepKeyLevel() {
	}

	// key getter and setter
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// valuesql getter and setter
	public String getValuesql() {
		return valuesql;
	}

	public void setValuesql(String valuesql) {
		this.valuesql = valuesql;
	}

}