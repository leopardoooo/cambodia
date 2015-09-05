package com.ycsoft.beans.ott;

import java.io.Serializable;

/**
user_id	用户ID	
money	帐户余额	保留两位小数
currency_type	货币类型	RMB：人民币USD：美元等
user_name	用户名称（用于友好显示）	
user_rank	用户等级	0：普通用户  默认 
1：VIP用户
telephone	手机号码	
customer_code	客户编号	每个用户唯一
用于wing充值时显示在界面上
**/
public class OttAccount implements Serializable  {
	private String user_id;
	private String money;
	private String currency_type="USD";
	private String user_name;
	private String user_rank="0";
	private String telephone;
	private String customer_code;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
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
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_rank() {
		return user_rank;
	}
	public void setUser_rank(String user_rank) {
		this.user_rank = user_rank;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCustomer_code() {
		return customer_code;
	}
	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}
	
}
