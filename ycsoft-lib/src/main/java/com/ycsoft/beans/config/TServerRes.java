/**
 * TServerRes.java	2010/09/10
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TServerRes -> T_SERVER_RES mapping
 */
@POJO(
	tn="T_SERVER_RES",
	sn="",
	pk="SERVER_ID,EXTERNAL_RES_ID,BOSS_RES_ID")
public class TServerRes extends OptrBase implements Serializable {

	// TServerRes all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -5554626344797915237L;
	private String server_id ;
	private String external_res_id ;
	private String res_name ;
	private String boss_res_id ;
	
	private String server_name;
	private String boss_res_name;
	private String serv_type;
	private String serv_type_text;

	public String getServ_type() {
		return serv_type;
	}


	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
		this.serv_type_text = MemoryDict.getDictName(DictKey.SERV_ID, serv_type);
	}


	public String getServ_type_text() {
		return serv_type_text;
	}


	/**
	 * default empty constructor
	 */
	public TServerRes() {}


	// server_id getter and setter
	public String getServer_id(){
		return server_id ;
	}

	public void setServer_id(String server_id){
		this.server_id = server_id ;
	}

	// external_res_id getter and setter
	public String getExternal_res_id(){
		return external_res_id ;
	}

	public void setExternal_res_id(String external_res_id){
		this.external_res_id = external_res_id ;
	}

	// res_name getter and setter
	public String getRes_name(){
		return res_name ;
	}

	public void setRes_name(String res_name){
		this.res_name = res_name ;
	}

	// boss_res_id getter and setter
	public String getBoss_res_id(){
		return boss_res_id ;
	}

	public void setBoss_res_id(String boss_res_id){
		this.boss_res_id = boss_res_id ;
	}


	public String getBoss_res_name() {
		return boss_res_name;
	}


	public void setBoss_res_name(String boss_res_name) {
		this.boss_res_name = boss_res_name;
	}


	public String getServer_name() {
		return server_name;
	}


	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

}