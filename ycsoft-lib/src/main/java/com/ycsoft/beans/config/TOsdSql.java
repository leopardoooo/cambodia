package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(tn="T_OSD_SQL",sn="",pk="QUERY_ID")
public class TOsdSql implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3707062797035404152L;
	
	private String query_id;
	private String title;
	private String sql_content;
	private String status;
	private String optr_id;
	private Date create_time;
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getQuery_id() {
		return query_id;
	}
	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSql_content() {
		return sql_content;
	}
	public void setSql_content(String sql_content) {
		this.sql_content = sql_content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
