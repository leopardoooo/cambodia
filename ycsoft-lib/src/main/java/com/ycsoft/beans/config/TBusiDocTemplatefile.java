/**
 * TBusiDocTemplatefile.java	2010/11/23
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TBusiDocTemplatefile -> T_BUSI_DOC_TEMPLATEFILE mapping 
 */
@POJO(
	tn="T_BUSI_DOC_TEMPLATEFILE",
	sn="",
	pk="")
public class TBusiDocTemplatefile implements Serializable {
	
	// TBusiDocTemplatefile all properties 

	private String temlate_id ;	
	private String doc_type ;	
	private String print_type ;	
	private String template_filename ;	
	private String method_name ;	
	private String change_doc_type;
	
	/**
	 * default empty constructor
	 */
	public TBusiDocTemplatefile() {}
	
	
	
	public String getChange_doc_type() {
		return change_doc_type;
	}



	public void setChange_doc_type(String change_doc_type) {
		this.change_doc_type = change_doc_type;
	}



	// temlate_id getter and setter
	public String getTemlate_id(){
		return this.temlate_id ;
	}
	
	public void setTemlate_id(String temlate_id){
		this.temlate_id = temlate_id ;
	}
	
	// doc_type getter and setter
	public String getDoc_type(){
		return this.doc_type ;
	}
	
	public void setDoc_type(String doc_type){
		this.doc_type = doc_type ;
	}
	
	// print_type getter and setter
	public String getPrint_type(){
		return this.print_type ;
	}
	
	public void setPrint_type(String print_type){
		this.print_type = print_type ;
	}
	
	// template_filename getter and setter
	public String getTemplate_filename(){
		return this.template_filename ;
	}
	
	public void setTemplate_filename(String template_filename){
		this.template_filename = template_filename ;
	}
	
	// method_name getter and setter
	public String getMethod_name(){
		return this.method_name ;
	}
	
	public void setMethod_name(String method_name){
		this.method_name = method_name ;
	}



}