/**
 * WWork.java	2013/08/23
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * WWork -> W_WORK mapping 
 */
@POJO(
	tn="W_WORK",
	sn="",
	pk="work_id")
public class WWork implements Serializable {
	
	// WWork all properties 

	private String work_id ;	
	private String task_type ;	
	private String create_type ;	
	private String create_done_code ;	
	private Date create_time ;	
	private String books_time ;	
	private String books_optr ;	
	private String task_status ;
	private String bug_cause;
	private String county_id;
	private String remark ;	
	private String assign_dept ;	
	private String assign_optr ;
	private Date assign_time;
	private String sync_status;
	private String books_optr_text;
	private String task_type_text;
	private String task_status_text;
	private String bug_cause_text;
	private String create_type_text;
	private String assign_dept_text;
	private String assign_optr_text;
	/**
	 * default empty constructor
	 */
	public WWork() {}
	
	
	// work_id getter and setter
	public String getWork_id(){
		return this.work_id ;
	}
	
	public void setWork_id(String work_id){
		this.work_id = work_id ;
	}
	
	// task_type getter and setter
	public String getTask_type(){
		return this.task_type ;
	}
	
	public void setTask_type(String task_type){
		this.task_type = task_type ;
		task_type_text = MemoryDict.getDictName(DictKey.TASK_TYPE, task_type);
	}
	
	// create_type getter and setter
	public String getCreate_type(){
		return this.create_type ;
	}
	
	public void setCreate_type(String create_type){
		this.create_type = create_type ;
		create_type_text = MemoryDict.getDictName(DictKey.TASK_CREATE_TYPE, create_type);
	}
	
	// create_done_code getter and setter
	public String getCreate_done_code(){
		return this.create_done_code ;
	}
	
	public void setCreate_done_code(String create_done_code){
		this.create_done_code = create_done_code ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// books_time getter and setter
	public String getBooks_time(){
		return this.books_time ;
	}
	
	public void setBooks_time(String books_time){
		this.books_time = books_time ;
	}
	
	// books_optr getter and setter
	public String getBooks_optr(){
		return this.books_optr ;
	}
	
	public void setBooks_optr(String books_optr){
		this.books_optr = books_optr ;
		books_optr_text = MemoryDict.getDictName(DictKey.OPTR, books_optr);
	}
	
	// task_status getter and setter
	public String getTask_status(){
		return this.task_status ;
	}
	
	public void setTask_status(String task_status){
		this.task_status = task_status ;
		task_status_text = MemoryDict.getDictName(DictKey.STATUS_W_TASK, task_status);
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}


	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	public String getBooks_optr_text() {
		return books_optr_text;
	}

	public void setBooks_optr_text(String books_optr_text) {
		this.books_optr_text = books_optr_text;
	}

	public String getTask_type_text() {
		return task_type_text;
	}

	public void setTask_type_text(String task_type_text) {
		this.task_type_text = task_type_text;
	}

	public String getTask_status_text() {
		return task_status_text;
	}

	public void setTask_status_text(String task_status_text) {
		this.task_status_text = task_status_text;
	}

	public String getBug_cause() {
		return bug_cause;
	}

	public void setBug_cause(String bug_cause) {
		this.bug_cause = bug_cause;
		bug_cause_text = MemoryDict.getDictName(DictKey.TASK_BUG_CAUSE, bug_cause);
	}

	public String getBug_cause_text() {
		return bug_cause_text;
	}

	public void setBug_cause_text(String bug_cause_text) {
		this.bug_cause_text = bug_cause_text;
	}

	public String getCreate_type_text() {
		return create_type_text;
	}

	public void setCreate_type_text(String create_type_text) {
		this.create_type_text = create_type_text;
	}

	public String getAssign_dept() {
		return assign_dept;
	}


	public void setAssign_dept(String assign_dept) {
		this.assign_dept = assign_dept;
		assign_dept_text = MemoryDict.getDictName(DictKey.DEPT, assign_dept);
	}

	public String getAssign_optr() {
		return assign_optr;
	}

	public void setAssign_optr(String assign_optr) {
		this.assign_optr = assign_optr;
		assign_optr_text = MemoryDict.getDictName(DictKey.OPTR, assign_optr);
	}

	public String getAssign_dept_text() {
		return assign_dept_text;
	}

	public void setAssign_dept_text(String assign_dept_text) {
		this.assign_dept_text = assign_dept_text;
	}

	public String getAssign_optr_text() {
		if(null!=this.getAssign_optr()){
			String[] installers = this.getAssign_optr().split(",");
			if(null!=installers){
				for(String installer : installers){
					assign_optr_text+=MemoryDict.getDictName(DictKey.OPTR, installer)+",";
				}
				assign_optr_text  = assign_optr_text.substring(0,assign_optr_text.length()-1);
			}
		}
		return assign_optr_text;
	}

	public void setAssign_optr_text(String assign_optr_text) {
		this.assign_optr_text = assign_optr_text;
	}

	public Date getAssign_time() {
		return assign_time;
	}


	public void setAssign_time(Date assign_time) {
		this.assign_time = assign_time;
	}


	/**
	 * @return the sync_status
	 */
	public String getSync_status() {
		return sync_status;
	}


	/**
	 * @param sync_status the sync_status to set
	 */
	public void setSync_status(String sync_status) {
		this.sync_status = sync_status;
	}

}