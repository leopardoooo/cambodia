/**
 * PPromotionCard.java	2010/07/22
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PPromotionCard -> P_PROMOTION_CARD mapping
 */
@POJO(tn = "P_PROMOTION_CARD", sn = "", pk = "")
public class PPromotionCard implements Serializable {

	// PPromotionCard all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5268717037121292709L;
	private String promotion_id;
	private String card_type;
	private Integer card_value;

	/**
	 * default empty constructor
	 */
	public PPromotionCard() {
	}

	// promotion_id getter and setter
	public String getPromotion_id() {
		return promotion_id;
	}

	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
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

}