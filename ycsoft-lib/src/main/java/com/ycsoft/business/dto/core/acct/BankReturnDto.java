package com.ycsoft.business.dto.core.acct;

import java.util.Date;

import com.ycsoft.beans.core.bank.CBankReturn;

public class BankReturnDto extends CBankReturn {
	private String file_no;
	private String cust_id;
	private String cust_name;
	private String acct_id;
	private String acctitem_id;
	private String bill_sn;
	private Date start_date;
	private Date end_date;
	private String bank_fee_name;
	private Integer fee;
	private String county_id;
	private String area_id;
	private String user_id;
	private String prod_sn;

	/**
	 * @return the file_no
	 */
	public String getFile_no() {
		return file_no;
	}

	/**
	 * @param file_no
	 *            the file_no to set
	 */
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}

	/**
	 * @return the cust_id
	 */
	public String getCust_id() {
		return cust_id;
	}

	/**
	 * @param cust_id
	 *            the cust_id to set
	 */
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	/**
	 * @return the cust_name
	 */
	public String getCust_name() {
		return cust_name;
	}

	/**
	 * @param cust_name
	 *            the cust_name to set
	 */
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}

	/**
	 * @return the acct_id
	 */
	public String getAcct_id() {
		return acct_id;
	}

	/**
	 * @param acct_id
	 *            the acct_id to set
	 */
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	/**
	 * @return the acctitem_id
	 */
	public String getAcctitem_id() {
		return acctitem_id;
	}

	/**
	 * @param acctitem_id
	 *            the acctitem_id to set
	 */
	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	/**
	 * @return the bill_sn
	 */
	public String getBill_sn() {
		return bill_sn;
	}

	/**
	 * @param bill_sn
	 *            the bill_sn to set
	 */
	public void setBill_sn(String bill_sn) {
		this.bill_sn = bill_sn;
	}

	/**
	 * @return the start_date
	 */
	public Date getStart_date() {
		return start_date;
	}

	/**
	 * @param start_date
	 *            the start_date to set
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return the end_date
	 */
	public Date getEnd_date() {
		return end_date;
	}

	/**
	 * @param end_date
	 *            the end_date to set
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return the bank_fee_name
	 */
	public String getBank_fee_name() {
		return bank_fee_name;
	}

	/**
	 * @param bank_fee_name
	 *            the bank_fee_name to set
	 */
	public void setBank_fee_name(String bank_fee_name) {
		this.bank_fee_name = bank_fee_name;
	}

	/**
	 * @return the fee
	 */
	public Integer getFee() {
		return fee;
	}

	/**
	 * @param fee
	 *            the fee to set
	 */
	public void setFee(Integer fee) {
		this.fee = fee;
	}

	/**
	 * @return the county_id
	 */
	public String getCounty_id() {
		return county_id;
	}

	/**
	 * @param county_id
	 *            the county_id to set
	 */
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

	/**
	 * @return the area_id
	 */
	public String getArea_id() {
		return area_id;
	}

	/**
	 * @param area_id
	 *            the area_id to set
	 */
	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id
	 *            the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the prod_sn
	 */
	public String getProd_sn() {
		return prod_sn;
	}

	/**
	 * @param prod_sn
	 *            the prod_sn to set
	 */
	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}
}
