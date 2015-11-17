package com.ycsoft.beans.ott;

import java.io.Serializable;
import java.util.Date;

public class TServerOttauthFee implements Serializable {
	 private String id;//
	 private String name;//	
	 private String  type;//	产品类型	0：周期性产品，如包月或者包年1：单片
	 private Integer  price;//	价格	
	 private String currency_type="USD";//	货币类型	RMB：人民币USD：美元
	 private String unit;//	单位（周期性产品的时间周期单位）	year:年month:月day:天
	 private String amount;//	数量	
	 private String pay_type;
	 private Date begin_time;//	开始时间	2014-06-19 08:59:59
	 private Date  end_time;//	结束时间	2050-06-19 08:59:59
	 private String continue_buy;// CONTINUE_BUY
	 private String explanation; //EXPLANATION

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getCurrency_type() {
		return currency_type;
	}
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public Date getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public String getContinue_buy() {
		return continue_buy;
	}
	public void setContinue_buy(String continue_buy) {
		this.continue_buy = continue_buy;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	

}
