package com.ycsoft.beans.prod;

/**
 * PPromotionCounty.java	2010/10/09
 */

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PPromotionCounty -> P_PROMOTION_COUNTY mapping
 */
@POJO(tn = "P_PROMOTION_COUNTY", sn = "", pk = "")
public class PPromotionCounty implements Serializable {

	// PPromotion all properties

	/**
	 * 
	 */
	private static final long serialVersionUID = -2921991635623784340L;
	private String promotion_id;
	private String county_id;
	private String county_name;

	/**
	 * @return the promotion_id
	 */
	public String getPromotion_id() {
		return promotion_id;
	}

	/**
	 * @param promotion_id
	 *            the promotion_id to set
	 */
	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

	/**
	 * @return the county_id
	 */
	public String getCounty_id() {
		return county_id;
	}

	/**
	 * @param county_id
	 *            the county_id to set
	 */
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
		setCounty_name(MemoryDict.getDictName(DictKey.COUNTY, county_id));
	}

	public String getCounty_name() {
		return county_name;
	}

	public void setCounty_name(String countyName) {
		county_name = countyName;
	}
}