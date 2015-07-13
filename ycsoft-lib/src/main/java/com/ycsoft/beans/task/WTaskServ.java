/**
 * WTaskServ.java	2010/03/16
 */

package com.ycsoft.beans.task;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;
/**
 * WTaskServ -> W_TASK_Serv mapping 
 */
@POJO(
	tn="W_TASK_SERV",
	sn="",
	pk="")
public class WTaskServ implements Serializable {
	
	// WTaskBaseInfo all properties 

	private String task_id ;	
	private String serv_id;
	private String serv_name;
	/**
	 * default empty constructor
	 */
	public WTaskServ() {}
	
	public WTaskServ(String task_id, String serv_id) {
		this.task_id = task_id;
		this.serv_id = serv_id;
	}

	/**
	 * @return the task_id
	 */
	public String getTask_id() {
		return task_id;
	}

	/**
	 * @param task_id the task_id to set
	 */
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	/**
	 * @return the serv_id
	 */
	public String getServ_id() {
		return serv_id;
	}

	/**
	 * @param serv_id the serv_id to set
	 */
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
	}

	/**
	 * @return the serv_name
	 */
	public String getServ_name() {
		serv_name = MemoryDict.getDictName(DictKey.SERV_ID, this.getServ_id());
		return serv_name;
	}

}