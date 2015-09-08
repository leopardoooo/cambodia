/**
 * SItemvalue.java	2010/03/08
 */

package com.ycsoft.beans.system;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * SItemvalue -> S_ITEMVALUE mapping
 */
@POJO(tn = "S_ITEMVALUE", sn = "", pk = "")
public class SItemvalue extends SItemDefine implements Serializable {

	// SItemvalue all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -3677627579948750345L;
	private String item_key;
	private String item_name;
	private String item_value;
	private Integer item_idx;
	private String show_county_id;
	
	private String show_county_text;
	
	/**
	 * default empty constructor
	 */
	public SItemvalue() {
	}

	public SItemvalue(String item_name, String item_value) {
		this.item_name = item_name;
		this.item_value = item_value;
	}

	// item_key getter and setter
	@Override
	public String getItem_key() {
		return item_key;
	}

	@Override
	public void setItem_key(String item_key) {
		this.item_key = item_key;
	}

	// item_name getter and setter
	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	// item_value getter and setter
	public String getItem_value() {
		return item_value;
	}

	public void setItem_value(String item_value) {
		this.item_value = item_value;
	}

	public Integer getItem_idx() {
		return item_idx;
	}

	public void setItem_idx(Integer item_idx) {
		this.item_idx = item_idx;
	}

	public String getShow_county_id() {
		return show_county_id;
	}

	public void setShow_county_id(String show_county_id) {
		this.show_county_id = show_county_id;
		if(StringHelper.isNotEmpty(this.show_county_id)){
			String[] countyIds = this.show_county_id.split(",");
			if(countyIds.length == 1){
				this.show_county_text = MemoryDict.getDictName(DictKey.COUNTY, show_county_id);
			}else{
				show_county_text = "";
				for(String c : countyIds){
					show_county_text += MemoryDict.getDictName(DictKey.COUNTY, c)+",";
				}
				show_county_text = show_county_text.substring(0, show_county_text.length()-1);
			}
		}
	}

	public String getShow_county_text() {
		return show_county_text;
	}
	
}