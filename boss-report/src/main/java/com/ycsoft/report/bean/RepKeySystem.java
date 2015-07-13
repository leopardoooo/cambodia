/**
 * RepKeySystem.java	2010/06/21
 */

package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepKeySystem -> REP_KEY_SYSTEM mapping
 */
@POJO(tn = "REP_KEY_SYSTEM", sn = "", pk = "")
public class RepKeySystem implements Serializable {

	// RepKeySystem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7793101129861179105L;
	private String key;
	private String name;

	/**
	 * default empty constructor
	 */
	public RepKeySystem() {
	}

	// key getter and setter
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// name getter and setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}