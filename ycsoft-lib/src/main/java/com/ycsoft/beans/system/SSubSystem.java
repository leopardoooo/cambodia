/**
 * SSubSystem.java	2010/09/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * SSubSystem -> S_SUB_SYSTEM mapping
 */
@POJO(
	tn="S_SUB_SYSTEM",
	sn="",
	pk="SUB_SYSTEM_ID")
public class SSubSystem implements Serializable {

	// SSubSystem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6042362501076143670L;
	private String sub_system_id ;
	private String sub_system_name ;
	private String sub_system_url ;
	private String sub_system_host ;
	private String iconcls ;

	/**
	 * default empty constructor
	 */
	public SSubSystem() {}


	// sub_system_id getter and setter
	public String getSub_system_id(){
		return sub_system_id ;
	}

	public void setSub_system_id(String sub_system_id){
		this.sub_system_id = sub_system_id ;
	}

	// sub_system_name getter and setter
	public String getSub_system_name(){
		return sub_system_name ;
	}

	public void setSub_system_name(String sub_system_name){
		this.sub_system_name = sub_system_name ;
	}

	// sub_system_url getter and setter
	public String getSub_system_url(){
		return sub_system_url ;
	}

	public void setSub_system_url(String sub_system_url){
		this.sub_system_url = sub_system_url ;
	}

	// sub_system_host getter and setter
	public String getSub_system_host(){
		return sub_system_host ;
	}

	public void setSub_system_host(String sub_system_host){
		this.sub_system_host = sub_system_host ;
	}

	// iconcls getter and setter
	public String getIconcls(){
		return iconcls ;
	}

	public void setIconcls(String iconcls){
		this.iconcls = iconcls ;
	}

}