package com.ycsoft.report.bean;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_TASK_HIS", sn = "", pk = "")
public class RepTaskHis implements Serializable {
	private Integer task_id;
	private String task_name;
	private String rep_id;
	private String task_type;
	private String task_execday;
	private String status;
	private String optr_id;
	private String remark;
	private String keylist;
	private String exec_result;
	private Date exec_start_time;
	private Date exec_end_time;
	private String exec_query_id;
	private Date create_time;
	
	private String rep_name;
	private String status_text;
	private String optr_name;
	private String task_type_text;
	public String getRep_name() {
		return rep_name;
	}
	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}
	public String getStatus_text() {
		return status_text;
	}
	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}
	public String getOptr_name() {
		return optr_name;
	}
	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}
	public String getTask_type_text() {
		return task_type_text;
	}
	public void setTask_type_text(String task_type_text) {
		this.task_type_text = task_type_text;
	}
	public Integer getTask_id() {
		return task_id;
	}
	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	public String getTask_execday() {
		return task_execday;
	}
	public void setTask_execday(String task_execday) {
		this.task_execday = task_execday;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getKeylist() {
		return keylist;
	}
	public void setKeylist(String keylist) {
		this.keylist = keylist;
	}
	public String getExec_result() {
		return exec_result;
	}
	public void setExec_result(String exec_result) {
		this.exec_result = exec_result;
	}
	public Date getExec_start_time() {
		return exec_start_time;
	}
	public void setExec_start_time(Date exec_start_time) {
		this.exec_start_time = exec_start_time;
	}
	public Date getExec_end_time() {
		return exec_end_time;
	}
	public void setExec_end_time(Date exec_end_time) {
		this.exec_end_time = exec_end_time;
	}
	public String getExec_query_id() {
		return exec_query_id;
	}
	public void setExec_query_id(String exec_query_id) {
		this.exec_query_id = exec_query_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}
