package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(
		tn="REP_DIMENSION_LEVEL",
		sn="",
		pk="")
public class RepDimensionLevel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7303178677964091599L;
	private String id;
	private int dim_level;
	private String dim_level_name;
	private String column_code;//数据库 ID代码
	private String column_text;//数据库 ID描述代码
	private String column_pid;//数据库 父级ID代码
	private String column_table;//数据取值表
	private String key_level;//数据权限限制键值
	private String memorykey;//内存缓存键值
	public String getMemorykey() {
		return memorykey;
	}
	public void setMemorykey(String memorykey) {
		this.memorykey = memorykey;
	}
	public String getKey_level() {
		return key_level;
	}
	public void setKey_level(String key_level) {
		this.key_level = key_level;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getDim_level() {
		return dim_level;
	}
	public void setDim_level(int dim_level) {
		this.dim_level = dim_level;
	}
	public String getColumn_code() {
		return column_code;
	}
	public void setColumn_code(String column_code) {
		this.column_code = column_code;
	}
	public String getColumn_text() {
		return column_text;
	}
	public void setColumn_text(String column_text) {
		this.column_text = column_text;
	}
	public String getDim_level_name() {
		return dim_level_name;
	}
	public void setDim_level_name(String dim_level_name) {
		this.dim_level_name = dim_level_name;
	}
	public String getColumn_pid() {
		return column_pid;
	}
	public void setColumn_p(String column_p) {
		this.column_pid = column_p;
	}
	public String getColumn_table() {
		return column_table;
	}
	public void setColumn_table(String column_table) {
		this.column_table = column_table;
	}
	
}
