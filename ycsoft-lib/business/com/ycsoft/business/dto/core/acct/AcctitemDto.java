/**
 *
 */
package com.ycsoft.business.dto.core.acct;

import java.util.Date;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;

/**
 * @author YC-SOFT
 *
 */
public class AcctitemDto extends CAcctAcctitem {
	/**
	 *
	 */
	private static final long serialVersionUID = 5610878486671963580L;
	private String prod_id;
	private String prod_name;
	private String prod_status;
	private String prod_sn;

	private String acctitem_type;

	private String tariff_id;
	private String tariff_name;
	private Integer tariff_rent;

	private Date invalid_date;
	private String next_tariff_id;
	private String next_tariff_name ;
	private Integer next_tariff_rent ;

	private String prod_status_text;

	private String allow_pay;
	private String allow_adjust;
	private String allow_tran;		//暂时只针对公用账目
	private Integer ownFeeNumber;		//基本包欠费天数
	
	private String is_base;
	private String acct_type;
	private String billing_type;
	private Integer billing_cycle;
	
	private String is_invalid_tariff;

	private String serv_id;
	
	private String pay_type;
	private String fee_type;
	
	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getServ_id() {
		return serv_id;
	}

	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
	}

	/**
	 * @return the is_base
	 */
	public String getIs_base() {
		return is_base;
	}

	/**
	 * @param is_base the is_base to set
	 */
	public void setIs_base(String is_base) {
		this.is_base = is_base;
	}

	/**
	 * @return the acct_type
	 */
	public String getAcct_type() {
		return acct_type;
	}

	/**
	 * @param acct_type the acct_type to set
	 */
	public void setAcct_type(String acct_type) {
		this.acct_type = acct_type;
	}

	/**
	 * @return the next_tariff_id
	 */
	public String getNext_tariff_id() {
		return next_tariff_id;
	}

	/**
	 * @param next_tariff_id the next_tariff_id to set
	 */
	public void setNext_tariff_id(String next_tariff_id) {
		this.next_tariff_id = next_tariff_id;
	}

	/**
	 * @return the next_tariff_name
	 */
	public String getNext_tariff_name() {
		return next_tariff_name;
	}

	/**
	 * @param next_tariff_name the next_tariff_name to set
	 */
	public void setNext_tariff_name(String next_tariff_name) {
		this.next_tariff_name = next_tariff_name;
	}

	public String getAcctitem_type() {
		return acctitem_type;
	}

	public void setAcctitem_type(String acctitem_type) {
		this.acctitem_type = acctitem_type;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	/**
	 * @return the prod_status
	 */
	public String getProd_status() {
		return prod_status;
	}

	/**
	 * @param prod_status the prod_status to set
	 */
	public void setProd_status(String prod_status) {
		this.prod_status = prod_status;
		prod_status_text = MemoryDict.getDictName(DictKey.STATUS, prod_status);
	}

	public String getProd_status_text() {
		return prod_status_text;
	}

	/**
	 * @return the tariff_rent
	 */
	public Integer getTariff_rent() {
		return tariff_rent;
	}

	/**
	 * @param tariff_rent the tariff_rent to set
	 */
	public void setTariff_rent(Integer tariff_rent) {
		this.tariff_rent = tariff_rent;
	}

	/**
	 * @return the next_tariff_rent
	 */
	public Integer getNext_tariff_rent() {
		return next_tariff_rent;
	}

	/**
	 * @param next_tariff_rent the next_tariff_rent to set
	 */
	public void setNext_tariff_rent(Integer next_tariff_rent) {
		this.next_tariff_rent = next_tariff_rent;
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

	public Date getInvalid_date() {
		return invalid_date;
	}

	public void setInvalid_date(Date invalid_date) {
		this.invalid_date = invalid_date;
	}

	public String getAllow_adjust() {
		if (StringHelper.isNotEmpty(allow_adjust))
			return allow_adjust;
		else 
			return SystemConstants.BOOLEAN_FALSE;
	}

	public void setAllow_adjust(String allow_adjust) {
		this.allow_adjust = allow_adjust;
	}

	public String getBilling_type() {
		return billing_type;
	}

	public void setBilling_type(String billingType) {
		billing_type = billingType;
	}

	public String getAllow_tran() {
		return allow_tran;
	}

	public void setAllow_tran(String allow_tran) {
		this.allow_tran = allow_tran;
	}

	public String getIs_invalid_tariff() {
		return is_invalid_tariff;
	}

	public void setIs_invalid_tariff(String is_invalid_tariff) {
		this.is_invalid_tariff = is_invalid_tariff;
	}

	public Integer getOwnFeeNumber() {
		return ownFeeNumber;
	}

	public void setOwnFeeNumber(Integer ownFeeNumber) {
		this.ownFeeNumber = ownFeeNumber;
	}

	public Integer getBilling_cycle() {
		return billing_cycle;
	}

	public void setBilling_cycle(Integer billing_cycle) {
		this.billing_cycle = billing_cycle;
	}

}
