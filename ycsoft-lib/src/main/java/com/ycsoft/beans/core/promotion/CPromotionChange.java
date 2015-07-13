/**
 * CPromotion.java	2010/07/26
 */

package com.ycsoft.beans.core.promotion;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;

/**
 * CPromotion -> C_PROMOTION mapping
 */
@POJO(tn = "C_PROMOTION_CHANGE", sn = "", pk = "")
public class CPromotionChange implements Serializable {


	private String change_done_code;
	private String new_promotion_sn;
	private String old_promotion_sn;
	private String first_promotion_sn;


	public String getChange_done_code() {
		return change_done_code;
	}


	public void setChange_done_code(String change_done_code) {
		this.change_done_code = change_done_code;
	}


	public String getNew_promotion_sn() {
		return new_promotion_sn;
	}


	public void setNew_promotion_sn(String new_promotion_sn) {
		this.new_promotion_sn = new_promotion_sn;
	}


	public String getOld_promotion_sn() {
		return old_promotion_sn;
	}


	public void setOld_promotion_sn(String old_promotion_sn) {
		this.old_promotion_sn = old_promotion_sn;
	}




	public String getFirst_promotion_sn() {
		return first_promotion_sn;
	}


	public void setFirst_promotion_sn(String first_promotion_sn) {
		this.first_promotion_sn = first_promotion_sn;
	}


	/**
	 * default empty constructor
	 */
	public CPromotionChange() {
	}

}