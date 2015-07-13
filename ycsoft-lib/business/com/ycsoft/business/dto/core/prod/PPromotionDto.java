package com.ycsoft.business.dto.core.prod;

import java.util.List;

import com.ycsoft.beans.prod.PPromotion;
import com.ycsoft.beans.prod.PPromotionAcct;

public class PPromotionDto extends PPromotion{
	/**
	 *
	 */
	private static final long serialVersionUID = 3009449916229239149L;
	private String county_id;
	List<PPromotionAcct> acctList ;

	public List<PPromotionAcct> getAcctList() {
		return acctList;
	}

	public void setAcctList(List<PPromotionAcct> acctList) {
		this.acctList = acctList;
	}

	/**
	 * @return the county_id
	 */
	public String getCounty_id() {
		return county_id;
	}

	/**
	 * @param county_id the county_id to set
	 */
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}

}
