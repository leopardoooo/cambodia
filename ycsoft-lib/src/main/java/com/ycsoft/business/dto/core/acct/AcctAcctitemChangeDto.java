package com.ycsoft.business.dto.core.acct;

import com.ycsoft.beans.core.acct.CAcctAcctitemChange;

public class AcctAcctitemChangeDto extends CAcctAcctitemChange {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4236502751746371562L;
	
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
