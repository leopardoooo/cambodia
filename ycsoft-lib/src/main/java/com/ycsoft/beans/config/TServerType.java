/**
 * TServerType.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TServerType -> T_SERVER_TYPE mapping
 */
@POJO(
	tn="T_SERVER_TYPE",
	sn="",
	pk="")
public class TServerType implements Serializable {

	// TServerType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8669358502832162398L;
	private String server_type ;
	private String server_type_name ;

	/**
	 * default empty constructor
	 */
	public TServerType() {}


	// server_type getter and setter
	public String getServer_type(){
		return server_type ;
	}

	public void setServer_type(String server_type){
		this.server_type = server_type ;
	}

	// server_type_name getter and setter
	public String getServer_type_name(){
		return server_type_name ;
	}

	public void setServer_type_name(String server_type_name){
		this.server_type_name = server_type_name ;
	}

}