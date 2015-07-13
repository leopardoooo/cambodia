/**
 * PPromotionGift.java	2010/07/22
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PPromotionGift -> P_PROMOTION_GIFT mapping
 */
@POJO(tn = "P_PROMOTION_GIFT", sn = "", pk = "")
public class PPromotionGift implements Serializable {

	// PPromotionGift all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6133563731091703269L;
	private String promotion_id;
	private String gift_type;
	private Integer amount;
	private Integer money;

	/**
	 * default empty constructor
	 */
	public PPromotionGift() {
	}

	// promotion_id getter and setter
	public String getPromotion_id() {
		return promotion_id;
	}

	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

	// gift_type getter and setter
	public String getGift_type() {
		return gift_type;
	}

	public void setGift_type(String gift_type) {
		this.gift_type = gift_type;
	}

	// amount getter and setter
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	// money getter and setter
	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

}