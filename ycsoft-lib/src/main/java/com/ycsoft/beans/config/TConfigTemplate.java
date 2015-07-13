/**
 * TConfigTemplate.java	2010/09/05
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;


/**
 * TConfigTemplate -> T_CONFIG_TEMPLATE mapping
 */
@POJO(
	tn="T_CONFIG_TEMPLATE",
	sn="",
	pk="")
public class TConfigTemplate extends TTemplate implements Serializable {

	// TConfigTemplate all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -4615637376893322255L;
	private String config_name ;
	private String config_value ;


	private String param_name;
	private String form_type;
	@SuppressWarnings("unused")
	private String config_value_text;

	/**
	 * default empty constructor
	 */
	public TConfigTemplate() {}

	// config_name getter and setter
	public String getConfig_name(){
		return config_name ;
	}

	public void setConfig_name(String config_name){
		this.config_name = config_name ;
	}

	// config_value getter and setter
	public String getConfig_value(){
		return config_value ;
	}


	public void setConfig_value(String config_value){
		this.config_value = config_value ;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getConfig_value_text() {
		if(getForm_type() != null){
			if(getForm_type().equals("paramcombo")){
				return MemoryDict.getDictName(param_name, config_value);
			}else if(getForm_type().equals("paramlovcombo")){
				String[] strs = config_value.split(";");
				StringBuffer buf = new StringBuffer();
				for(int i=0,len=strs.length;i<len;i++){
					buf.append(MemoryDict.getDictName(param_name, strs[i])).append(";");
				}
				return buf.toString().substring(0, buf.toString().length()-1);
			}
		}
		return "";
		
	}

	public void setConfig_value_text(String config_value_text) {
		this.config_value_text = config_value_text;
	}

	public String getForm_type() {
		return form_type;
	}

	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}

}