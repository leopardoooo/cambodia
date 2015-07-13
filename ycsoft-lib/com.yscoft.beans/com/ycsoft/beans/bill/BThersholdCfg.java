/**
 * SArea.java	2010/03/07
 */

package com.ycsoft.beans.bill;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * SArea -> S_AREA mapping
 */
@POJO(tn = "bill.B_THRESHOLD_CFG", sn = "", pk = "")
public class BThersholdCfg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2420872176764423966L;
	
	private String prod_type;//产品类型 t-基本产品  f-增值产品
	private String prod_type_name;//
	public String getProd_type_name() {
		return prod_type_name;
	}
	public String getTask_name() {
		return task_name;
	}
	public String getCounty_name() {
		return county_name;
	}
	public String getArea_name() {
		return area_name;
	}
	private String task_code;//任务代码
	private String task_name;//任务代码
	private Integer threshold_day;//延后停天数
	private Integer new_threshold_day;
	private String county_id;//varchar2(8)	y			地区代码
	private String county_name;//
	private String area_id;// 	varchar2(8)	y
	private String area_name;
	
	public String getProd_type() {
		return prod_type;
	}
	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
		if(StringHelper.isNotEmpty(this.prod_type)){
			if(prod_type.equalsIgnoreCase("F")){
				prod_type_name = "增值产品";
			}else if(prod_type.equalsIgnoreCase("T")){
				prod_type_name = "基本产品";
			}
		}
	}
	public String getTask_code() {
		return task_code;
	}
	public void setTask_code(String task_code) {
		this.task_code = task_code;
		this.task_name = MemoryDict.getDictName(DictKey.JOB_TASK_CODE, task_code);
	}
	public Integer getThreshold_day() {
		return threshold_day;
	}
	public void setThreshold_day(Integer threshold_day) {
		this.threshold_day = threshold_day;
	}
	public String getCounty_id() {
		return county_id;
	}
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
		this.county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}
	public String getArea_id() {
		return area_id;
	}
	public void setArea_id(String area_id) {
		this.area_id = area_id;
		this.area_name = MemoryDict.getDictName(DictKey.AREA, area_id);
	}
	public Integer getNew_threshold_day() {
		return new_threshold_day;
	}
	public void setNew_threshold_day(Integer new_threshold_day) {
		this.new_threshold_day = new_threshold_day;
	}
}