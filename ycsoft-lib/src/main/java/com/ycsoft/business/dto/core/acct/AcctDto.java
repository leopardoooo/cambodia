/**
 *
 */
package com.ycsoft.business.dto.core.acct;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.beans.core.user.CUser;

/**
 * @author liujiaqi
 *
 */
public class AcctDto extends CUser {

	/**
	 *
	 */
	private static final long serialVersionUID = -1629232738423959620L;
	private String acct_type;
	private String acct_type_text;
	private String allow_pay;
	private List<AcctitemDto> acctitems = new ArrayList<AcctitemDto>();

	/**
	 * @return the acct_type_text
	 */
	public String getAcct_type_text() {
		return acct_type_text;
	}

	/**
	 * @param acct_type_text
	 *            the acct_type_text to set
	 */
	public void setAcct_type_text(String acct_type_text) {
		this.acct_type_text = acct_type_text;
	}

	/**
	 * @return
	 */
	public String getAcct_type() {
		return acct_type;
	}

	/**
	 * @param acct_type
	 */
	public void setAcct_type(String acct_type) {
		this.acct_type = acct_type;
	}

	/**
	 * @return the acctitems
	 */
	public List<AcctitemDto> getAcctitems() {
		return acctitems;
	}

	/**
	 * @param acctitem
	 */
	public void addAcctitems(AcctitemDto acctitem) {
		acctitems.add(acctitem);
	}

	/**
	 * @param acctitems the acctitems to set
	 */
	public void setAcctitems(List<AcctitemDto> acctitems) {
		this.acctitems = acctitems;
	}

	/**
	 * @return the allow_pay
	 */
	public String getAllow_pay() {
		return allow_pay;
	}

	/**
	 * @param allow_pay the allow_pay to set
	 */
	public void setAllow_pay(String allow_pay) {
		this.allow_pay = allow_pay;
	}


}
