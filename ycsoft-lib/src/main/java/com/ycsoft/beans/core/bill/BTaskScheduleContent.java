/**
 * BTaskScheduleContent.java	2011/05/27
 */
 
package com.ycsoft.beans.core.bill; 

import java.io.Serializable;
import java.util.Date;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * BTaskScheduleContent -> B_TASK_SCHEDULE_CONTENT mapping 
 */
@POJO(
	tn="B_TASK_SCHEDULE_CONTENT",
	sn="",
	pk="")
public class BTaskScheduleContent implements Serializable {
	
	// BTaskScheduleContent all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -735972354035070799L;
	private String task_code ;	
	private String serv_type ;	
	private String isbase ;
	private String isbase_text ;
	private Integer bcnt ;	
	private String notbase ;
	private String notbase_text;
	private Integer xcnt ;	
	private Date base_eff_date ;	
	private Date base_exp_date ;	
	private Date notbase_eff_date ;	
	private Date notbase_exp_date ;	
	private Integer is_real_time ;
	private String is_real_time_text ;
	private String status ;
	private String status_text;
	private String county_id ;	
	private String area_id ;
	
	private String serv_type_text;
	private String county_name;
	private String area_name;
	
	
	public String getCounty_name() {
		return county_name;
	}


	public String getArea_name() {
		return area_name;
	}


	/**
	 * default empty constructor
	 */
	public BTaskScheduleContent() {}
	
	
	// task_code getter and setter
	public String getTask_code(){
		return this.task_code ;
	}
	
	public void setTask_code(String task_code){
		this.task_code = task_code ;
	}
	
	// serv_type getter and setter
	public String getServ_type(){
		return this.serv_type ;
	}
	
	public void setServ_type(String serv_type){
		this.serv_type = serv_type ;
		serv_type_text = MemoryDict.getDictName(DictKey.SERV_ID, serv_type);
	}
	
	// isbase getter and setter
	public String getIsbase(){
		return this.isbase ;
	}
	
	public void setIsbase(String isbase){
		this.isbase = isbase ;
		isbase_text = "y".equalsIgnoreCase(isbase)?"停":"不停";
	}
	
	// bcnt getter and setter
	public Integer getBcnt(){
		return this.bcnt ;
	}
	
	public void setBcnt(Integer bcnt){
		this.bcnt = bcnt ;
	}
	
	// notbase getter and setter
	public String getNotbase(){
		return this.notbase ;
	}
	
	public void setNotbase(String notbase){
		this.notbase = notbase ;
		notbase_text = "y".equalsIgnoreCase(notbase)?"停":"不停";
	}
	
	// xcnt getter and setter
	public Integer getXcnt(){
		return this.xcnt ;
	}
	
	public void setXcnt(Integer xcnt){
		this.xcnt = xcnt ;
	}
	
	public Date getBase_eff_date(){
		return this.base_eff_date ;
	}
	
	public void setBase_eff_date(Date base_eff_date){
		this.base_eff_date = base_eff_date ;
	}
	
	public Date getBase_exp_date(){
		return this.base_exp_date ;
	}
	
	public void setBase_exp_date(Date base_exp_date){
		this.base_exp_date = base_exp_date ;
	}
	
	public Date getNotbase_eff_date(){
		return this.notbase_eff_date ;
	}
	
	public void setNotbase_eff_date(Date notbase_eff_date){
		this.notbase_eff_date = notbase_eff_date ;
	}
	
	public Date getNotbase_exp_date(){
		return this.notbase_exp_date ;
	}
	
	public void setNotbase_exp_date(Date notbase_exp_date){
		this.notbase_exp_date = notbase_exp_date ;
	}
	
	// is_real_time getter and setter
	public Integer getIs_real_time(){
		return this.is_real_time ;
	}
	
	public void setIs_real_time(Integer is_real_time){
		this.is_real_time = is_real_time ;
		is_real_time_text = is_real_time == 1 ? "实时":"定时";
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
		status_text = "1".equalsIgnoreCase(status)?"有效":"失效";
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
		county_name = MemoryDict.getDictName(DictKey.COUNTY, county_id);
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
		area_name = MemoryDict.getDictName(DictKey.AREA, area_id);
	}


	public String getServ_type_text() {
		return serv_type_text;
	}

	public String getIsbase_text() {
		return isbase_text;
	}


	public String getNotbase_text() {
		return notbase_text;
	}


	public String getIs_real_time_text() {
		return is_real_time_text;
	}


	public String getStatus_text() {
		return status_text;
	}

}