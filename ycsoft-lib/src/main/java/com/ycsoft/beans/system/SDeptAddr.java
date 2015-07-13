/**
 * SDeptAddr.java	2013/05/20
 */
 
package com.ycsoft.beans.system; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * SDeptAddr -> S_DEPT_ADDR mapping 
 */
@POJO(
	tn="S_DEPT_ADDR",
	sn="",
	pk="")
public class SDeptAddr implements Serializable {
	
	// SDeptAddr all properties 

	private String county_id ;	
	private String dept_id ;	
	private String addr_id ;	
	private Integer tree_level ;	
	
	/**
	 * default empty constructor
	 */
	public SDeptAddr() {}
	
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}
	
	// dept_id getter and setter
	public String getDept_id(){
		return this.dept_id ;
	}
	
	public void setDept_id(String dept_id){
		this.dept_id = dept_id ;
	}
	
	// addr_id getter and setter
	public String getAddr_id(){
		return this.addr_id ;
	}
	
	public void setAddr_id(String addr_id){
		this.addr_id = addr_id ;
	}
	
	// tree_level getter and setter
	public Integer getTree_level(){
		return this.tree_level ;
	}
	
	public void setTree_level(Integer tree_level){
		this.tree_level = tree_level ;
	}

}