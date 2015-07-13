package com.ycsoft.beans.core.job;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(tn="J_PROD_PREOPEN",sn = "", pk = "job_id")
public class JProdPreopen implements Serializable{
	private String job_id;
	private String done_code;
	private String prod_sn;
	private Date pre_open_time;
	private String county_id;
	private String area_id;
	private Integer jobstep;
	private String is_success;
	private String remark;
	public String getIs_success() {
		return is_success;
	}
	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public JProdPreopen() {
		super();
	}
	
	public JProdPreopen(String jobId, String doneCode, String prodSn,
			Date preOpenTime, String areaId, String countyId) {
		super();
		job_id = jobId;
		done_code = doneCode;
		prod_sn = prodSn;
		pre_open_time = preOpenTime;
		area_id = areaId;
		county_id = countyId;
	}



	public String getJob_id() {
		return job_id;
	}
	public void setJob_id(String jobId) {
		job_id = jobId;
	}
	public String getDone_code() {
		return done_code;
	}
	public void setDone_code(String doneCode) {
		done_code = doneCode;
	}
	public String getProd_sn() {
		return prod_sn;
	}
	public void setProd_sn(String prodSn) {
		prod_sn = prodSn;
	}
	public Date getPre_open_time() {
		return pre_open_time;
	}
	public void setPre_open_time(Date preOpenTime) {
		pre_open_time = preOpenTime;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String countyId) {
		county_id = countyId;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String areaId) {
		area_id = areaId;
	}

	public Integer getJobstep() {
		return jobstep;
	}

	public void setJobstep(Integer jobstep) {
		this.jobstep = jobstep;
	}
}
