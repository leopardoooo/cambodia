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
	tn="S_DEPT_BUSICODE",
	sn="",
	pk="")
public class SDeptBusicode implements Serializable {
	
	// SDeptAddr all properties 

	private String dept_id ;
	private String dept_name;
	private String busi_code;
	private String busi_name;
	private String bind_type;
	public String getDept_id() {
		return dept_id;
	}
	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
	}
	public String getBind_type() {
		return bind_type;
	}
	public void setBind_type(String bind_type) {
		this.bind_type = bind_type;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getBusi_name() {
		return busi_name;
	}
	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}	
}