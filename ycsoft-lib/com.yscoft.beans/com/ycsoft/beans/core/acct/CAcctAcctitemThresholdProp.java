/**
 * CAcctAcctitemThresholdProp.java	2011/08/29
 */
 
package com.ycsoft.beans.core.acct; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * CAcctAcctitemThresholdProp -> C_ACCT_ACCTITEM_THRESHOLD_PROP mapping 
 */
@POJO(
	tn="C_ACCT_ACCTITEM_THRESHOLD_PROP",
	sn="",
	pk="")
public class CAcctAcctitemThresholdProp extends OptrBase {
	
	// CAcctAcctitemThresholdProp all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 6286423497853132636L;
	private Integer done_code ;	
	private String acct_id ;	
	private String acctitem_id ;
	private String task_code;
	private String column_name ;	
	private String old_value ;	
	private String new_value ;	
	private Date change_time ;	
	
	private String task_code_text;
	
	/**
	 * default empty constructor
	 */
	public CAcctAcctitemThresholdProp() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// acct_id getter and setter
	public String getAcct_id(){
		return this.acct_id ;
	}
	
	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}
	
	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return this.acctitem_id ;
	}
	
	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}
	
	// column_name getter and setter
	public String getColumn_name(){
		return this.column_name ;
	}
	
	public void setColumn_name(String column_name){
		this.column_name = column_name ;
	}
	
	// old_value getter and setter
	public String getOld_value(){
		return this.old_value ;
	}
	
	public void setOld_value(String old_value){
		this.old_value = old_value ;
	}
	
	// new_value getter and setter
	public String getNew_value(){
		return this.new_value ;
	}
	
	public void setNew_value(String new_value){
		this.new_value = new_value ;
	}
	
	// change_time getter and setter
	public Date getChange_time(){
		return this.change_time ;
	}
	
	public void setChange_time(Date change_time){
		this.change_time = change_time ;
	}
	
	public String getTask_code() {
		return task_code;
	}


	public void setTask_code(String task_code) {
		this.task_code = task_code;
		this.task_code_text = MemoryDict.getDictName(DictKey.JOB_TASK_CODE, task_code);
	}


	public String getTask_code_text() {
		return task_code_text;
	}

}