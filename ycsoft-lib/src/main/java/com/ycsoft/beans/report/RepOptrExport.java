package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_OPTR_EXPORT", sn = "", pk = "")
public class RepOptrExport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7161712074552417650L;
	
    private String optr_id;
    private String rep_id;
    private Integer col_idx;
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public Integer getCol_idx() {
		return col_idx;
	}
	public void setCol_idx(Integer col_idx) {
		this.col_idx = col_idx;
	}
}
