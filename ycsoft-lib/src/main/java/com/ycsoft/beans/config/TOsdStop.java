package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(tn="T_OSD_STOP",sn="",pk="DONECODE")
public class TOsdStop implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3707062797035404152L;
	private String optr_id;
	private String optr_name;
	private String done_code;
	private Date op_time;
	private String status;
	private String status_text;
	private Date eff_end_time;
	private String remark;
	/**
	 * @return the op_time
	 */
	public Date getOp_time() {
		return op_time;
	}
	/**
	 * @param op_time the op_time to set
	 */
	public void setOp_time(Date op_time) {
		this.op_time = op_time;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}
	/**
	 * @return the eff_end_time
	 */
	public Date getEff_end_time() {
		return eff_end_time;
	}
	/**
	 * @param eff_end_time the eff_end_time to set
	 */
	public void setEff_end_time(Date eff_end_time) {
		this.eff_end_time = eff_end_time;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the status_text
	 */
	public String getStatus_text() {
		return status_text;
	}
	/**
	 * @return the optr_id
	 */
	public String getOptr_id() {
		return optr_id;
	}
	/**
	 * @param optr_id the optr_id to set
	 */
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
		optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}
	/**
	 * @return the done_code
	 */
	public String getDone_code() {
		return done_code;
	}
	/**
	 * @param done_code the done_code to set
	 */
	public void setDone_code(String done_code) {
		this.done_code = done_code;
	}
}
