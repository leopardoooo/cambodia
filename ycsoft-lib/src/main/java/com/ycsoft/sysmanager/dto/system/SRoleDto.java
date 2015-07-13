package com.ycsoft.sysmanager.dto.system;

import java.util.List;

import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.system.SRoleResource;

public class SRoleDto extends SRole {
	/**
	 *
	 */
	private static final long serialVersionUID = -2839129954519484962L;
	private String res_id;
	private String res_name;
	private String optr_id;
	private List<SRoleResource> roleList;
	public String getRes_id() {
		return res_id;
	}
	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}
	public String getRes_name() {
		return res_name;
	}
	public void setRes_name(String res_name) {
		this.res_name = res_name;
	}
	public List<SRoleResource> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<SRoleResource> roleList) {
		this.roleList = roleList;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

}
