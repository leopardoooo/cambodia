/**
 * CAcctItemChange.java	2010/07/12
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctItemChange -> C_ACCT_ITEM_CHANGE mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_CHANGE", sn = "SEQ_ACCT_CHANGE_SN", pk = "ACCT_CHANGE_SN")
public class CAcctAcctitemChange extends BusiBase implements Serializable {

	// CAcctItemChange all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2231221623709640987L;
	private Date done_date;
	private String cust_id;
	private String acct_id;
	private String acctitem_id;
	private String fee_type;
	private String change_type;
	private Integer fee;
	private Integer change_fee;
	private Integer pre_fee;
	private String billing_cycle_id;
	private Integer inactive_done_code;
	private String cometype;
	private String acct_change_sn;

	private String fee_type_text;
	private String change_type_text;
	private String acctitem_name;
	private String start_acctitem;
	private String end_acctitem;

	/**
	 * @return the fee_type_text
	 */
	
	public String getFee_type_text() {
		return fee_type_text;
	}

	public String getAcct_change_sn() {
		return acct_change_sn;
	}

	public void setAcct_change_sn(String acct_change_sn) {
		this.acct_change_sn = acct_change_sn;
	}


	/**
	 * @return the change_type_text
	 */
	public String getChange_type_text() {
		return change_type_text;
	}

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemChange() {
	}

	// done_date getter and setter
	public Date getDone_date() {
		return done_date;
	}

	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	// fee_type getter and setter
	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
		this.fee_type = fee_type;
	}

	// change_type getter and setter
	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		change_type_text = MemoryDict.getDictName(DictKey.ACCT_CHANGE_TYPE,
				change_type);
		this.change_type = change_type;
	}

	// fee getter and setter
	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	// change_fee getter and setter
	public Integer getChange_fee() {
		return change_fee;
	}

	public void setChange_fee(Integer change_fee) {
		this.change_fee = change_fee;
	}

	// pre_fee getter and setter
	public Integer getPre_fee() {
		return pre_fee;
	}

	public void setPre_fee(Integer pre_fee) {
		this.pre_fee = pre_fee;
	}

	/**
	 * @return the billing_cycle_id
	 */
	public String getBilling_cycle_id() {
		return billing_cycle_id;
	}

	/**
	 * @param billing_cycle_id the billing_cycle_id to set
	 */
	public void setBilling_cycle_id(String billing_cycle_id) {
		this.billing_cycle_id = billing_cycle_id;
	}

	public String getAcctitem_name() {
		return acctitem_name;
	}

	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}

	public String getStart_acctitem() {
		return start_acctitem;
	}

	public void setStart_acctitem(String startAcctitem) {
		start_acctitem = startAcctitem;
	}

	public String getEnd_acctitem() {
		return end_acctitem;
	}

	public void setEnd_acctitem(String endAcctitem) {
		end_acctitem = endAcctitem;
	}

	public Integer getInactive_done_code() {
		return inactive_done_code;
	}

	public void setInactive_done_code(Integer inactive_done_code) {
		this.inactive_done_code = inactive_done_code;
	}

	public String getCometype() {
		return cometype;
	}

	public void setCometype(String cometype) {
		this.cometype = cometype;
	}
	
}