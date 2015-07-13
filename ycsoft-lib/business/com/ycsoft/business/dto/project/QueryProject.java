package com.ycsoft.business.dto.project;

public class QueryProject {

	private String project_number;
	private String project_name;
	private String start_date;
	private String end_date;
	private String county_id;
	private String addr_name;
	
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getCounty_id() {
		return county_id;
	}
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}
	public String getAddr_name() {
		return addr_name;
	}
	public void setAddr_name(String addr_name) {
		this.addr_name = addr_name;
	}
	public String getProject_number() {
		return project_number;
	}
	public void setProject_number(String project_number) {
		this.project_number = project_number;
	}
}
