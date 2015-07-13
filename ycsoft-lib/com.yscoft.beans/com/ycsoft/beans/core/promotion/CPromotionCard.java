/**
 * CPromotionCard.java	2010/07/26
 */

package com.ycsoft.beans.core.promotion;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CPromotionCard -> C_PROMOTION_CARD mapping
 */
@POJO(tn = "C_PROMOTION_CARD", sn = "", pk = "")
public class CPromotionCard implements Serializable {

	// CPromotionCard all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4792814894039410542L;
	private String promotion_sn;
	private String card_type;
	private Integer card_value;
	private String area_id;
	private String county_id;

	/**
	 * default empty constructor
	 */
	public CPromotionCard() {
	}

	// promotion_sn getter and setter
	public String getPromotion_sn() {
		return promotion_sn;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
	}

	// card_type getter and setter
	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	// card_value getter and setter
	public Integer getCard_value() {
		return card_value;
	}

	public void setCard_value(Integer card_value) {
		this.card_value = card_value;
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

}