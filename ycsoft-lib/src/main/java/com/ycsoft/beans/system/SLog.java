package com.ycsoft.beans.system;

/**
 * SLog.java	2011/06/15
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * SLog -> S_LOG mapping 
 */
@POJO(
	tn="S_LOG",
	sn="SEQ_LOG_DONE_CODE",
	pk="done_code")
public class SLog extends OptrBase implements Serializable {
	
	// SLog all properties 

	private Integer done_code ;	
	private Date done_date ;	
	private String func_code ;	
	private String rec_id ;	
	private String rec_name;
	private String remark ;	
	
	private String func_code_text;
	
	/**
	 * default empty constructor
	 */
	public SLog() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// func_code getter and setter
	public String getFunc_code(){
		return this.func_code ;
	}
	
	public void setFunc_code(String func_code){
		this.func_code = func_code ;
		this.func_code_text = MemoryDict.getDictName(DictKey.FUNC_CODE, func_code);
	}
	
	// rec_id getter and setter
	public String getRec_id(){
		return this.rec_id ;
	}
	
	public void setRec_id(String rec_id){
		this.rec_id = rec_id ;
	}
	
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}


	public String getRec_name() {
		return rec_name;
	}


	public void setRec_name(String rec_name) {
		this.rec_name = rec_name;
	}


	public String getFunc_code_text() {
		return func_code_text;
	}


	public void setFunc_code_text(String func_code_text) {
		this.func_code_text = func_code_text;
	}
	
}