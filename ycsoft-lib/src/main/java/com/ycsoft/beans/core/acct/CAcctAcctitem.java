/**
 * CAcctAcctitem.java	2010/06/08
 */

package com.ycsoft.beans.core.acct;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.CountyBase;
import com.ycsoft.daos.config.POJO;

/**
 * CAcctAcctitem -> C_ACCT_ACCTITEM mapping
 */
@POJO(tn = "C_ACCT_ACCTITEM", sn = "", pk = "")
public class CAcctAcctitem extends CountyBase implements Serializable {

	// CAcctAcctitem all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 770965407651493354L;
	private String acct_id;
	private String acctitem_id;
	private Integer active_balance = 0;
	private Integer owe_fee = 0;
	private Integer real_fee = 0;
	private Integer real_bill = 0;
	private Integer order_balance = 0;
	private Integer real_balance = 0;
	private Integer can_trans_balance = 0;
	private Integer can_refund_balance = 0;//如果产品为不可退款，则设为0
	private Integer can_refund_balance_norule=0;//没有任何规则情况下的可退资金
	private Integer inactive_balance = 0;
	private Integer can_trans_atod=0;//模拟转数时取的可转金额
	private Date open_time;
	private String is_zero_tariff;// 是否零资费


	private String acctitem_name;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitem() {
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

	// active_balance getter and setter
	public Integer getActive_balance() {
		return active_balance==null?0:active_balance;
	}

	public void setActive_balance(Integer active_balance) {
		this.active_balance = active_balance;
	}

	// owe_fee getter and setter
	public Integer getOwe_fee() {
		return owe_fee;
	}

	public void setOwe_fee(Integer owe_fee) {
		this.owe_fee = owe_fee;
	}

	// real_fee getter and setter
	public Integer getReal_fee() {
		return real_fee;
	}

	public void setReal_fee(Integer real_fee) {
		this.real_fee = real_fee;
	}

	// real_bill getter and setter
	public Integer getReal_bill() {
		return real_bill;
	}

	public void setReal_bill(Integer real_bill) {
		this.real_bill = real_bill;
	}

	// order_balance getter and setter
	public Integer getOrder_balance() {
		return order_balance;
	}

	public void setOrder_balance(Integer order_balance) {
		this.order_balance = order_balance;
	}

	// real_balance getter and setter
	public Integer getReal_balance() {
		return active_balance - owe_fee- real_bill +(order_balance<0?order_balance:0);
	}

	public void setReal_balance(Integer real_balance) {
		this.real_balance = real_balance;
	}

	// can_trans_balance getter and setter
	public Integer getCan_trans_balance() {
		int temp = active_balance - owe_fee- real_bill > can_trans_balance ? can_trans_balance
				: active_balance - owe_fee- real_bill;
		temp = temp +(order_balance<0?order_balance:0);
		return temp > 0 ? temp : 0;

	}

	public void setCan_trans_balance(Integer can_trans_balance) {
		this.can_trans_balance = can_trans_balance;
	}

	// can_back_balance getter and setter
	public Integer getCan_refund_balance() {
		int temp = active_balance - owe_fee - real_bill > can_refund_balance ? can_refund_balance
				: active_balance - owe_fee - real_bill;
		temp = temp +(order_balance<0?order_balance:0);
		return temp > 0 ? temp : 0;
	}

	public void setCan_refund_balance(Integer can_refund_balance) {
		this.can_refund_balance = can_refund_balance;
	}

	// inactive_balance getter and setter
	public Integer getInactive_balance() {
		return inactive_balance==null?0:inactive_balance;
	}

	public void setInactive_balance(Integer inactive_balance) {
		this.inactive_balance = inactive_balance;
	}

	// open_time getter and setter
	public Date getOpen_time() {
		return open_time;
	}

	public void setOpen_time(Date open_time) {
		this.open_time = open_time;
	}

	/**
	 * @return the acctitem_name
	 */
	public String getAcctitem_name() {
		return acctitem_name;
	}

	/**
	 * @param acctitem_name
	 *            the acctitem_name to set
	 */
	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}

	public String getIs_zero_tariff() {
		return is_zero_tariff;
	}

	public void setIs_zero_tariff(String is_zero_tariff) {
		this.is_zero_tariff = is_zero_tariff;
	}

	public Integer getCan_trans_atod() {
		int temp = active_balance - owe_fee- real_bill > can_trans_atod ? can_trans_atod
				: active_balance - owe_fee- real_bill;
		return temp > 0 ? temp : 0;
	}

	public void setCan_trans_atod(Integer can_trans_atod) {
		this.can_trans_atod = can_trans_atod;
	}

	public Integer getCan_refund_balance_norule() {
		return can_refund_balance_norule;
	}

	public void setCan_refund_balance_norule(Integer can_refund_balance_norule) {
		this.can_refund_balance_norule = can_refund_balance_norule;
	}

	
	
	
}