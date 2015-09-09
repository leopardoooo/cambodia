package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

@POJO(
		tn="P_PROMOTION_EASY_PROD",
		sn="",
		pk="promotion_id")
public class PPromotionEasyProd implements Serializable {
	private String promotion_id ;
	private String promotion_name ;
	private String promotion_code ;
	private String promotion_type ;
	private Date eff_date ;
	private Date exp_date ;
	private String prod_id;
	private String tariff_id;
	private Integer order_cycles;
	private String optr_id ;
	private Date create_time ;
	private String remark ;
	public String getPromotion_id() {
		return promotion_id;
	}
	public void setPromotion_id(String promotion_id) {
		this.promotion_id = promotion_id;
	}
	public String getPromotion_name() {
		return promotion_name;
	}
	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}
	public String getPromotion_code() {
		return promotion_code;
	}
	public void setPromotion_code(String promotion_code) {
		this.promotion_code = promotion_code;
	}
	public String getPromotion_type() {
		return promotion_type;
	}
	public void setPromotion_type(String promotion_type) {
		this.promotion_type = promotion_type;
	}
	public Date getEff_date() {
		return eff_date;
	}
	public void setEff_date(Date eff_date) {
		this.eff_date = eff_date;
	}
	public Date getExp_date() {
		return exp_date;
	}
	public void setExp_date(Date exp_date) {
		this.exp_date = exp_date;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	public Integer getOrder_cycles() {
		return order_cycles;
	}
	public void setOrder_cycles(Integer order_cycles) {
		this.order_cycles = order_cycles;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
