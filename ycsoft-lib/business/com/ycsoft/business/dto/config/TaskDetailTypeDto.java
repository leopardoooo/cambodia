/**
 *
 */
package com.ycsoft.business.dto.config;

import com.ycsoft.beans.task.TTaskDetailType;

/**
 * @author YC-SOFT
 *
 */
public class TaskDetailTypeDto extends TTaskDetailType{

	/**
	 *
	 */
	private static final long serialVersionUID = -468240709825328581L;
	private String busi_code ;

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
}
