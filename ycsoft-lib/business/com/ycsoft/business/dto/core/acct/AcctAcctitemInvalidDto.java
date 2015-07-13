/**
 *
 */
package com.ycsoft.business.dto.core.acct;

import com.ycsoft.beans.core.acct.CAcctAcctitemInvalid;

/**
 * @author YC-SOFT
 *
 */
public class AcctAcctitemInvalidDto extends CAcctAcctitemInvalid {
	/**
	 *
	 */
	private static final long serialVersionUID = 4998476630953744527L;
	private String acctitem_name;
	private String can_refund;
	
	public String getCan_refund() {
		return can_refund;
	}
	public void setCan_refund(String can_refund) {
		this.can_refund = can_refund;
	}
	public String getAcctitem_name() {
		return acctitem_name;
	}
	public void setAcctitem_name(String acctitemName) {
		acctitem_name = acctitemName;
	}


}
