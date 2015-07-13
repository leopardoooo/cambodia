package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_OPTR_CONFIGREP", sn = "", pk = "")
public class RepOptrConfigrep implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -5408089076039572839L;
	private String optr_id;
    private String optr_type;
    private String remark;
	
	public String getOptr_type() {
		return optr_type;
	}

	public void setOptr_type(String optr_type) {
		this.optr_type = optr_type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

}
