package com.ycsoft.business.dto.core.prod;

import com.ycsoft.beans.core.promotion.CPromotionAcct;

public class CPromotionAcctDto extends CPromotionAcct {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5159062723979379342L;
	private String prod_name;		//产品名称
	private String necessary;		//是否必选
	private String promotion_id;	//促销编号
	private String tariff_name;
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getNecessary() {
		return necessary;
	}
	public void setNecessary(String necessary) {
		this.necessary = necessary;
	}
	public String getPromotion_id() {
		return promotion_id;
	}
	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}

}
