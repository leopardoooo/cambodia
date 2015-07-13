/**
 * CAcctAcctitemAdjust.java	2010/07/19
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemAdjust -> C_ACCT_ACCTITEM_ADJUST mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_ADJUST", sn = "", pk = "")
public class CAcctAcctitemAdjust extends BusiBase implements Serializable {

	// CAcctAcctitemAdjust all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 7066316568456499849L;
	private String acct_id;
	private String acctitem_id;
	private Integer ajust_fee;
	private String fee_type;
	private String remark;
	private String reason;
	
	private String fee_type_text;
	private String reason_text;
	

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
		this.fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
	}

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemAdjust() {
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

	// ajust_fee getter and setter
	public Integer getAjust_fee() {
		return ajust_fee;
	}

	public void setAjust_fee(Integer ajust_fee) {
		this.ajust_fee = ajust_fee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
		this.reason_text = MemoryDict.getDictName(DictKey.ADJUST_REASON, reason);
	}

	public String getReason_text() {
		return reason_text;
	}

	public String getFee_type_text() {
		return fee_type_text;
	}

}