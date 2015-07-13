/**
 * PPromotionAcct.java	2010/07/22
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PPromotionAcct -> P_PROMOTION_ACCT mapping
 */
@POJO(tn = "P_PROMOTION_ACCT", sn = "", pk = "")
public class PPromotionAcct implements Serializable {

	// PPromotionAcct all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4070789854230335495L;
	private String promotion_id;
	private String acctitem_id;
	private String tariff_id;
	private Integer fee;
	private Integer cycle;
	private Integer active_amount;
	private String necessary;
	private String present_type;
	private Integer present_month;
	private Integer repetition_times;

	private String tariff_name;
	private String acctitem_name;
	private String necessary_text;
	private String present_type_text;
	
	private String acct_id;//用户账目（宽带自动匹配的账目ID）

	/**
	 * default empty constructor
	 */
	public PPromotionAcct() {
	}

	// promotion_id getter and setter
	public String getPromotion_id() {
		return promotion_id;
	}

	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

	// acctitem_id getter and setter
	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// fee getter and setter
	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	// cycle getter and setter
	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	// active_amount getter and setter
	public Integer getActive_amount() {
		return active_amount;
	}

	public void setActive_amount(Integer active_amount) {
		this.active_amount = active_amount;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	public String getAcctitem_name() {
		return acctitem_name;
	}

	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}

	public String getNecessary() {
		return necessary;
	}

	public void setNecessary(String necessary) {
		this.necessary = necessary;
		necessary_text = MemoryDict.getDictName("BOOLEAN", necessary);
	}

	public String getNecessary_text() {
		return necessary_text;
	}

	public void setNecessary_text(String necessary_text) {
		this.necessary_text = necessary_text;
	}

	public String getPresent_type() {
		return present_type;
	}

	public void setPresent_type(String present_type) {
		this.present_type = present_type;
		this.present_type_text = MemoryDict.getDictName(DictKey.ACCT_PRESENT_TYPE, present_type);
	}

	public Integer getPresent_month() {
		return present_month;
	}

	public void setPresent_month(Integer present_month) {
		this.present_month = present_month;
	}

	public String getPresent_type_text() {
		return present_type_text;
	}

	public void setPresent_type_text(String present_type_text) {
		this.present_type_text = present_type_text;
	}

	public Integer getRepetition_times() {
		return repetition_times;
	}

	public void setRepetition_times(Integer repetition_times) {
		this.repetition_times = repetition_times;
	}

	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
}