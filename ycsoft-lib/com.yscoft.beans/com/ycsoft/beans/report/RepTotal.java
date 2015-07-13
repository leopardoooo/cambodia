package com.ycsoft.beans.report;


import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * REP_TOTAL -> REP_TOTAL mapping
 */
@POJO(tn = "REP_TOTAL", sn = "", pk = "")
public class RepTotal implements Serializable {

	// RepColumns all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5365557646937184259L;

	private String rep_id;
	private String rep_column;
	private Integer rep_column_index;
	public Integer getRep_column_index() {
		return rep_column_index;
	}
	public void setRep_column_index(Integer rep_column_index) {
		this.rep_column_index = rep_column_index;
	}
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getRep_column() {
		return rep_column;
	}
	public void setRep_column(String rep_column) {
		this.rep_column = rep_column;
	}
}