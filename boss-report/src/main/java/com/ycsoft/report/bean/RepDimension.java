package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepDefineDetail -> REP_DEFINE_DETAIL mapping
 */
@POJO(
	tn="REP_DIMENSION",
	sn="",
	pk="")
public class RepDimension implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9079395682347630207L;
	private String id;
	private String name;
	private String from_table;
	private String database;
	private String datesign;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getFrom_table() {
		return from_table;
	}
	public void setFrom_table(String from_table) {
		this.from_table = from_table;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getDatesign() {
		return datesign;
	}
	public void setDatesign(String datesign) {
		this.datesign = datesign;
	}
	
}
