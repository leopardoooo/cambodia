package com.ycsoft.business.dto.system;

import com.ycsoft.commons.helper.CnToSpell;
import com.ycsoft.commons.helper.StringHelper;

public class OptrDto {

	private String optr_id;
	private String optr_name;
	private String attr;	//用于拼音首字母过滤
	
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getOptr_name() {
		return optr_name;
	}
	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
		attr = StringHelper.append(optr_name, "_", CnToSpell.getPinYin(optr_name),
				"_", CnToSpell.getPinYinHeadChar(optr_name));
	}
	public String getAttr() {
		return attr;
	}
}
