package com.yaochen.boss.model;


import com.ycsoft.beans.core.prod.CProd;

public class CProdCycleDto  extends CProd {
	private Boolean ismax;//是否最大到期日
	private Integer public_balance;//公用账目余额
	private Double  invalid_date_num;//到期日按基准日期转换成数字
	private Double  tariff_rent_365;//按365天计算一天的资费
	private Double  exp_date_num;//失效日期转换成数字,如果exp_date为空，也设置为空
	
	private Integer tariff_billing_cycle;//资费周期
	private Double  tariff_cycle_rent_365;//一个资费周期的资费
	private Double  invalid_cycle_num;//一个资费周期天数
	private Double  backup_invlaid_num;//备份周期的基准到期日
	
	private Integer public_active_balance;//公用账目活动余额
	private String  public_acctitem_id;//公用账目ID
	private Double  public_toprod_balance;//公用账目转账金额
	private String public_acct_id;
	private String cust_no;
	private String cust_name;
	private String address;
	private String card_id;
	private String user_type;
	
	private Integer order_balance;//产品预约公用账目金额
	
	public Double getPublic_toprod_balance() {
		return public_toprod_balance;
	}
	public void setPublic_toprod_balance(Double public_toprod_balance) {
		this.public_toprod_balance = public_toprod_balance;
	}
	public Double getTariff_cycle_rent_365() {
		return tariff_cycle_rent_365;
	}
	public void setTariff_cycle_rent_365(Double tariff_cycle_rent_365) {
		this.tariff_cycle_rent_365 = tariff_cycle_rent_365;
	}
	
	public Integer getTariff_billing_cycle() {
		return tariff_billing_cycle;
	}
	public void setTariff_billing_cycle(Integer tariff_billing_cycle) {
		this.tariff_billing_cycle = tariff_billing_cycle;
	}
	public Integer getPublic_balance() {
		return public_balance;
	}
	public void setPublic_balance(Integer public_balance) {
		this.public_balance = public_balance;
	}

	public Double getInvalid_date_num() {
		return invalid_date_num;
	}
	public void setInvalid_date_num(Double invalid_date_num) {
		this.invalid_date_num = invalid_date_num;
	}
	public Double getInvalid_cycle_num() {
		return invalid_cycle_num;
	}
	public void setInvalid_cycle_num(Double invalid_cycle_num) {
		this.invalid_cycle_num = invalid_cycle_num;
	}
	public Double getBackup_invlaid_num() {
		return backup_invlaid_num;
	}
	public void setBackup_invlaid_num(Double backup_invlaid_num) {
		this.backup_invlaid_num = backup_invlaid_num;
	}
	public Double getTariff_rent_365() {
		return tariff_rent_365;
	}
	public void setTariff_rent_365(Double tariff_rent_365) {
		this.tariff_rent_365 = tariff_rent_365;
	}
	public Double getExp_date_num() {
		return exp_date_num;
	}
	public void setExp_date_num(Double exp_date_num) {
		this.exp_date_num = exp_date_num;
	}
	public Boolean getIsmax() {
		return ismax;
	}
	public void setIsmax(Boolean ismax) {
		this.ismax = ismax;
	}
	public Integer getPublic_active_balance() {
		return public_active_balance;
	}
	public void setPublic_active_balance(Integer public_active_balance) {
		this.public_active_balance = public_active_balance;
	}
	public String getPublic_acctitem_id() {
		return public_acctitem_id;
	}
	public void setPublic_acctitem_id(String public_acctitem_id) {
		this.public_acctitem_id = public_acctitem_id;
	}
	public String getCust_no() {
		return cust_no;
	}
	public void setCust_no(String cust_no) {
		this.cust_no = cust_no;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getPublic_acct_id() {
		return public_acct_id;
	}
	public void setPublic_acct_id(String public_acct_id) {
		this.public_acct_id = public_acct_id;
	}
	public Integer getOrder_balance() {
		return order_balance;
	}
	public void setOrder_balance(Integer order_balance) {
		this.order_balance = order_balance;
	}

}
