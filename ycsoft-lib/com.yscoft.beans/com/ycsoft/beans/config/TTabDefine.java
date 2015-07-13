/**
 * TTabDefine.java	2012/10/11
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TTabDefine -> T_TAB_DEFINE mapping 
 */
@POJO(
	tn="T_TAB_DEFINE",
	sn="",
	pk="")
public class TTabDefine implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1289031799343637797L;
	// TTabDefine all properties 

	private String table_name ;	
	private String column_name ;	
	private String comments ;	
	private String param_name ;	
	private String rule_prop ;	
	private String data_type ;	
	private String func_name ;	
	private String field_name ;	
	private String status;
	private Date change_date;
	private String old_data;
	
	private String rule_prop_text;
	private String param_name_text;
	private String data_type_text;
	/**
	 * default empty constructor
	 */
	public TTabDefine() {}
	
	
	public String getOld_data() {
		return old_data;
	}


	public void setOld_data(String oldData) {
		old_data = oldData;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getChange_date() {
		return change_date;
	}


	public void setChange_date(Date changeDate) {
		change_date = changeDate;
	}


	public String getData_type_text() {
		return data_type_text;
	}


	public void setData_type_text(String dataTypeText) {
		this.data_type_text = dataTypeText;
	}


	public String getParam_name_text() {
		return param_name_text;
	}


	public void setParam_name_text(String paramNameText) {
		param_name_text = paramNameText;
	}


	public String getRule_prop_text() {
		return rule_prop_text;
	}


	public void setRule_prop_text(String rulePropText) {
		rule_prop_text = rulePropText;
	}


	// table_name getter and setter
	public String getTable_name(){
		return this.table_name ;
	}
	
	public void setTable_name(String table_name){
		this.table_name = table_name ;
	}
	
	// column_name getter and setter
	public String getColumn_name(){
		return this.column_name ;
	}
	
	public void setColumn_name(String column_name){
		this.column_name = column_name ;
	}
	
	// comments getter and setter
	public String getComments(){
		return this.comments ;
	}
	
	public void setComments(String comments){
		this.comments = comments ;
	}
	
	// param_name getter and setter
	public String getParam_name(){
		return this.param_name ;
	}
	
	public void setParam_name(String param_name){
		this.param_name = param_name ;
	}
	
	// rule_prop getter and setter
	public String getRule_prop(){
		return this.rule_prop ;
	}
	
	public void setRule_prop(String rule_prop){
		this.rule_prop = rule_prop ;
		rule_prop_text = MemoryDict.getDictName(DictKey.BOOLEAN, rule_prop);
	}
	
	// data_type getter and setter
	public String getData_type(){
		return this.data_type ;
	}
	
	public void setData_type(String data_type){
		this.data_type = data_type ;
		if(data_type!=null){
			if(data_type.endsWith("D")){
				data_type_text = "日期型"; 
			}else if(data_type.endsWith("F")){
				data_type_text = "函数型"; 
			}else if(data_type.endsWith("N")){
				data_type_text = "数字型"; 
			}else if(data_type.endsWith("S")){
				data_type_text = "字符串型"; 
			}else{
				data_type_text ="";
			}
		}
	}
	
	// func_name getter and setter
	public String getFunc_name(){
		return this.func_name ;
	}
	
	public void setFunc_name(String func_name){
		this.func_name = func_name ;
	}
	
	// field_name getter and setter
	public String getField_name(){
		return this.field_name ;
	}
	
	public void setField_name(String field_name){
		this.field_name = field_name ;
	}

}