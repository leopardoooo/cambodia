/**
 * TRuleEdit.java	2010/08/30
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TRuleEdit -> T_RULE_EDIT mapping
 */
@POJO(
	tn="T_RULE_EDIT",
	sn="",
	pk="")
public class TRuleEdit implements Serializable {

	// TRuleEdit all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4611550449119365966L;
	private String rule_id ;
	private Integer row_idx ;
	private String left_bracket ;
	private String prop_id ;
	private String operator ;
	private String prop_value ;
	private String logic ;
	private String right_barcket ;

	private String data_type;
	private String param_name;
	private String prop_name;
	private String prop_value_text;

	/**
	 * default empty constructor
	 */
	public TRuleEdit() {}


	// rule_id getter and setter
	public String getRule_id(){
		return rule_id ;
	}

	public void setRule_id(String rule_id){
		this.rule_id = rule_id ;
	}

	// row_idx getter and setter
	public Integer getRow_idx(){
		return row_idx ;
	}

	public void setRow_idx(Integer row_idx){
		this.row_idx = row_idx ;
	}

	// left_bracket getter and setter
	public String getLeft_bracket(){
		return left_bracket ;
	}

	public void setLeft_bracket(String left_bracket){
		this.left_bracket = left_bracket ;
	}

	// prop_id getter and setter
	public String getProp_id(){
		return prop_id ;
	}

	public void setProp_id(String prop_id){
		this.prop_id = prop_id ;
	}

	// operator getter and setter
	public String getOperator(){
		return operator ;
	}

	public void setOperator(String operator){
		this.operator = operator ;
	}

	// prop_value getter and setter
	public String getProp_value(){
		return prop_value ;
	}

	public void setProp_value(String prop_value){
		this.prop_value = prop_value ;
	}

	// logic getter and setter
	public String getLogic(){
		return logic ;
	}

	public void setLogic(String logic){
		this.logic = logic ;
	}

	// right_barcket getter and setter
	public String getRight_barcket(){
		return right_barcket ;
	}

	public void setRight_barcket(String right_barcket){
		this.right_barcket = right_barcket ;
	}


	public String getData_type() {
		return data_type;
	}


	public void setData_type(String data_type) {
		this.data_type = data_type;
	}


	public String getParam_name() {
		return param_name;
	}


	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}


	public String getProp_name() {
		return prop_name;
	}


	public void setProp_name(String prop_name) {
		this.prop_name = prop_name;
	}


	public String getProp_value_text() {
		return prop_value_text;
	}


	public void setProp_value_text(String prop_value_text) {
		this.prop_value_text = prop_value_text;
	}

}