/**
 * RDeviceChange.java	2010/06/23
 */

package com.ycsoft.beans.device;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 * RDeviceChange -> R_DEVICE_CHANGE mapping
 */
public class RDeviceChangeDto extends RDeviceChange  {

	// RDeviceChange all properties

	private String cust_id;
	private String cust_no;
	private String cust_name;
	private String old_value_text;
	private String new_value_text;
	
	@Override
	public void setOld_value(String old_value) {
		super.setOld_value(old_value);
		this.old_value_text = MemoryDict.getDictName(DictKey.STATUS, old_value);
	}
	
	public void setNew_value(String new_value) {
		super.setNew_value(new_value);
		this.new_value_text =  MemoryDict.getDictName(DictKey.STATUS, new_value);;
	}
	/**
	 * default empty constructor
	 */
	public RDeviceChangeDto() {
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getCust_no() {
		return cust_no;
	}

	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	public String getOld_value_text() {
		return old_value_text;
	}



	public String getNew_value_text() {
		return new_value_text;
	}



	
}