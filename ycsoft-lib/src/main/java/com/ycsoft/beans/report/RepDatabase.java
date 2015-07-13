/**
 * RepDatabase.java	2010/06/21
 */

package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepDatabase -> REP_DATABASE mapping
 */
@POJO(tn = "REP_DATABASE", sn = "", pk = "DATABASE")
public class RepDatabase implements Serializable {

	// RepDatabase all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4825445278842134968L;
	private String database;
	private String name;
	private String type;
	private String driverclass;
	private String url;
	private Integer maxidletime;
	private Integer maxpoolsize;
	private Integer minpoolsize;
	private String testquerysql;
	private Integer testoeriod;
	private String username;
	private String password;
	private String encrypt;

	/**
	 * default empty constructor
	 */
	public RepDatabase() {
	}

	// database getter and setter
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	// name getter and setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// type getter and setter
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// driverclass getter and setter
	public String getDriverclass() {
		return driverclass;
	}

	public void setDriverclass(String driverclass) {
		this.driverclass = driverclass;
	}

	// url getter and setter
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// maxidletime getter and setter
	public Integer getMaxidletime() {
		return maxidletime;
	}

	public void setMaxidletime(Integer maxidletime) {
		this.maxidletime = maxidletime;
	}

	// maxpoolsize getter and setter
	public Integer getMaxpoolsize() {
		return maxpoolsize;
	}

	public void setMaxpoolsize(Integer maxpoolsize) {
		this.maxpoolsize = maxpoolsize;
	}

	// minpoolsize getter and setter
	public Integer getMinpoolsize() {
		return minpoolsize;
	}

	public void setMinpoolsize(Integer minpoolsize) {
		this.minpoolsize = minpoolsize;
	}

	// testquerysql getter and setter
	public String getTestquerysql() {
		return testquerysql;
	}

	public void setTestquerysql(String testquerysql) {
		this.testquerysql = testquerysql;
	}

	// testoeriod getter and setter
	public Integer getTestoeriod() {
		return testoeriod;
	}

	public void setTestoeriod(Integer testoeriod) {
		this.testoeriod = testoeriod;
	}

	// username getter and setter
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// password getter and setter
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// encrypt getter and setter
	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

}