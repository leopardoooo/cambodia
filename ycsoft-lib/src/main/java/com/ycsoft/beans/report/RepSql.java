/**
 * RepSql.java	2010/06/21
 */

package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepSql -> REP_SQL mapping
 */
@POJO(tn = "REP_SQL", sn = "", pk = "")
public class RepSql implements Serializable {

	// RepSql all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5177972739880745752L;
	private String rep_id;
	private Integer sql_index;
	private String query_sql;

	/**
	 * default empty constructor
	 */
	public RepSql() {
	}

	// rep_id getter and setter
	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	// sql_index getter and setter
	public Integer getSql_index() {
		return sql_index;
	}

	public void setSql_index(Integer sql_index) {
		this.sql_index = sql_index;
	}

	// query_sql getter and setter
	public String getQuery_sql() {
		return query_sql;
	}

	public void setQuery_sql(String query_sql) {
		this.query_sql = query_sql;
	}
}