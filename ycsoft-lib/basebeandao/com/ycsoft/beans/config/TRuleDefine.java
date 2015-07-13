/**
 * TRuleDefine.java	2010/08/30
 */

package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TRuleDefine -> T_RULE_DEFINE mapping
 */
@POJO(tn = "T_RULE_DEFINE", sn = "SEQ_RULE_ID", pk = "RULE_ID")
public class TRuleDefine implements Serializable {

	// TRuleDefine all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2839614348262915703L;
	private String rule_id ;
	private String rule_name ;
	private String rule_str ;
	private String remark ;
	private String rule_str_cn ;
	private String rule_type;
	private String data_type;
	private String cfg_type;
	private String pre_billing_rule;
	private String optr_id;
	private Date eff_date;
	private Date exp_date;

	private String rule_type_text;
	private String cfg_type_text;
	/**
	 * default empty constructor
	 */
	public TRuleDefine() {}


	// rule_id getter and setter
	public String getRule_id(){
		return rule_id ;
	}

	public void setRule_id(String rule_id){
		this.rule_id = rule_id ;
	}

	// rule_name getter and setter
	public String getRule_name(){
		return rule_name ;
	}

	public void setRule_name(String rule_name){
		this.rule_name = rule_name ;
	}

	// rule_str getter and setter
	public String getRule_str(){
		return rule_str ;
	}

	public void setRule_str(String rule_str){
		this.rule_str = rule_str ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}

	// rule_str_cn getter and setter
	public String getRule_str_cn(){
		if(StringHelper.isNotEmpty(rule_str_cn) && rule_str_cn.length() >= 2000)
			return rule_str_cn.substring(0, 100).concat("...");
		return rule_str_cn ;
	}

	public void setRule_str_cn(String rule_str_cn){
		this.rule_str_cn = rule_str_cn ;
	}


	public String getRule_type() {
		return rule_type;
	}


	public void setRule_type(String rule_type) {
		this.rule_type = rule_type;
		rule_type_text = MemoryDict.getDictName("RULE_TYPE", rule_type);
	}


	public String getData_type() {
		return data_type;
	}


	public void setData_type(String data_type) {
		this.data_type = data_type;
	}


	public String getCfg_type() {
		return cfg_type;
	}


	public void setCfg_type(String cfg_type) {
		this.cfg_type = cfg_type;
		cfg_type_text = MemoryDict.getDictName("RULE_CFG_TYPE", cfg_type);
	}


	public String getRule_type_text() {
		return rule_type_text;
	}


	public String getCfg_type_text() {
		return cfg_type_text;
	}


	public String getPre_billing_rule() {
		return pre_billing_rule;
	}


	public void setPre_billing_rule(String pre_billing_rule) {
		this.pre_billing_rule = pre_billing_rule;
	}


	public String getOptr_id() {
		return optr_id;
	}


	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}


	public Date getEff_date() {
		return eff_date;
	}


	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}


	public Date getExp_date() {
		return exp_date;
	}


	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}
	

}