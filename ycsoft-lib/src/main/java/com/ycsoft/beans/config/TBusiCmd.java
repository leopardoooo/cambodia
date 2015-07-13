/**
 * TBusiCmd.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TBusiCmd -> T_BUSI_CMD mapping
 */
@POJO(
	tn="T_BUSI_CMD",
	sn="",
	pk="")
public class TBusiCmd implements Serializable {

	// TBusiCmd all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6805238595629294628L;
	private String cmd_id ;
	private String cmd_name ;

	/**
	 * default empty constructor
	 */
	public TBusiCmd() {}


	// cmd_id getter and setter
	public String getCmd_id(){
		return cmd_id ;
	}

	public void setCmd_id(String cmd_id){
		this.cmd_id = cmd_id ;
	}

	// cmd_name getter and setter
	public String getCmd_name(){
		return cmd_name ;
	}

	public void setCmd_name(String cmd_name){
		this.cmd_name = cmd_name ;
	}

}