/**
 * TProvince.java	2015/08/24
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * TProvince -> T_PROVINCE mapping 
 */
@POJO(
	tn="T_PROVINCE",
	sn="",
	pk="id")
public class TProvince implements Serializable {
	
	// TProvince all properties 

	private String id ;	
	private String name ;	
	private String description ;	
	private String task_code ;	
	private String cust_code ;	
	private String remark ;	
	private String domain_name;
	
	
	public String getDomain_name() {
		return domain_name;
	}


	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}


	/**
	 * default empty constructor
	 */
	public TProvince() {}
	
	
	// id getter and setter
	public String getId(){
		return this.id ;
	}
	
	public void setId(String id){
		this.id = id ;
	}
	
	// name getter and setter
	public String getName(){
		return this.name ;
	}
	
	public void setName(String name){
		this.name = name ;
	}
	
	// description getter and setter
	public String getDescription(){
		return this.description ;
	}
	
	public void setDescription(String description){
		this.description = description ;
	}
	
	// task_code getter and setter
	public String getTask_code(){
		return this.task_code ;
	}
	
	public void setTask_code(String task_code){
		this.task_code = task_code ;
	}
	
	// cust_code getter and setter
	public String getCust_code(){
		return this.cust_code ;
	}
	
	public void setCust_code(String cust_code){
		this.cust_code = cust_code ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}

}