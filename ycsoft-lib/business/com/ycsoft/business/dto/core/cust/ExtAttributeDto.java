package com.ycsoft.business.dto.core.cust;

import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.store.MemoryDict;

/**
 *
 * @author hh
 */
public class ExtAttributeDto {


	private String attribute_name;
	private String attribute_value;

	public ExtAttributeDto(DoneCodeDto d) {
		attribute_name = d.getAttribute_name();
		if(SystemConstants.EXT_TAB_INPUTTYPE_COMBO.equals(d.getInput_type())){
			attribute_value = MemoryDict.getDictName( d.getParam_name() , d.getAttribute_value());
		}else{
			attribute_value = d.getAttribute_value();
		}
	}
	public String getAttribute_name() {
		return attribute_name;
	}
	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}
	public String getAttribute_value() {
		return attribute_value;
	}
	public void setAttribute_value(String attribute_value) {
		this.attribute_value = attribute_value;
	}
}
