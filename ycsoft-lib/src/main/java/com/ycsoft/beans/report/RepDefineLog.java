/**
 * RepDefineDetail.java	2010/08/13
 */

package com.ycsoft.beans.report;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * RepDefineDetail -> REP_DEFINE_DETAIL mapping
 */
@POJO(
	tn="REP_DEFINE_LOG",
	sn="",
	pk="")
public class RepDefineLog implements Serializable {

	// RepDefineDetail all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4223987541035149605L;
	private String rep_id ;
	private String rep_name ;
	private String optr_id ;
	private String optr_login_name;
	private String remark ;
	private Date  create_date ;
	private String update_type;

	public String getUpdate_type() {
		return update_type;
	}

	public void setUpdate_type(String update_type) {
		this.update_type = update_type;
	}

	/**
	 * default empty constructor
	 */
	public RepDefineLog() {}

	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	public String getRep_name() {
		return rep_name;
	}

	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}

	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getOptr_login_name() {
		return optr_login_name;
	}

	public void setOptr_login_name(String optr_login_name) {
		this.optr_login_name = optr_login_name;
	}

}