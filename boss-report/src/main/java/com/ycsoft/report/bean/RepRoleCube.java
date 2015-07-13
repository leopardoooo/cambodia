package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_ROLE_CUBE", sn = "", pk = "")
public class RepRoleCube implements Serializable {
	private String rep_id;
	private String role_id;
	private String cube_json;
	private String name;
	private String remark;
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getCube_json() {
		return cube_json;
	}
	public void setCube_json(String cube_json) {
		this.cube_json = cube_json;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
