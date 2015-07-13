/**
 * RepKeyCon.java	2010/06/21
 */

package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;
import com.ycsoft.report.commons.tree.TreeLink;
import com.ycsoft.report.query.key.Impl.ConKeyValue;

/**
 * RepKeyCon -> REP_KEY_CON mapping
 */
@POJO(tn = "REP_KEY_CON", sn = "", pk = "KEY")
public class RepKeyCon extends ConKeyValue implements Serializable,TreeLink {

	// RepKeyCon all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2212202789816394515L;
	private String name;
	private String type;
	private String htmlcode;
	private Integer htmlorder;
	private String fkey;
	private String testvalue;
	private String valuesql;
	private Integer levle;
	private String database;
	private String isnull;
    private String memorykey;
	
	public String getMemorykey() {
		return memorykey;
	}

	public void setMemorykey(String memorykey) {
		this.memorykey = memorykey;
	}

	public String getIsnull() {
		return isnull;
	}

	public void setIsnull(String isnull) {
		this.isnull = isnull;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * default empty constructor
	 */
	public RepKeyCon() {
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

	// htmlcode getter and setter
	public String getHtmlcode() {
		return htmlcode;
	}

	public void setHtmlcode(String htmlcode) {
		this.htmlcode = htmlcode;
	}

	// htmlorder getter and setter
	public Integer getHtmlorder() {
		return htmlorder;
	}

	public void setHtmlorder(Integer htmlorder) {
		this.htmlorder = htmlorder;
	}

	// fkey getter and setter
	public String getFkey() {
		return fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}

	// testvalue getter and setter
	public String getTestvalue() {
		return testvalue;
	}

	public void setTestvalue(String testvalue) {
		this.testvalue = testvalue;
	}

	// valuesql getter and setter
	public String getValuesql() {
		return valuesql;
	}

	public void setValuesql(String valuesql) {
		this.valuesql = valuesql;
	}

	// levle getter and setter
	public Integer getLevle() {
		return levle;
	}

	public void setLevle(Integer levle) {
		this.levle = levle;
	}

	public String getPid() {
		return getFkey();
	}

}