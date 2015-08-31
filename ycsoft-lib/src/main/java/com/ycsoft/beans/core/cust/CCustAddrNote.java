package com.ycsoft.beans.core.cust;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class CCustAddrNote implements Serializable  {
	private String note;
	private String note_status_type;
	private String note_status_type_text;
	
	private String cust_no;
	private String cust_name;
	
	public String getNote_status_type_text() {
		return note_status_type_text;
	}
	public void setNote_status_type_text(String note_status_type_text) {
		this.note_status_type_text = note_status_type_text;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote_status_type() {
		return note_status_type;
	}
	public void setNote_status_type(String note_status_type) {
		this.note_status_type_text=MemoryDict.getDictName(DictKey.NOTE_STATUS_TYPE, note_status_type);
		this.note_status_type = note_status_type;
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
	
	

}
