/**
 * WCustInfo.java	2013/08/23
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * WCustInfo -> W_CUST_INFO mapping 
 */
@POJO(
	tn="W_CUST_INFO",
	sn="",
	pk="work_id")
public class WCustInfo implements Serializable {
	
	// WCustInfo all properties 

	private String work_id ;	
	private String cust_id ;	
	private String user_id ;	
	private String install_addr ;	
	private String task_cust_name ;	
	private String tel ;	
	private String old_addr ;	
	private String net_type ;	
	
	/**
	 * default empty constructor
	 */
	public WCustInfo() {}
	
	
	// work_id getter and setter
	public String getWork_id(){
		return this.work_id ;
	}
	
	public void setWork_id(String work_id){
		this.work_id = work_id ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// install_addr getter and setter
	public String getInstall_addr(){
		return this.install_addr ;
	}
	
	public void setInstall_addr(String install_addr){
		this.install_addr = install_addr ;
	}
	
	public String getTask_cust_name() {
		return task_cust_name;
	}
	
	public void setTask_cust_name(String task_cust_name) {
		this.task_cust_name = task_cust_name;
	}

	// tel getter and setter
	public String getTel(){
		return this.tel ;
	}
	
	public void setTel(String tel){
		this.tel = tel ;
	}
	
	// old_addr getter and setter
	public String getOld_addr(){
		return this.old_addr ;
	}
	
	public void setOld_addr(String old_addr){
		this.old_addr = old_addr ;
	}
	
	// net_type getter and setter
	public String getNet_type(){
		return this.net_type ;
	}
	
	public void setNet_type(String net_type){
		this.net_type = net_type ;
	}

}