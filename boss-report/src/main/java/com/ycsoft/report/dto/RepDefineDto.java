package com.ycsoft.report.dto;

import com.ycsoft.report.bean.RepDefine;

/**
 * 配置一张报表时的所有配置信息
 */
public class RepDefineDto extends RepDefine {

	private String sql;// 查询语句
	private Integer sort_num;// 排序
	private String res_pid;// 所属上级菜单
	private String res_pid_text;// 所属上级菜单
	
	private String rep_total_list;//报表合计项(','分隔)
	
	private String rep_group;
	
	private String quiee_raq_text;
	
	private String main_sql;//主报表sql，明细配置时使用


	public String getMain_sql() {
		return main_sql;
	}

	public void setMain_sql(String main_sql) {
		this.main_sql = main_sql;
	}

	public String getRes_pid() {
		return res_pid;
	}

	public void setRes_pid(String res_pid) {
		this.res_pid = res_pid;
	}


	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Integer getSort_num() {
		return sort_num;
	}

	public void setSort_num(Integer sort_num) {
		this.sort_num = sort_num;
	}

	public String getRep_total_list() {
		return rep_total_list;
	}

	public void setRep_total_list(String rep_total_list) {
		this.rep_total_list = rep_total_list;
	}

	public String getRes_pid_text() {
		return res_pid_text;
	}

	public void setRes_pid_text(String res_pid_text) {
		this.res_pid_text = res_pid_text;
	}

	public String getQuiee_raq_text() {
		return quiee_raq_text;
	}

	public void setQuiee_raq_text(String quiee_raq_text) {
		this.quiee_raq_text = quiee_raq_text;
	}

	public String getRep_group() {
		return rep_group;
	}

	public void setRep_group(String rep_group) {
		this.rep_group = rep_group;
	}
}
