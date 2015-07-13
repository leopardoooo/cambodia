/**
 * CAcctAcctitemInactiveHis.java	2010/07/14
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitemInactiveHis -> C_ACCT_ACCTITEM_INACTIVE_HIS mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM_INACTIVE_HIS", sn = "", pk = "")
public class CAcctAcctitemInactiveHis extends CAcctAcctitemInactive implements Serializable {

	// CAcctAcctitemInactiveHis all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6930320753990712718L;

	private Integer create_done_code;
	/**
	 * default empty constructor
	 */
	public CAcctAcctitemInactiveHis() {
	}
	public Integer getCreate_done_code() {
		return create_done_code;
	}
	public void setCreate_done_code(Integer create_done_code) {
		this.create_done_code = create_done_code;
	}

}