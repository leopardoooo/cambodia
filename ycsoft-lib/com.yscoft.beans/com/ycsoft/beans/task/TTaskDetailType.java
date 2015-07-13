/**
 * TTaskDetailType.java	2010/03/04
 */

package com.ycsoft.beans.task;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TTaskDetailType -> T_TASK_DETAIL_TYPE mapping
 */
@POJO(tn = "T_TASK_DETAIL_TYPE", sn = "", pk = "")
public class TTaskDetailType implements Serializable {

	// TTaskDetailType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5148429300542667297L;
	private String detail_type_id;
	private String detail_type_name;
	private String task_type_id;
	private String terminal_cal_type;
	private String can_add_manual;
	private String is_system_cust;
	private String rule_id;
	private String remark;
	private String bug_cause;


	public String getBug_cause() {
		return bug_cause;
	}

	public void setBug_cause(String bug_cause) {
		this.bug_cause = bug_cause;
	}

	/**
	 * default empty constructor
	 */
	public TTaskDetailType() {
	}

	// detail_type_id getter and setter
	public String getDetail_type_id() {
		return detail_type_id;
	}

	public void setDetail_type_id(String detail_type_id) {
		this.detail_type_id = detail_type_id;
	}

	// detail_type_name getter and setter
	public String getDetail_type_name() {
		return detail_type_name;
	}

	public void setDetail_type_name(String detail_type_name) {
		this.detail_type_name = detail_type_name;
	}

	// task_type_id getter and setter
	public String getTask_type_id() {
		return task_type_id;
	}

	public void setTask_type_id(String task_type_id) {
		this.task_type_id = task_type_id;
	}

	// terminal_cal_type getter and setter
	public String getTerminal_cal_type() {
		return terminal_cal_type;
	}

	public void setTerminal_cal_type(String terminal_cal_type) {
		this.terminal_cal_type = terminal_cal_type;
	}

	// can_add_manual getter and setter
	public String getCan_add_manual() {
		return can_add_manual;
	}

	public void setCan_add_manual(String can_add_manual) {
		this.can_add_manual = can_add_manual;
	}

	// is_system_cust getter and setter
	public String getIs_system_cust() {
		return is_system_cust;
	}

	public void setIs_system_cust(String is_system_cust) {
		this.is_system_cust = is_system_cust;
	}

	// rule_id getter and setter
	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}