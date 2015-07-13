/**
 * SOptrRole.java	2010/04/21
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * SOptrRole -> S_OPTR_ROLE mapping
 */
@POJO(tn = "S_OPTR_ROLE", sn = "", pk = "")
public class SOptrRole implements Serializable {

	// SOptrRole all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 840572056987844891L;
	private String optr_id;
	private String optr_name;
	private String role_id;
	private String role_name;

	/**
	 * default empty constructor
	 */
	public SOptrRole() {
	}
	public SOptrRole(String optrId,String roleId) {
		optr_id = optrId;
		role_id = roleId;
	}
	// optr_id getter and setter
	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}

	// role_id getter and setter
	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getOptr_name() {
		return optr_name;
	}
	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String roleName) {
		role_name = roleName;
	}

}