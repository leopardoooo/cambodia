/**
 * WTaskBaseInfo.java	2010/03/16
 */

package com.ycsoft.beans.task;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.pojo.UserTypeDto;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * WTaskBaseInfo -> W_TASK_BASE_INFO mapping
 */
@POJO(tn = "W_TASK_BASE_INFO", sn = "", pk = "")
public class WTaskBaseInfo extends BusiBase implements Serializable,
		UserTypeDto {

	// WTaskBaseInfo all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3902988035837840214L;
	private String task_id;
	private String task_title;
	private String task_type_id;
	private String task_detail_type_id;
	private String task_create_type;
	private String task_status;
	private String team_id;
	private String installer_id;
	private String invisitor_id;
	private Date task_create_time;
	private Date task_assign_time;
	private Date task_invalide_time;
	private Date task_finish_teime;
	private Date revisit_time;
	private String task_finish_type;
	private String cust_id;
	private Integer user_count;
	private String cust_name;
	private String new_addr;
	private String old_addr;
	private String tel;
	private String mobile;
	private String remark;

	private String user_type;
	private String net_type;
	private String terminal_type;
	private String serv_type;

	private String task_type_name;
	private String task_detail_type_name;
	private String task_status_text;

	/**
	 * @return the user_type
	 */
	public String getUser_type() {
		return user_type;
	}

	/**
	 * @return the net_type
	 */
	public String getNet_type() {
		return net_type;
	}

	/**
	 * @return the terminal_type
	 */
	public String getTerminal_type() {
		return terminal_type;
	}

	/**
	 * @return the serv_type
	 */
	public String getServ_type() {
		return serv_type;
	}

	/**
	 * @return the task_type_name
	 */
	public String getTask_type_name() {
		return task_type_name;
	}

	/**
	 * @return the task_detail_type_name
	 */
	public String getTask_detail_type_name() {
		return task_detail_type_name;
	}

	/**
	 * @return the task_status_text
	 */
	public String getTask_status_text() {
		return task_status_text;
	}

	/**
	 * default empty constructor
	 */
	public WTaskBaseInfo() {
	}

	// task_id getter and setter
	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	// task_title getter and setter
	public String getTask_title() {
		return task_title;
	}

	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}

	// task_type_id getter and setter
	public String getTask_type_id() {
		return task_type_id;
	}

	public void setTask_type_id(String task_type_id) {
		this.task_type_id = task_type_id;
	}

	// task_detail_type_id getter and setter
	public String getTask_detail_type_id() {
		return task_detail_type_id;
	}

	public void setTask_detail_type_id(String task_detail_type_id) {
		this.task_detail_type_id = task_detail_type_id;
	}

	// task_create_type getter and setter
	public String getTask_create_type() {
		return task_create_type;
	}

	public void setTask_create_type(String task_create_type) {
		this.task_create_type = task_create_type;
	}

	// task_status getter and setter
	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		task_status_text = MemoryDict.getDictName(DictKey.STATUS, task_status);
		this.task_status = task_status;
	}

	// team_id getter and setter
	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}

	// installer_id getter and setter
	public String getInstaller_id() {
		return installer_id;
	}

	public void setInstaller_id(String installer_id) {
		this.installer_id = installer_id;
	}

	// invisitor_id getter and setter
	public String getInvisitor_id() {
		return invisitor_id;
	}

	public void setInvisitor_id(String invisitor_id) {
		this.invisitor_id = invisitor_id;
	}

	// task_create_time getter and setter
	public Date getTask_create_time() {
		return task_create_time;
	}

	public void setTask_create_time(Date task_create_time) {
		this.task_create_time = task_create_time;
	}

	// task_assign_time getter and setter
	public Date getTask_assign_time() {
		return task_assign_time;
	}

	public void setTask_assign_time(Date task_assign_time) {
		this.task_assign_time = task_assign_time;
	}

	// task_invalide_time getter and setter
	public Date getTask_invalide_time() {
		return task_invalide_time;
	}

	public void setTask_invalide_time(Date task_invalide_time) {
		this.task_invalide_time = task_invalide_time;
	}

	// task_finish_teime getter and setter
	public Date getTask_finish_teime() {
		return task_finish_teime;
	}

	public void setTask_finish_teime(Date task_finish_teime) {
		this.task_finish_teime = task_finish_teime;
	}

	// revisit_time getter and setter
	public Date getRevisit_time() {
		return revisit_time;
	}

	public void setRevisit_time(Date revisit_time) {
		this.revisit_time = revisit_time;
	}

	// task_finish_type getter and setter
	public String getTask_finish_type() {
		return task_finish_type;
	}

	public void setTask_finish_type(String task_finish_type) {
		this.task_finish_type = task_finish_type;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// user_count getter and setter
	public Integer getUser_count() {
		return user_count;
	}

	public void setUser_count(Integer user_count) {
		this.user_count = user_count;
	}

	// cust_name getter and setter
	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	// new_addr getter and setter
	public String getNew_addr() {
		return new_addr;
	}

	public void setNew_addr(String new_addr) {
		this.new_addr = new_addr;
	}

	// old_addr getter and setter
	public String getOld_addr() {
		return old_addr;
	}

	public void setOld_addr(String old_addr) {
		this.old_addr = old_addr;
	}

	// mobile getter and setter
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	// remark getter and setter
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	/**
	 * @param task_type_name the task_type_name to set
	 */
	public void setTask_type_name(String task_type_name) {
		this.task_type_name = task_type_name;
	}

	/**
	 * @param task_detail_type_name the task_detail_type_name to set
	 */
	public void setTask_detail_type_name(String task_detail_type_name) {
		this.task_detail_type_name = task_detail_type_name;
	}

}