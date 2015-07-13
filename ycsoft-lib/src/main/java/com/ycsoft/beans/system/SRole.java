/**
 * SRole.java	2010/03/07
 */

package com.ycsoft.beans.system;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * SRole -> S_ROLE mapping
 */
@POJO(tn = "S_ROLE", sn = "SEQ_S_ROLE", pk = "ROLE_ID")
public class SRole extends CountyBase implements Serializable {

	// SRole all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 906911497502911011L;

	private String role_id ;
	private String role_type ;
	private String role_name ;
	private String role_desc ;
	private Date create_time ;
	private String creator ;
	private String sub_system_id ;
	private String data_right_type ;
	private String rule_id ;
	private String data_right_level;

	private String sub_system_text;
	private String role_type_text;
	private String data_right_type_text ;
	private String data_right_level_text;
	private String rule_name;
	private String rule_str;
	private String role_county_id;
	private String role_county_name;

	public String getRole_county_id() {
		return role_county_id;
	}


	public void setRole_county_id(String roleCountyId) {
		role_county_name = MemoryDict.getDictName(DictKey.COUNTY, roleCountyId);
		role_county_id = roleCountyId;
	}


	public String getRole_county_name() {
		return role_county_name;
	}


	public void setRole_county_name(String roleCountyName) {
		role_county_name = roleCountyName;
	}


	/**
	 * default empty constructor
	 */
	public SRole() {}


	// role_id getter and setter
	public String getRole_id(){
		return role_id ;
	}

	public void setRole_id(String role_id){
		this.role_id = role_id ;
	}

	// role_type getter and setter
	public String getRole_type(){
		return role_type ;
	}

	public void setRole_type(String role_type){
		this.role_type = role_type ;
		role_type_text = MemoryDict.getDictName(DictKey.ROLE_TYPE,
					this.role_type);
	}

	// role_name getter and setter
	public String getRole_name(){
		return role_name ;
	}

	public void setRole_name(String role_name){
		this.role_name = role_name ;
	}

	// role_desc getter and setter
	public String getRole_desc(){
		return role_desc ;
	}

	public void setRole_desc(String role_desc){
		this.role_desc = role_desc ;
	}

	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

	// creator getter and setter
	public String getCreator(){
		return creator ;
	}

	public void setCreator(String creator){
		this.creator = creator ;
	}

	// sub_system_id getter and setter
	public String getSub_system_id(){
		return sub_system_id ;
	}

	public void setSub_system_id(String sub_system_id){
		this.sub_system_id = sub_system_id ;
		sub_system_text = MemoryDict.getDictName(DictKey.SUB_SYSTEM,
				this.sub_system_id);
	}



	public String getData_right_type() {
		return data_right_type;
	}


	public void setData_right_type(String data_right_type) {
		this.data_right_type = data_right_type;
		data_right_type_text = MemoryDict.getDictName(DictKey.DATA_TYPE,
				this.data_right_type);
	}


	// rule_id getter and setter
	public String getRule_id(){
		return rule_id ;
	}

	public void setRule_id(String rule_id){
		this.rule_id = rule_id ;
	}


	public String getRole_type_text() {
		return role_type_text;
	}


	public void setRole_type_text(String role_type_text) {
		this.role_type_text = role_type_text;
	}


	public String getSub_system_text() {
		return sub_system_text;
	}


	public void setSub_system_text(String sub_system_text) {
		this.sub_system_text = sub_system_text;
	}


	public String getRule_name() {
		return rule_name;
	}


	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}


	public String getData_right_type_text() {
		return data_right_type_text;
	}


	public void setData_right_type_text(String data_right_type_text) {
		this.data_right_type_text = data_right_type_text;
	}


	public String getData_right_level() {
		return data_right_level;
	}


	public void setData_right_level(String data_right_level) {
		this.data_right_level = data_right_level;
		data_right_level_text = MemoryDict.getDictName(DictKey.SYS_LEVEL,
				this.data_right_level);
		
	}


	public String getData_right_level_text() {
		return data_right_level_text;
	}


	public void setData_right_level_text(String dataRightLevelText) {
		data_right_level_text = dataRightLevelText;
	}


	/**
	 * @return the rule_str
	 */
	public String getRule_str() {
		return rule_str;
	}


	/**
	 * @param rule_str the rule_str to set
	 */
	public void setRule_str(String rule_str) {
		this.rule_str = rule_str;
	}



}