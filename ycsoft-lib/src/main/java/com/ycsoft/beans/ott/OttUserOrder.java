package com.ycsoft.beans.ott;

import java.io.Serializable;

import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.NumericHelper;

/**
 * 订购记录
re.put("money", NumericHelper.changeF2Y(order.getOrder_fee().toString()));
			re.put("currency_type", "USD");
			re.put("product_id", order.getProd_id());
			re.put("product_name",order.getProd_name());
			re.put("product_fee_id",order.getTariff_id());
			re.put("user_ip", "");
			re.put("time", DateHelpe
 *
 */
public class OttUserOrder  implements Serializable  {
	
	private String money;
	private String currency_type="USD";
	private String product_id;
	private String product_name;
	private String product_fee_id;
	private String user_ip="";
	private String time;
	
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCurrency_type() {
		return currency_type;
	}
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_fee_id() {
		return product_fee_id;
	}
	public void setProduct_fee_id(String product_fee_id) {
		this.product_fee_id = product_fee_id;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
