/**
 *
 */
package com.ycsoft.business.dto.core.acct;

import com.ycsoft.beans.core.acct.CAcctAcctitemActive;

/**
 * @author YC-SOFT
 *
 */
public class AcctAcctitemActiveDto extends CAcctAcctitemActive {
	/**
	 *
	 */
	private static final long serialVersionUID = 4998476630953744527L;
	private String can_trans;
	private String can_refund;
	private String is_cash;
	private Integer priority;
	public String getCan_trans() {
		return can_trans;
	}
	public void setCan_trans(String can_trans) {
		this.can_trans = can_trans;
	}
	public String getCan_refund() {
		return can_refund;
	}
	public void setCan_refund(String can_refund) {
		this.can_refund = can_refund;
	}
	public String getIs_cash() {
		return is_cash;
	}
	public void setIs_cash(String is_cash) {
		this.is_cash = is_cash;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}


}
