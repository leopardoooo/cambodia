package com.ycsoft.beans.core.common;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;

public class PropertyChange extends BusiBase {
	/**
	 *
	 */
	private static final long serialVersionUID = 1834107077755159588L;
	private String column_name;
	private String old_value;
	private String new_value;
	private Date change_time;

	private String param_name;
	private String column_name_text;
	private String old_value_text;
	private String new_value_text;
	
	/**
	 * 如果变更的属性是字典里的数据，根据属性名从字典里取数据对应的汉字.
	 * @param paramName
	 */
	public void fillPropertyChineseText(String paramName){
		if(StringHelper.isEmpty(paramName)){
			this.old_value_text = this.old_value;
			this.new_value_text = this.new_value;
		}else{
			this.old_value_text = MemoryDict.getDictName(paramName, this.old_value);
			this.new_value_text = MemoryDict.getDictName(paramName, this.new_value);
		}
	}
	
	/**
	 * 如果变更的属性是字典里的数据，根据属性名从字典里取数据对应的汉字.默认使用column_name的大写作为属性名.
	 */
	public void fillPropertyChineseText(){
		String paramName = this.column_name.toUpperCase();
		List<SItemvalue> dicts = MemoryDict.getDicts(paramName);
		if(CollectionHelper.isEmpty(dicts)){
			paramName = null;
		}
		fillPropertyChineseText(paramName);
	}
	
	// column_name getter and setter
	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	// old_value getter and setter
	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}

	// new_value getter and setter
	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	// change_time getter and setter
	public Date getChange_time() {
		return change_time;
	}

	public void setChange_time(Date change_time) {
		this.change_time = change_time;
	}

	public String getColumn_name_text() {
		return column_name_text;
	}

	public void setColumn_name_text(String column_name_text) {
		this.column_name_text = column_name_text;
	}

	public String getOld_value_text() {
		return old_value_text;
	}

	public void setOld_value_text(String old_value_text) {
		this.old_value_text = old_value_text;
	}

	public String getNew_value_text() {
		return new_value_text;
	}

	public void setNew_value_text(String new_value_text) {
		this.new_value_text = new_value_text;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

}
