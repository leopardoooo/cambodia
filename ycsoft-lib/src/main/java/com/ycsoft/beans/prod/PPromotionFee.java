/**
 * PPromotionFee.java	2010/07/22
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * PPromotionFee -> P_PROMOTION_FEE mapping
 */
@POJO(tn = "P_PROMOTION_FEE", sn = "", pk = "")
public class PPromotionFee implements Serializable {

	// PPromotionFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5923761591797926860L;
	private String promotion_id;
	private String device_model;
	private String fee_id;
	private Integer disct_value;

	private String fee_name;
	private String fee_type;
	private String device_model_name;


	/**
	 * default empty constructor
	 */
	public PPromotionFee() {
	}

	// promotion_id getter and setter
	public String getPromotion_id() {
		return promotion_id;
	}

	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}


	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getFee_name() {
		return fee_name;
	}

	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}

	// fee_id getter and setter
	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
	}

	// dict_value getter and setter
	public Integer getDisct_value() {
		return disct_value;
	}

	public void setDisct_value(Integer disct_value) {
		this.disct_value = disct_value;
	}

	public String getDevice_model_name() {
		return device_model_name;
	}

	public void setDevice_model_name(String device_model_name) {
		this.device_model_name = device_model_name;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

}