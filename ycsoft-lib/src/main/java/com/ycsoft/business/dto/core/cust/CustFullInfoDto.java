package com.ycsoft.business.dto.core.cust;

import java.io.Serializable;

import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustBonuspoint;
import com.ycsoft.beans.core.cust.CCustLinkman;

public class CustFullInfoDto implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -3330090536027137338L;
	private CCust cust = new CCust();
	private CCustLinkman linkman;
	private CCustBonuspoint bonuspoint;
	private CAcctBank acctBank;
	
	public CAcctBank getAcctBank() {
		return acctBank;
	}
	public void setAcctBank(CAcctBank acctBank) {
		this.acctBank = acctBank;
	}
	public CCust getCust() {
		return cust;
	}
	public void setCust(CCust cust) {
		this.cust = cust;
	}
	public CCustLinkman getLinkman() {
		return linkman;
	}
	public void setLinkman(CCustLinkman linkman) {
		this.linkman = linkman;
	}
	public CCustBonuspoint getBonuspoint() {
		return bonuspoint;
	}
	public void setBonuspoint(CCustBonuspoint bonuspoint) {
		this.bonuspoint = bonuspoint;
	}
}
