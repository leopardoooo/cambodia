/**
 * CPromotionAcct.java	2010/07/26
 */

package com.ycsoft.beans.core.promotion;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CPromotionAcct -> C_PROMOTION_ACCT mapping
 */
@POJO(tn = "C_PROMOTION_ACCT", sn = "", pk = "")
public class CPromotionAcctHis implements Serializable {

	// CPromotionAcct all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -4442305200476031938L;
	private Integer create_done_code;
	private String promotion_sn;
	private String area_id;
	private String county_id;
	private String acct_id;
	private String acctitem_id;
	private Integer fee;
	private Integer months;

	/**
	 * default empty constructor
	 */
	public CPromotionAcctHis() {
	}

	public Integer getCreate_done_code() {
		return create_done_code;
	}

	public void setCreate_done_code(Integer createDoneCode) {
		create_done_code = createDoneCode;
	}
	// promotion_sn getter and setter
	public String getPromotion_sn() {
		return promotion_sn;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
	}

	// area_id getter and setter
	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	// county_id getter and setter
	public String getCounty_id() {
		return county_id;
	}

	public void setCounty_id(String county_id) {
		this.county_id = county_id;
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

	// fee getter and setter
	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}
	
	

}