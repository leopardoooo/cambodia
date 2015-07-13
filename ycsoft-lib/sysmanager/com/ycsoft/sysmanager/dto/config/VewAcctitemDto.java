package com.ycsoft.sysmanager.dto.config;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class VewAcctitemDto {

	private String acctitem_id;
	private String acctitem_name;
	private String acctitem_type;
	private String printitem_id;

	private String prod_id;
	private String prod_name;

	private String acctitem_type_text;
	public String getAcctitem_id() {
		return acctitem_id;
	}
	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}
	public String getAcctitem_name() {
		return acctitem_name;
	}
	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}
	public String getAcctitem_type() {
		return acctitem_type;
	}
	public void setAcctitem_type(String acctitem_type) {
		acctitem_type_text = MemoryDict.getDictName(DictKey.ACCT_TYPE, acctitem_type);
		this.acctitem_type = acctitem_type;
	}
	public String getPrintitem_id() {
		return printitem_id;
	}
	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}
	public String getAcctitem_type_text() {
		return acctitem_type_text;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}


}
