package com.ycsoft.beans.task;

import com.ycsoft.daos.config.POJO;

@POJO(
		tn="W_TEAM",
		sn="",
		pk="DEPT_ID")
public class WTeam {
	private String dept_id;
	private String team_type;
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
	public String getTeam_type() {
		return team_type;
	}
	public void setTeam_type(String team_type) {
		this.team_type = team_type;
	}
	
	

}
