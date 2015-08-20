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
@POJO(tn = "W_TASK_BASE_INFO", sn = "", pk = "task_id")
public class WTaskBaseInfo implements Serializable{
	private static final long serialVersionUID = -3902988035837840214L;
	private String task_id;
	private String task_title;
	private String task_type_id;
	private String task_detail_type_id;
	private String task_create_type;
	private String task_status;
	private String team_id;
	private int installer_id;
	private Date task_create_time;
	private Date task_invalide_time;
	private Date task_finish_time;
	private String task_finish_type;
	private String cust_id;
	private String cust_name;
	private String new_addr;
	private String old_addr;
	private String tel;
	private String mobile;
	private int done_code;
	private String county_id;
	private String area_id;
	private String remark;
	private String syn_status;
	private String visit_result;

	private String task_type_name;
	private String task_detail_type_name;
	private String task_status_text;
	
	
	
	public String getTask_id() {
		return task_id;
	}



	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}



	public String getTask_title() {
		return task_title;
	}



	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}



	public String getTask_type_id() {
		return task_type_id;
	}



	public void setTask_type_id(String task_type_id) {
		this.task_type_id = task_type_id;
	}



	public String getTask_detail_type_id() {
		return task_detail_type_id;
	}



	public void setTask_detail_type_id(String task_detail_type_id) {
		this.task_detail_type_id = task_detail_type_id;
	}



	public String getTask_create_type() {
		return task_create_type;
	}



	public void setTask_create_type(String task_create_type) {
		this.task_create_type = task_create_type;
	}



	public String getTask_status() {
		return task_status;
	}



	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}



	public String getTeam_id() {
		return team_id;
	}



	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}



	public Date getTask_create_time() {
		return task_create_time;
	}



	public void setTask_create_time(Date task_create_time) {
		this.task_create_time = task_create_time;
	}



	public Date getTask_invalide_time() {
		return task_invalide_time;
	}



	public void setTask_invalide_time(Date task_invalide_time) {
		this.task_invalide_time = task_invalide_time;
	}



	public Date getTask_finish_time() {
		return task_finish_time;
	}



	public void setTask_finish_time(Date task_finish_time) {
		this.task_finish_time = task_finish_time;
	}



	public String getTask_finish_type() {
		return task_finish_type;
	}



	public void setTask_finish_type(String task_finish_type) {
		this.task_finish_type = task_finish_type;
	}



	public String getCust_id() {
		return cust_id;
	}



	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}



	public String getCust_name() {
		return cust_name;
	}



	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}



	public String getNew_addr() {
		return new_addr;
	}



	public void setNew_addr(String new_addr) {
		this.new_addr = new_addr;
	}



	public String getOld_addr() {
		return old_addr;
	}



	public void setOld_addr(String old_addr) {
		this.old_addr = old_addr;
	}



	public String getTel() {
		return tel;
	}



	public void setTel(String tel) {
		this.tel = tel;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public int getDone_code() {
		return done_code;
	}



	public void setDone_code(int done_code) {
		this.done_code = done_code;
	}



	public String getCounty_id() {
		return county_id;
	}



	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}



	public String getArea_id() {
		return area_id;
	}



	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getSyn_status() {
		return syn_status;
	}



	public void setSyn_status(String syn_status) {
		this.syn_status = syn_status;
	}



	public String getTask_type_name() {
		return task_type_name;
	}



	public void setTask_type_name(String task_type_name) {
		this.task_type_name = task_type_name;
	}



	public String getTask_detail_type_name() {
		return task_detail_type_name;
	}



	public void setTask_detail_type_name(String task_detail_type_name) {
		this.task_detail_type_name = task_detail_type_name;
	}



	public String getTask_status_text() {
		return task_status_text;
	}



	public void setTask_status_text(String task_status_text) {
		this.task_status_text = task_status_text;
	}
	
	



	public int getInstaller_id() {
		return installer_id;
	}



	public void setInstaller_id(int installer_id) {
		this.installer_id = installer_id;
	}
	
	



	public String getVisit_result() {
		return visit_result;
	}



	public void setVisit_result(String visit_result) {
		this.visit_result = visit_result;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
}