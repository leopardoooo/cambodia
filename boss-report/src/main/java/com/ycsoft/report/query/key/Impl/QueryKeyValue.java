package com.ycsoft.report.query.key.Impl;


import com.ycsoft.report.query.key.ConKey;

public class QueryKeyValue implements ConKey {
	
	private String id;

	private String name;
	
	private String pid;

	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
