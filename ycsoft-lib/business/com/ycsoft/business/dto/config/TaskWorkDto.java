/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 *
 */
public class TaskWorkDto {

	/**
	 *
	 */
	private static final long serialVersionUID = -468240709825328581L;
	private String busi_code ;
	private String task_type;
	private String task_type_name;

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}

	public String getTask_type() {
		return task_type;
	}

	public void setTask_type(String task_type) {
		this.task_type = task_type;
		task_type_name = MemoryDict.getDictName(DictKey.TASK_TYPE.toString(), task_type);
	}

	public String getTask_type_name() {
		return task_type_name;
	}

	public void setTask_type_name(String task_type_name) {
		this.task_type_name = task_type_name;
	}
	
}
