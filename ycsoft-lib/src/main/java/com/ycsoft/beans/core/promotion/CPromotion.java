/**
 * CPromotion.java	2010/07/26
 */

package com.ycsoft.beans.core.promotion;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CPromotion -> C_PROMOTION mapping
 */
@POJO(tn = "C_PROMOTION", sn = "SEQ_PROMOTION_SN", pk = "PROMOTION_SN")
public class CPromotion extends BusiBase implements Serializable {

	// CPromotion all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8866672010931974223L;
	private String promotion_sn;
	private String promotion_id;
	private String cust_id;
	private String user_id;
	private String status = "";
	private Integer times;
	
	private String promotion_create_sn;//更换促销的原始SN
	
	private String promotion_name;
	private String status_text;

	/**
	 * default empty constructor
	 */
	public CPromotion() {
	}

	// promotion_sn getter and setter
	public String getPromotion_sn() {
		return promotion_sn;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
	}

	// promotion_id getter and setter
	public String getPromotion_id() {
		return promotion_id;
	}

	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	public String getPromotion_name() {
		return promotion_name;
	}

	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPromotion_create_sn() {
		return promotion_create_sn;
	}

	public void setPromotion_create_sn(String promotion_create_sn) {
		this.promotion_create_sn = promotion_create_sn;
	}

}