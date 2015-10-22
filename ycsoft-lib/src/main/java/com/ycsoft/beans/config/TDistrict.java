/**
 * TDistrict.java	2015/08/24
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * TDistrict -> T_DISTRICT mapping 
 */
@POJO(
	tn="T_DISTRICT",
	sn="SEQ_DISTRICT_ID",
	pk="district_id")
public class TDistrict implements Serializable {
	
	// TDistrict all properties 

	private String district_id ;	
	private Integer district_level ;	
	private String district_name ;	
	private String district_desc ;	
	private String parent_id ;	
	private String province_id ;	
	private String status ;	
	private Date create_time ;	
	private String remark ;	
	
	/**
	 * default empty constructor
	 */
	public TDistrict() {}
	
	
	// district_id getter and setter
	public String getDistrict_id(){
		return this.district_id ;
	}
	
	public void setDistrict_id(String district_id){
		this.district_id = district_id ;
	}
	
	// district_level getter and setter
	public Integer getDistrict_level(){
		return this.district_level ;
	}
	
	public void setDistrict_level(Integer district_level){
		this.district_level = district_level ;
	}
	
	// district_name getter and setter
	public String getDistrict_name(){
		return this.district_name ;
	}
	
	public void setDistrict_name(String district_name){
		this.district_name = district_name ;
	}
	
	// district_desc getter and setter
	public String getDistrict_desc(){
		return this.district_desc ;
	}
	
	public void setDistrict_desc(String district_desc){
		this.district_desc = district_desc ;
	}
	
	// parent_id getter and setter
	public String getParent_id(){
		return this.parent_id ;
	}
	
	public void setParent_id(String parent_id){
		this.parent_id = parent_id ;
	}
	
	// province_id getter and setter
	public String getProvince_id(){
		return this.province_id ;
	}
	
	public void setProvince_id(String province_id){
		this.province_id = province_id ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}

}