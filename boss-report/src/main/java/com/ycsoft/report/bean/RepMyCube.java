package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_MY_CUBE", sn = "seq_repquery_id", pk = "")

public class RepMyCube implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2950292234614191807L;
	private String template_id;//TEMPLATE_ID
	private String rep_id;
	private String optr_id;
	private String name;
	private String cube_json;
	private String warn_json;//值警戒配置
	private String default_show;
	private String remark;
	private String status;
	
	private String cube_config_text;//cube配置文本
	public String getCube_config_text() {
		return cube_config_text;
	}
	public void setCube_config_text(String cube_config_text) {
		this.cube_config_text = cube_config_text;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefault_show() {
		return default_show;
	}
	public void setDefault_show(String default_show) {
		this.default_show = default_show;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getWarn_json() {
		return warn_json;
	}
	public void setWarn_json(String warn_json) {
		this.warn_json = warn_json;
	}
}
