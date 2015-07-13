/**
 * PResgroup.java	2010/06/21
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PResgroup -> P_RESGROUP mapping
 */
@POJO(tn = "P_RESGROUP", sn = "SEQ_RES_GROUP", pk = "group_id")
public class PResgroup extends OptrBase implements Serializable {

	// PResgroup all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6126313900620760505L;
	private String group_id;
	private String group_name;
	private String serv_id;
	private String group_desc;
	private Date create_time;
	
	private String serv_id_text;

	/**
	 * default empty constructor
	 */
	public PResgroup() {
	}

	// group_id getter and setter
	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	// group_name getter and setter
	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	// serv_id getter and setter
	public String getServ_id() {
		return serv_id;
	}

	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
		serv_id_text = MemoryDict.getDictName(DictKey.SERV_ID, serv_id);
	}

	// group_desc getter and setter
	public String getGroup_desc() {
		return group_desc;
	}

	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}

	// create_time getter and setter
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getServ_id_text() {
		return serv_id_text;
	}

	public void setServ_id_text(String servIdText) {
		serv_id_text = servIdText;
	}

}