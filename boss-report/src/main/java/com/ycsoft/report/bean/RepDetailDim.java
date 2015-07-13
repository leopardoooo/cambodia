package com.ycsoft.report.bean;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_DETAIL_DIM", sn = "", pk = "")
public class RepDetailDim {

	private String rep_id;
	private String dim;
	private int idx;
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getDim() {
		return dim;
	}
	public void setDim(String dim) {
		this.dim = dim;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
}
