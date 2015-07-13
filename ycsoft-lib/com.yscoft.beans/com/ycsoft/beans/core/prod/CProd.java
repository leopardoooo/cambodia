/**
 * CProd.java	2010/07/15
 */

package com.ycsoft.beans.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CProd -> C_PROD mapping
 */
@POJO(tn = "C_PROD", sn = "SEQ_PROD_SN", pk = "PROD_SN")
public class CProd extends PProd implements Serializable {

	// CProd all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -6439375220234204620L;
	private String prod_sn;
	private String cust_id;
	private String acct_id;
	private String user_id;
	private String tariff_id;
	private String next_tariff_id;
	private String package_sn;
	private String package_id;
	private String order_type;
	private Date order_date;
	private Date billinfo_eff_date;
	private Date last_bill_date;
	private Date next_bill_date;
	private Date status_date;
	private Date notice_date;
	private Integer check_count;
	private Date check_time;
	private String stop_by_invalid_date;
	private String stop_type;
//	private String public_acctitem_type;
	private Date last_send_time;
	private Date ca_end_time;

	private String order_type_text;
	private String stop_type_text;
	
	private Date pre_open_time;//预开通时间
//	private String public_acctitem_type_text;

	/**
	 * default empty constructor
	 */
	public CProd() {
	}

	// prod_sn getter and setter
	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

	public Date getCa_end_time() {
		return ca_end_time;
	}

	public void setCa_end_time(Date caEndTime) {
		ca_end_time = caEndTime;
	}

	public Date getLast_send_time() {
		return last_send_time;
	}

	public void setLast_send_time(Date lastSendTime) {
		last_send_time = lastSendTime;
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

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// package_sn getter and setter
	public String getPackage_sn() {
		return package_sn;
	}

	public void setPackage_sn(String package_sn) {
		this.package_sn = package_sn;
	}

	// package_id getter and setter
	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	// order_type getter and setter
	public String getOrder_type() {
		return order_type;
	}

	// order_date getter and setter
	public Date getOrder_date() {
		return order_date;
	}

	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}

	// billinfo_eff_date getter and setter
	public Date getBillinfo_eff_date() {
		return billinfo_eff_date;
	}

	public void setBillinfo_eff_date(Date billinfo_eff_date) {
		this.billinfo_eff_date = billinfo_eff_date;
	}

	// last_bill_date getter and setter
	public Date getLast_bill_date() {
		return last_bill_date;
	}

	public void setLast_bill_date(Date last_bill_date) {
		this.last_bill_date = last_bill_date;
	}

	// next_bill_date getter and setter
	public Date getNext_bill_date() {
		return next_bill_date;
	}

	public void setNext_bill_date(Date next_bill_date) {
		this.next_bill_date = next_bill_date;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
		order_type_text = MemoryDict.getDictName(DictKey.USER_PROD_ORDER_TYPE,
				order_type);
	}

	public String getOrder_type_text() {
		return order_type_text;
	}

	public String getNext_tariff_id() {
		return next_tariff_id;
	}

	public void setNext_tariff_id(String next_tariff_id) {
		this.next_tariff_id = next_tariff_id;
	}

	public void setOrder_type_text(String order_type_text) {
		this.order_type_text = order_type_text;
	}

	/**
	 * @return the status_date
	 */
	public Date getStatus_date() {
		return status_date;
	}

	/**
	 * @param status_date
	 *            the status_date to set
	 */
	public void setStatus_date(Date status_date) {
		this.status_date = status_date;
	}

	public Date getNotice_date() {
		return notice_date;
	}

	public void setNotice_date(Date notice_date) {
		this.notice_date = notice_date;
	}

	public Integer getCheck_count() {
		return check_count;
	}

	public void setCheck_count(Integer check_count) {
		this.check_count = check_count;
	}

	public Date getCheck_time() {
		return check_time;
	}

	public void setCheck_time(Date check_time) {
		this.check_time = check_time;
	}

	public String getStop_by_invalid_date() {
		return stop_by_invalid_date;
	}

	public void setStop_by_invalid_date(String stop_by_invalid_date) {
		this.stop_by_invalid_date = stop_by_invalid_date;
	}

	public String getStop_type() {
		return stop_type;
	}

	public void setStop_type(String stop_type) {
		this.stop_type = stop_type;
		this.stop_type_text = MemoryDict.getDictName(DictKey.STOP_TYPE, this.stop_type);
	}

	public String getStop_type_text() {
		return stop_type_text;
	}

	public void setStop_type_text(String stop_type_text) {
		this.stop_type_text = stop_type_text;
	}

	public Date getPre_open_time() {
		return pre_open_time;
	}

	public void setPre_open_time(Date preOpenTime) {
		pre_open_time = preOpenTime;
	}

}