/**
 * RepHead.java	2010/06/21
 */

package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepHead -> REP_HEAD mapping
 */
@POJO(tn = "REP_HEAD", sn = "", pk = "")
public class RepHead implements Serializable {

	// RepHead all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -7290743318456192124L;
	
	private Integer col_length;//表头长
	private String col_desc;//表头描述
	private Integer col_hidden_length;//表头隐藏后长度
	
	private boolean ishidden = false;//表头是否隐藏
	private boolean nothidden=true;//表头不能隐藏;维度底层不能隐藏
	
	/**
	 * 不实用
	 */
	private Integer row_high;//表头高
	private Integer col_pid;//父级列序号
	private String rep_id;
	private Integer row_seq;//表头row坐标
	private Integer col_seq;//表头col坐标
	private String olap_type;

	public String getOlap_type() {
		return olap_type;
	}

	public void setOlap_type(String olap_type) {
		this.olap_type = olap_type;
	}

	/**
	 * default empty constructor
	 */
	public RepHead() {
	}

	// rep_id getter and setter
	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	// row_seq getter and setter
	public Integer getRow_seq() {
		return row_seq;
	}

	public void setRow_seq(Integer row_seq) {
		this.row_seq = row_seq;
	}

	// col_seq getter and setter
	public Integer getCol_seq() {
		return col_seq;
	}

	public void setCol_seq(Integer col_seq) {
		this.col_seq = col_seq;
	}

	// col_length getter and setter
	public Integer getCol_length() {
		return col_length;
	}

	public void setCol_length(Integer col_length) {
		this.col_length = col_length;
	}

	// col_desc getter and setter
	public String getCol_desc() {
		return col_desc;
	}

	public void setCol_desc(String col_desc) {
		this.col_desc = col_desc;
	}

	public Integer getRow_high() {
		return row_high;
	}

	public boolean isIshidden() {
		return ishidden;
	}

	public void setIshidden(boolean ishidden) {
		this.ishidden = ishidden;
	}

	public boolean isNothidden() {
		return nothidden;
	}

	public void setNothidden(boolean nothidden) {
		this.nothidden = nothidden;
	}

	public void setRow_high(Integer row_high) {
		this.row_high = row_high;
	}

	public Integer getCol_pid() {
		return col_pid;
	}

	public void setCol_pid(Integer col_pid) {
		this.col_pid = col_pid;
	}

	public Integer getCol_hidden_length() {
		return col_hidden_length;
	}

	public void setCol_hidden_length(Integer col_hidden_length) {
		this.col_hidden_length = col_hidden_length;
	}

}