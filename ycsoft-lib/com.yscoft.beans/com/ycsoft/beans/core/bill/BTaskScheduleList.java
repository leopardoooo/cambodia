/**
 * BBill.java	2010/11/03
 */

package com.ycsoft.beans.core.bill;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * BBill -> B_BILL mapping
 */
@POJO(
	tn="B_TASK_SCHEDULE_LIST",
	sn="",
	pk="")
public class BTaskScheduleList implements Serializable {

	// BBill all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -8943713418494308156L;
	private String status;
	private String task_code ;
	private String isbase ;
	private int max_prod_num ;
	private Date eff_date ;
	private Date exp_date ;
	private String area_id ;
	private String county_id ;
	
	private String county_name;
	private String area_name;

	/**
	 * default empty constructor
	 */
	public BTaskScheduleList() {}



	// status getter and setter
	public String getStatus(){
		return status ;
	}

	public void setStatus(String status){
		this.status = status ;
	}



	// eff_date getter and setter
	public Date getEff_date(){
		return eff_date ;
	}

	public void setEff_date(Date eff_date){
		this.eff_date = eff_date ;
	}

	// exp_date getter and setter
	public Date getExp_date(){
		return exp_date ;
	}

	public void setExp_date(Date exp_date){
		this.exp_date = exp_date ;
	}



	// area_id getter and setter
	public String getArea_id(){
		return area_id ;
	}

	public void setArea_id(String area_id){
		this.area_id = area_id ;
		area_name = MemoryDict.getDictName(DictKey.AREA, area_id);
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}

	public String getTask_code() {
		return task_code;
	}

	public void setTask_code(String taskCode) {
		task_code = taskCode;
	}

	public String getIsbase() {
		return isbase;
	}



	public void setIsbase(String isbase) {
		this.isbase = isbase;
	}



	public int getMax_prod_num() {
		return max_prod_num;
	}



	public void setMax_prod_num(int maxProdNum) {
		max_prod_num = maxProdNum;
	}



	public String getCounty_name() {
		return county_name;
	}



	public String getArea_name() {
		return area_name;
	}


}