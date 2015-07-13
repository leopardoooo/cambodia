/**
 * WRevisitInfo.java	2013/08/23
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * WRevisitInfo -> W_REVISIT_INFO mapping 
 */
@POJO(
	tn="W_REVISIT_INFO",
	sn="",
	pk="work_id")
public class WRevisitInfo implements Serializable {
	
	// WRevisitInfo all properties 

	private String work_id ;	
	private String succeed ;	
	private String fail_cause;
	private String fail_remark ;	
	private String installer_dept ;	
	private String installer_optr ;	
	private String revisit_optr ;	
	private String satisfaction ;	
	private String satisfaction_remak ;
	private Date revisit_create_time ;	
	private Date installer_time ;	
	
	private String succeed_text;
	private String fail_cause_text;
	private String installer_dept_text;
	private String installer_optr_text;
	/**
	 * default empty constructor
	 */
	public WRevisitInfo() {}
	
	
	// work_id getter and setter
	public String getWork_id(){
		return this.work_id ;
	}
	
	public void setWork_id(String work_id){
		this.work_id = work_id ;
	}
	
	// succeed getter and setter
	public String getSucceed(){
		return this.succeed ;
	}
	
	public void setSucceed(String succeed){
		this.succeed = succeed ;
		succeed_text = MemoryDict.getDictName(DictKey.BOOLEAN,succeed);
	}
	
	public String getSucceed_text() {
		return succeed_text;
	}


	public void setSucceed_text(String succeed_text) {
		this.succeed_text = succeed_text;
	}


	// fail_remark getter and setter
	public String getFail_remark(){
		return this.fail_remark ;
	}
	
	public void setFail_remark(String fail_remark){
		this.fail_remark = fail_remark ;
	}
	
	// installer_dept getter and setter
	public String getInstaller_dept(){
		return this.installer_dept ;
	}
	
	public void setInstaller_dept(String installer_dept){
		this.installer_dept = installer_dept ;
		installer_dept_text = MemoryDict.getDictName(DictKey.DEPT, installer_dept);
	}
	
	// installer_optr getter and setter
	public String getInstaller_optr(){
		return this.installer_optr ;
	}
	
	public void setInstaller_optr(String installer_optr){
		this.installer_optr = installer_optr ;
	}
	
	// revisit_optr getter and setter
	public String getRevisit_optr(){
		return this.revisit_optr ;
	}
	
	public void setRevisit_optr(String revisit_optr){
		this.revisit_optr = revisit_optr ;
	}
	
	// satisfaction getter and setter
	public String getSatisfaction(){
		return this.satisfaction ;
	}
	
	public void setSatisfaction(String satisfaction){
		this.satisfaction = satisfaction ;
	}
	
	// satisfaction_remak getter and setter
	public String getSatisfaction_remak(){
		return this.satisfaction_remak ;
	}
	
	public void setSatisfaction_remak(String satisfaction_remak){
		this.satisfaction_remak = satisfaction_remak ;
	}


	public Date getRevisit_create_time() {
		return revisit_create_time;
	}


	public void setRevisit_create_time(Date revisit_create_time) {
		this.revisit_create_time = revisit_create_time;
	}


	public Date getInstaller_time() {
		return installer_time;
	}


	public void setInstaller_time(Date installer_time) {
		this.installer_time = installer_time;
	}


	public String getFail_cause() {
		return fail_cause;
	}


	public void setFail_cause(String fail_cause) {
		this.fail_cause = fail_cause;
		fail_cause_text = MemoryDict.getDictName(DictKey.TASK_FAILURE_CAUSE,fail_cause);
	}


	public String getFail_cause_text() {
		return fail_cause_text;
	}


	public void setFail_cause_text(String fail_cause_text) {
		this.fail_cause_text = fail_cause_text;
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
					installer_optr_text+=MemoryDict.getDictName(DictKey.OPTR, installer)+",";
				}
				installer_optr_text  = installer_optr_text.substring(0,installer_optr_text.length()-1);
			}
		}
		return installer_optr_text;
	}


	public void setInstaller_optr_text(String installer_optr_text) {
		this.installer_optr_text = installer_optr_text;
	}

}