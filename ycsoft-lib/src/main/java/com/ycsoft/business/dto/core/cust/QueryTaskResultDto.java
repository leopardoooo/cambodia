package com.ycsoft.business.dto.core.cust;

import java.sql.Date;

import com.ycsoft.beans.task.WWork;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;


public class QueryTaskResultDto extends WWork{

	private static final long serialVersionUID = 1L;
	private String cust_no;
	private String install_addr;
	private String tel ; 
	private String task_cust_name;
	private String cust_id;
	
	private String installer_dept;
	private String installer_optr;
	private Date installer_time;
	private String installer_dept_text ;	
	private String installer_optr_text ;
	private String busi_code;
	private String busi_name;
	private String optr_id;
	private String optr_name;


	
	
	
	public QueryTaskResultDto(){
	}
	
	
	
	public String getInstall_addr() {
		return install_addr;
	}



	public void setInstall_addr(String install_addr) {
		this.install_addr = install_addr;
	}



	public String getTel() {
		return tel;
	}



	public void setTel(String tel) {
		this.tel = tel;
	}



	public String getTask_cust_name() {
		return task_cust_name;
	}



	public void setTask_cust_name(String task_cust_name) {
		this.task_cust_name = task_cust_name;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public void setBusi_name(String busi_name) {
		this.busi_name = busi_name;
	}



	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}



		/**
	 * @return the cust_id
	 */
	public String getCust_id() {
		return cust_id;
	}
	/**
	 * @param cust_id the cust_id to set
	 */
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	/**
	 * @return the cust_no
	 */
	public String getCust_no() {
		return cust_no;
	}
	/**
	 * @param cust_no the cust_no to set
	 */
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	public String getBusi_code() {
		return busi_code;
	}

	public void setBusi_code(String busi_code) {
		busi_name = MemoryDict.getDictName(DictKey.BUSI_CODE, busi_code);
		this.busi_code = busi_code;
	}


	public String getBusi_name() {
		return busi_name;
	}

	public String getOptr_id() {
		return optr_id;
	}

	public void setOptr_id(String optr_id) {
		optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
		this.optr_id = optr_id;
	}

	public String getOptr_name() {
		return optr_name;
	}



	public String getInstaller_dept_text() {
		return installer_dept_text;
	}



	public void setInstaller_dept_text(String installer_dept_text) {
		this.installer_dept_text = installer_dept_text;
	}

	
	public String getInstaller_optr_text() {
		if(null!=this.getInstaller_optr()){
			String[] installers = this.getInstaller_optr().split(",");
			if(null!=installers){
				for(String installer : installers){
					String src = MemoryDict.getDictName(DictKey.OPTR, installer)+",";
					if(null != installer_optr_text){
						installer_optr_text += src;
					}else{
						installer_optr_text = src;
					}
				}
				installer_optr_text  = installer_optr_text.substring(0,installer_optr_text.length()-1);
			}
		}
		return installer_optr_text;
	}

	public void setInstaller_optr_text(String installer_optr_text) {
		this.installer_optr_text = installer_optr_text;
	}



	public String getInstaller_dept() {
		return installer_dept;
	}

	public void setInstaller_dept(String installer_dept) {
		this.installer_dept = installer_dept;
		installer_dept_text = MemoryDict.getDictName(DictKey.DEPT, installer_dept);
	}

	public String getInstaller_optr() {
		return installer_optr;
	}

	public void setInstaller_optr(String installer_optr) {
		this.installer_optr = installer_optr;
	}

	public Date getInstaller_time() {
		return installer_time;
	}

	public void setInstaller_time(Date installer_time) {
		this.installer_time = installer_time;
	}

}
