package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

@POJO(tn = "REP_CUBE", sn = "", pk = "")
public class RepCube implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5029719078080437247L;
	private String rep_id;
	private String column_code;
	private String column_type;
	private String column_type_check;//粒度的可编辑和纵向维的可固定
	private String column_define;
	private String column_as;
	private String mea_detail_id;//指标明细报表ID
	private String show_control;//数据显示控制和维度层警戒配置
	
	private boolean ismea;//加载基础queryid_base缓存时，控制向数据库取数据方法getString(),getObject()
	
	public boolean isIsmea() {
		return ismea;
	}
	public void setIsmea(boolean ismea) {
		this.ismea = ismea;
	}
	public String getShow_control() {
		return show_control;
	}
	public void setShow_control(String show_control) {
		this.show_control = show_control;
	}
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getColumn_code() {
		return column_code;
	}
	public void setColumn_code(String column_code) {
		this.column_code = column_code;
	}
	public String getColumn_type() {
		return column_type;
	}
	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}
	public String getColumn_define() {
		return column_define;
	}
	public void setColumn_define(String column_define) {
		this.column_define = column_define;
	}
	public String getColumn_as() {
		return column_as;
	}
	public void setColumn_as(String column_as) {
		this.column_as = column_as;
	}
	public String getMea_detail_id() {
		return mea_detail_id;
	}
	public void setMea_detail_id(String mea_detail_id) {
		this.mea_detail_id = mea_detail_id;
	}
	public String getColumn_type_check() {
		return column_type_check;
	}
	public void setColumn_type_check(String column_type_check) {
		this.column_type_check = column_type_check;
	}
	
}
