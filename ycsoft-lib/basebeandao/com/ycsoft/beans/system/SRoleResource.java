/**
 * SRoleResource.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SRoleResource -> S_ROLE_RESOURCE mapping
 */
@POJO(tn = "S_ROLE_RESOURCE", sn = "", pk = "")
public class SRoleResource implements Serializable {

	// SRoleResource all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5478615709198678478L;
	private String role_id;
	private String res_id;
	private String role_name;
	private String res_name;

	/**
	 * default empty constructor
	 */
	public SRoleResource() {
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String roleName) {
		role_name = roleName;
	}

	public String getRes_name() {
		return res_name;
	}

	public void setRes_name(String resName) {
		res_name = resName;
	}

}