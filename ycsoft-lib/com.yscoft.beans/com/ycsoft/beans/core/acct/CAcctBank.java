/**
 * CAcctBank.java	2010/07/12
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctBank -> C_ACCT_BANK mapping
 */
@POJO(tn = "C_ACCT_BANK", sn = "", pk = "")
public class CAcctBank extends CountyBase implements Serializable {

	// CAcctBank all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1762740174066239574L;
	private String acct_id;
	private String bank_code;
	private String bank_account;
	private String account_name;
	private String cert_num;
	private Date sign_time;
	private Date unsign_time;
	private String status;
	private String bank_pay_type;
	private String cust_id;
	
	private String bank_code_text;

	public String getBank_pay_type() {
		return bank_pay_type;
	}

	public void setBank_pay_type(String bank_pay_type) {
		this.bank_pay_type = bank_pay_type;
	}

	/**
	 * default empty constructor
	 */
	public CAcctBank() {
	}

	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	// bank_code getter and setter
	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
		bank_code_text = MemoryDict.getDictName(DictKey.BANK_CODE, bank_code);
	}

	// bank_account getter and setter
	public String getBank_account() {
		return bank_account;
	}

	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}

	// account_name getter and setter
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	// cert_num getter and setter
	public String getCert_num() {
		return cert_num;
	}

	public void setCert_num(String cert_num) {
		this.cert_num = cert_num;
	}

	// sign_time getter and setter
	public Date getSign_time() {
		return sign_time;
	}

	public void setSign_time(Date sign_time) {
		this.sign_time = sign_time;
	}

	// unsign_time getter and setter
	public Date getUnsign_time() {
		return unsign_time;
	}

	public void setUnsign_time(Date unsign_time) {
		this.unsign_time = unsign_time;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getBank_code_text() {
		return bank_code_text;
	}

	public void setBank_code_text(String bank_code_text) {
		this.bank_code_text = bank_code_text;
	}

}