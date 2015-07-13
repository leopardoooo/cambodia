/**
 * TTaskType.java	2010/03/04
 */

package com.ycsoft.beans.task;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * TTaskType -> T_TASK_TYPE mapping
 */
@POJO(tn = "T_TASK_TYPE", sn = "", pk = "")
public class TTaskType implements Serializable {

	// TTaskType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -911585294670462804L;
	private String task_type_id;
	private String task_type_name;
	private String remark;

	/**
	 * default empty constructor
	 */
	public TTaskType() {
	}

	// task_type_id getter and setter
	public String getTask_type_id() {
		return task_type_id;
	}

	public void setTask_type_id(String task_type_id) {
		this.task_type_id = task_type_id;
	}

	// task_type_name getter and setter
	public String getTask_type_name() {
		return task_type_name;
	}

	public void setTask_type_name(String task_type_name) {
		this.task_type_name = task_type_name;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}