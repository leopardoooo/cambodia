package com.ycsoft.beans.report;

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
	private String name;
	private int dim_level;
	private String dim_level_name;
	private String column_code;
	private String column_text;
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
	
}
