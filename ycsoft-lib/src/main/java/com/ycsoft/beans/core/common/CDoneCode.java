/**
 * CDoneCode.java	2010/03/16
 */

package com.ycsoft.beans.core.common;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CDoneCode -> C_DONE_CODE mapping
 */
@POJO(tn = "C_DONE_CODE", sn = "SEQ_DONE_CODE", pk = "DONE_CODE")
public class CDoneCode extends BusiBase implements Serializable {

	// CDoneCode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2336693094456857500L;
	private Date done_date;
	private String remark;
	private String status;
	private String flag;

	private String status_text;

	// done_date getter and setter
	public Date getDone_date() {
		return done_date;
	}

	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	public String getStatus_text() {
		return status_text;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}