package com.ycsoft.business.dto.core.acct;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class AcctAcctitemThresholdDto extends CUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 822696407237876498L;
	
	private String acct_id;
	private String acctitem_id;
	private String task_code;
	private Integer threshold;
	private Integer base_threshold;
	private Integer temp_threshold;
	private String acctitem_name;

	private String task_code_text;
	
	
	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	// task_code getter and setter
	public String getTask_code() {
		return task_code;
	}

	public void setTask_code(String task_code) {
		task_code_text = MemoryDict.getDictName(DictKey.JOB_TASK_CODE, task_code);
		this.task_code = task_code;
	}

	// threshold getter and setter
	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	// base_threshold getter and setter
	public Integer getBase_threshold() {
		return base_threshold;
	}

	public void setBase_threshold(Integer base_threshold) {
		this.base_threshold = base_threshold;
	}

	// temp_threshold getter and setter
	public Integer getTemp_threshold() {
		return temp_threshold;
	}

	public void setTemp_threshold(Integer temp_threshold) {
		this.temp_threshold = temp_threshold;
	}

	/**
	 * @return the task_code_text
	 */
	public String getTask_code_text() {
		return task_code_text;
	}

	public String getAcctitem_name() {
		return acctitem_name;
	}

	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}
}
