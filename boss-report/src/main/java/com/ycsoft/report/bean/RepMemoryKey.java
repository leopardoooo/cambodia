package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;
/**
 * RepMemoryKey -> REP_MEMORY_KEY mapping
 */
@POJO(tn = "REP_MEMORY_KEY", sn = "", pk = "KEY")

public class RepMemoryKey implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3656606282951304109L;
	private String memory_key;
	private String memory_desc;
	private String memory_type;
	private String value_key;
	private String value_sql;
	private String database;
	private String remark;
	public String getMemory_key() {
		return memory_key;
	}
	public void setMemory_key(String memory_key) {
		this.memory_key = memory_key;
	}
	public String getMemory_desc() {
		return memory_desc;
	}
	public void setMemory_desc(String memory_desc) {
		this.memory_desc = memory_desc;
	}
	public String getMemory_type() {
		return memory_type;
	}
	public void setMemory_type(String memory_type) {
		this.memory_type = memory_type;
	}
	public String getValue_key() {
		return value_key;
	}
	public void setValue_key(String value_key) {
		this.value_key = value_key;
	}
	public String getValue_sql() {
		return value_sql;
	}
	public void setValue_sql(String value_sql) {
		this.value_sql = value_sql;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
