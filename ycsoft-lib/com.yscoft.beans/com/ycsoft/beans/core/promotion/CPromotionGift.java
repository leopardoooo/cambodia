/**
 * CPromotionGift.java	2010/07/26
 */

package com.ycsoft.beans.core.promotion;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CPromotionGift -> C_PROMOTION_GIFT mapping
 */
@POJO(tn = "C_PROMOTION_GIFT", sn = "", pk = "")
public class CPromotionGift implements Serializable {

	// CPromotionGift all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -6179908271168765350L;
	private String promotion_sn;
	private String gift_type;
	private Integer amount;
	private Integer money;
	private String area_id;
	private String county_id;

	/**
	 * default empty constructor
	 */
	public CPromotionGift() {
	}

	// promotion_sn getter and setter
	public String getPromotion_sn() {
		return promotion_sn;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
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