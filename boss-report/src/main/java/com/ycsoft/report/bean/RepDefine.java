/**
 * RepDefine.java	2010/06/21
 */

package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * RepDefine -> REP_DEFINE mapping
 */
@POJO(tn = "REP_DEFINE", sn = "", pk = "REP_ID")
public class RepDefine implements Serializable {

	// RepDefine all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6105263442151344411L;
	private String rep_id;
	private String rep_name;
	private String rep_type;
	private String database;
	private String database_text;
	private String rep_info;//报表属性分类
	private String quiee_raq;//快逸模板
	private String detail_id;//对应明细查询
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * default empty constructor
	 */
	public RepDefine() {
	}

	// rep_id getter and setter
	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	// rep_type getter and setter
	public String getRep_type() {
		return rep_type;
	}

	public void setRep_type(String rep_type) {
		this.rep_type = rep_type;
	}

	// database getter and setter
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getRep_info() {
		return rep_info;
	}

	public void setRep_info(String rep_info) {
		this.rep_info = rep_info;
	}

	public String getQuiee_raq() {
		return quiee_raq;
	}

	public void setQuiee_raq(String quiee_raq) {
		this.quiee_raq = quiee_raq;
	}

	public String getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}

	public String getDatabase_text() {
		return database_text;
	}

	public void setDatabase_text(String database_text) {
		this.database_text = database_text;
	}

	public String getRep_name() {
		return rep_name;
	}

	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}

}