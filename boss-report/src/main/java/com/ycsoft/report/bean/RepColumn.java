/**
 * RepColumns.java	2010/07/16
 */

package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepColumns -> REP_COLUMNS mapping
 */
@POJO(tn = "REP_COLUMNS", sn = "", pk = "")
public class RepColumn implements Serializable {

	// RepColumns all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6255537069966011739L;
	private String rep_id;
	private String dim_key;
	private Integer col_index;
	private String olap_type;
	// 统计报表选中的key
	private String dim_key_select;
	// 被选中的ID
	private String dim_id_select;

	public String getDim_key_select() {
		return dim_key_select;
	}

	public void setDim_key_select(String dim_key_select) {
		this.dim_key_select = dim_key_select;
	}

	public String getDim_id_select() {
		return dim_id_select;
	}

	public void setDim_id_select(String dim_id_select) {
		this.dim_id_select = dim_id_select;
	}

	/**
	 * default empty constructor
	 */
	public RepColumn() {
	}

	// rep_id getter and setter
	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	// dim_key getter and setter
	public String getDim_key() {
		return dim_key;
	}

	public void setDim_key(String dim_key) {
		this.dim_key = dim_key;
	}

	// col_index getter and setter
	public Integer getCol_index() {
		return col_index;
	}

	public void setCol_index(Integer col_index) {
		this.col_index = col_index;
	}

	public String getOlap_type() {
		return olap_type;
	}

	public void setOlap_type(String olap_type) {
		this.olap_type = olap_type;
	}

}