package com.ycsoft.sysmanager.dto.config;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;

public class RuleDefineDto extends TRuleDefine {
	/**
	 *
	 */
	private static final long serialVersionUID = 4576588237954008963L;
	private String type_name;
	private String table_name;
	private String result_column;
	private String select_column;
	private String county_id;
	private String county_name;

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getResult_column() {
		return result_column;
	}

	public void setResult_column(String result_column) {
		this.result_column = result_column;
	}

	public String getSelect_column() {
		return select_column;
	}

	public void setSelect_column(String select_column) {
		this.select_column = select_column;
	}

	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
		if(StringHelper.isNotEmpty(county_id)){
			String[] countyIds = county_id.split(",");
			String countIdStr = "";
			for(int i=0,len=countyIds.length;i<len;i++){
				countIdStr += MemoryDict.getDictName(DictKey.COUNTY, countyIds[i])+",";
			}
			county_name = countIdStr.substring(0,countIdStr.length()-1);
		}
	}

	public String getCounty_name() {
		return county_name;
	}
}
