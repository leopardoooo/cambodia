/**
 * CAcctAcctitemThreshold.java	2010/07/12
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemThreshold -> C_ACCT_ACCTITEM_THRESHOLD mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_THRESHOLD", sn = "", pk = "")
public class CAcctAcctitemThreshold extends CountyBase implements Serializable {

	// CAcctAcctitemThreshold all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8986660942703164014L;
	private String acct_id;
	private String acctitem_id;
	private String task_code;
	private Integer threshold;
	private Integer base_threshold;
	private Integer temp_threshold;

	private String task_code_text;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemThreshold() {
	}

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

}