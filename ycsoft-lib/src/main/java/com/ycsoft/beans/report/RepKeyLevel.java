/**
 * RepKeyLevel.java	2010/06/21
 */

package com.ycsoft.beans.report;

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
	private String valuesql;
	private String memorykey;
	private String systemkey;

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