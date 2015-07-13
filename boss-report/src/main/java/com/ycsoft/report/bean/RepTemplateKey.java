package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;
@POJO(tn = "REP_TEMPLATE_KEY", sn = "", pk = "KEY")

public class RepTemplateKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5218016312240123957L;
	private String template_key;
	private String template_type;
	private String contect;
	private String contect_cycle;
	private String database;
	private String remark;
	public String getTemplate_key() {
		return template_key;
	}
	public void setTemplate_key(String template_key) {
		this.template_key = template_key;
	}
	public String getTemplate_type() {
		return template_type;
	}
	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}
	public String getContect() {
		return contect;
	}
	public void setContect(String contect) {
		this.contect = contect;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContect_cycle() {
		return contect_cycle;
	}
	public void setContect_cycle(String contect_cycle) {
		this.contect_cycle = contect_cycle;
	}
	
}
