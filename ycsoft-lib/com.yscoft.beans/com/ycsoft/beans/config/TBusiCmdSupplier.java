/**
 * TBusiCmdSupplier.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TBusiCmdSupplier -> T_BUSI_CMD_SUPPLIER mapping
 */
@POJO(
	tn="T_BUSI_CMD_SUPPLIER",
	sn="",
	pk="")
public class TBusiCmdSupplier implements Serializable {

	// TBusiCmdSupplier all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5033127810964121637L;
	private String cmd_id ;
	private String supplier_id ;
	private String supplier_cmd_id ;
	private String supplier_cmd_name ;
	private Integer idx ;

	/**
	 * default empty constructor
	 */
	public TBusiCmdSupplier() {}


	// cmd_id getter and setter
	public String getCmd_id(){
		return cmd_id ;
	}

	public void setCmd_id(String cmd_id){
		this.cmd_id = cmd_id ;
	}

	// supplier_cmd_id getter and setter
	public String getSupplier_cmd_id(){
		return supplier_cmd_id ;
	}

	public void setSupplier_cmd_id(String supplier_cmd_id){
		this.supplier_cmd_id = supplier_cmd_id ;
	}

	// supplier_cmd_name getter and setter
	public String getSupplier_cmd_name(){
		return supplier_cmd_name ;
	}

	public void setSupplier_cmd_name(String supplier_cmd_name){
		this.supplier_cmd_name = supplier_cmd_name ;
	}

	// idx getter and setter
	public Integer getIdx(){
		return idx ;
	}

	public void setIdx(Integer idx){
		this.idx = idx ;
	}


	public String getSupplier_id() {
		return supplier_id;
	}


	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

}