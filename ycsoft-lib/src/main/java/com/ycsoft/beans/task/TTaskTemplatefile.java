/**
 * TTaskTemplatefile.java	2013/08/29
 */
 
package com.ycsoft.beans.task; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * TTaskTemplatefile -> T_TASK_TEMPLATEFILE mapping 
 */
@POJO(
	tn="T_TASK_TEMPLATEFILE",
	sn="",
	pk="")
public class TTaskTemplatefile implements Serializable {
	
	// TTaskTemplatefile all properties 

	private String temlate_id ;	
	private String task_detail_type ;	
	private String template_filename ;	
	
	/**
	 * default empty constructor
	 */
	public TTaskTemplatefile() {}
	
	
	// temlate_id getter and setter
	public String getTemlate_id(){
		return this.temlate_id ;
	}
	
	public void setTemlate_id(String temlate_id){
		this.temlate_id = temlate_id ;
	}
	
	// task_detail_type getter and setter
	public String getTask_detail_type(){
		return this.task_detail_type ;
	}
	
	public void setTask_detail_type(String task_detail_type){
		this.task_detail_type = task_detail_type ;
	}
	
	// template_filename getter and setter
	public String getTemplate_filename(){
		return this.template_filename ;
	}
	
	public void setTemplate_filename(String template_filename){
		this.template_filename = template_filename ;
	}

}