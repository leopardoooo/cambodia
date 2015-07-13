package com.ycsoft.beans.report;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_MY_CUBE", sn = "", pk = "KEY")

public class RepMyCube implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2950292234614191807L;
	private String rep_id;
	private String optr_id;
	private String cube_json;
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getCube_json() {
		return cube_json;
	}
	public void setCube_json(String cube_json) {
		this.cube_json = cube_json;
	}
}
