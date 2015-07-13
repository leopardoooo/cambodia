/**
 * TServerCounty.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TServerCounty -> T_SERVER_COUNTY mapping
 */
@POJO(
	tn="T_SERVER_COUNTY",
	sn="",
	pk="")
public class TServerCounty implements Serializable {

	// TServerCounty all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1600240727428506450L;
	private String server_id ;
	private String county_id ;

	/**
	 * default empty constructor
	 */
	public TServerCounty() {}


	// server_id getter and setter
	public String getServer_id(){
		return server_id ;
	}

	public void setServer_id(String server_id){
		this.server_id = server_id ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}