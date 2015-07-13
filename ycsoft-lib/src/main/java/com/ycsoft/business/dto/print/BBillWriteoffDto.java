package com.ycsoft.business.dto.print;

import com.ycsoft.beans.core.bill.BBillWriteoff;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class BBillWriteoffDto extends BBillWriteoff {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String acctitem_name;
	private String acctitem_type;
	private String acctitem_type_text;
	private String busi_name;
	private String billing_cycle_id;
	private String min_cycle_id;
	private String max_cycle_id;
	private String serv_id;
	public String getServ_id() {
		return serv_id;
	}
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
	}
	/**
	 * @return the busi_name
	 */
	public String getBusi_name() {
		busi_name = MemoryDict.getDictName(DictKey.BUSI_CODE, this.getBusi_code());
		return busi_name;
	}
	public String getAcctitem_name() {
		return acctitem_name;
	}
	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}
	/**
	 * @return the acctitem_type
	 */
	public String getAcctitem_type() {
		return acctitem_type;
	}
	/**
	 * @param acctitem_type the acctitem_type to set
	 */
	public void setAcctitem_type(String acctitem_type) {
		acctitem_type_text = MemoryDict.getDictName(DictKey.ACCTITEM_TYPE, acctitem_type);
		this.acctitem_type = acctitem_type;
	}
	/**
	 * @return the acctitem_type_text
	 */
	public String getAcctitem_type_text() {
		return acctitem_type_text;
	}
	public String getBilling_cycle_id() {
		return billing_cycle_id;
	}
	public void setBilling_cycle_id(String billing_cycle_id) {
		this.billing_cycle_id = billing_cycle_id;
	}
	public String getMin_cycle_id() {
		return min_cycle_id;
	}
	public void setMin_cycle_id(String min_cycle_id) {
		this.min_cycle_id = min_cycle_id;
	}
	public String getMax_cycle_id() {
		return max_cycle_id;
	}
	public void setMax_cycle_id(String max_cycle_id) {
		this.max_cycle_id = max_cycle_id;
	}
	
	

}
