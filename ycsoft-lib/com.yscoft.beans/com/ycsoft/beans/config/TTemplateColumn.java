/**
 * TTemplateColumn.java	2013/01/14
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TTemplateColumn -> T_TEMPLATE_COLUMN mapping 
 */
@POJO(
	tn="T_TEMPLATE_COLUMN",
	sn="SEQ_COLUMN_ID",
	pk="COLUMN_ID")
public class TTemplateColumn implements Serializable {
	
	// TTemplateColumn all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -7480266564114342619L;
	private Integer column_id ;	
	private String template_id ;	
	private String column_name ;	
	private String column_text ;	
	private String type ;	
	private String is_editable ;	
	private Integer min_value ;	
	private Integer default_value ;	
	private Integer max_value ;	
	private String item_key ;	
	private String item_key_text;
	private String select_value ;	
	private String optr_id ;	
	private String fee_std_id;
	private Date create_time;
	
	private String select_value_text;
	
	/**
	 * default empty constructor
	 */
	public TTemplateColumn() {}
	
	
	// column_id getter and setter
	public Integer getColumn_id(){
		return this.column_id ;
	}
	
	public void setColumn_id(Integer column_id){
		this.column_id = column_id ;
	}
	
	// template_id getter and setter
	public String getTemplate_id(){
		return this.template_id ;
	}
	
	public void setTemplate_id(String template_id){
		this.template_id = template_id ;
	}
	
	// column_name getter and setter
	public String getColumn_name(){
		return this.column_name ;
	}
	
	public void setColumn_name(String column_name){
		this.column_name = column_name ;
	}
	
	// column_text getter and setter
	public String getColumn_text(){
		return this.column_text ;
	}
	
	public void setColumn_text(String column_text){
		this.column_text = column_text ;
	}
	
	// type getter and setter
	public String getType(){
		return this.type ;
	}
	
	public void setType(String type){
		this.type = type ;
	}
	
	// is_editable getter and setter
	public String getIs_editable(){
		return this.is_editable ;
	}
	
	public void setIs_editable(String is_editable){
		this.is_editable = is_editable ;
	}
	
	// min_value getter and setter
	public Integer getMin_value(){
		return this.min_value ;
	}
	
	public void setMin_value(Integer min_value){
		this.min_value = min_value ;
	}
	
	// default_value getter and setter
	public Integer getDefault_value(){
		return this.default_value ;
	}
	
	public void setDefault_value(Integer default_value){
		this.default_value = default_value ;
	}
	
	// max_value getter and setter
	public Integer getMax_value(){
		return this.max_value ;
	}
	
	public void setMax_value(Integer max_value){
		this.max_value = max_value ;
	}
	
	// item_key getter and setter
	public String getItem_key(){
		return this.item_key ;
	}
	
	public void setItem_key(String item_key){
		this.item_key = item_key ;
	}
	
	// select_value getter and setter
	public String getSelect_value(){
		return this.select_value ;
	}
	
	public void setSelect_value(String select_value){
		this.select_value = select_value ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}


	public String getFee_std_id() {
		return fee_std_id;
	}


	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public String getSelect_value_text() {
		if(StringHelper.isNotEmpty(this.select_value)){
			if(this.select_value.equals("ALL")) return "所有";
			StringBuilder text = new StringBuilder();
			if(StringHelper.isNotEmpty(this.item_key)){
				List<String> selectValues = Arrays.asList(this.select_value.split(","));
				List<SItemvalue> items = MemoryDict.getDicts(this.getItem_key());
				for(SItemvalue itemValue : items){
					if(selectValues.contains(itemValue.getItem_value())){
						text.append(itemValue.getItem_name()).append(",");
					}
				}
			}
			String str = text.toString();
			if(str.length()>0){
				str = str.substring(0, str.lastIndexOf(","));
			}
			return str;
		}
		return "";
	}


	public String getItem_key_text() {
		return item_key_text;
	}


	public void setItem_key_text(String item_key_text) {
		this.item_key_text = item_key_text;
	}

}