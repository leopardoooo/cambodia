package com.ycsoft.business.dto.core.prod;

import com.ycsoft.beans.core.prod.CProd;

public class CProdDto extends CProd {
	/**
	 *
	 */
	private static final long serialVersionUID = -2590105154516096344L;
	private String is_invalid_tariff;
	private String is_zero_tariff;// 是否零资费
	private String allow_pay;// 是否可以续费
	private String tariff_name; // 产品资费名称
	private String next_tariff_name; // 产品资费名称
	private String package_name;
	private Integer tariff_rent;
	private String billing_type;
	private Integer billing_cycle;
	private Integer owe_fee;//累计欠费
	private Integer real_bill;//本月欠费
	private Integer all_balance;//余额active_balance+order_balance
	private String stb_id;
	private String card_id;
	private String modem_mac;
	private String has_dyn;				//是否有动态资源('T','F')
	private String is_pause="F";		//是否能暂停(默认不能暂停)
	private Integer inactive_balance;
	private String month_rent_cal_type;
	
	private Integer real_balance;
	private Integer active_balance;
	private Integer real_fee;
	private String p_bank_pay;
	
	public String getMonth_rent_cal_type() {
		return month_rent_cal_type;
	}

	public void setMonth_rent_cal_type(String month_rent_cal_type) {
		this.month_rent_cal_type = month_rent_cal_type;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}

	

	public String getStb_id() {
		return stb_id;
	}

	public void setStb_id(String stbId) {
		stb_id = stbId;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String cardId) {
		card_id = cardId;
	}

	public String getModem_mac() {
		return modem_mac;
	}

	public void setModem_mac(String modemMac) {
		modem_mac = modemMac;
	}

	public Integer getBilling_cycle() {
		return billing_cycle;
	}

	public void setBilling_cycle(Integer billingCycle) {
		billing_cycle = billingCycle;
	}

	public String getNext_tariff_name() {
		return next_tariff_name;
	}

	public void setNext_tariff_name(String next_tariff_name) {
		this.next_tariff_name = next_tariff_name;
	}

	public String getIs_zero_tariff() {
		return is_zero_tariff;
	}

	public void setIs_zero_tariff(String is_zero_tariff) {
		this.is_zero_tariff = is_zero_tariff;
	}

	public String getAllow_pay() {
		return allow_pay;
	}

	public void setAllow_pay(String allow_pay) {
		this.allow_pay = allow_pay;
	}

	public String getPackage_name() {
		return package_name;
	}

	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}

	public Integer getTariff_rent() {
		return tariff_rent;
	}

	public void setTariff_rent(Integer tariff_rent) {
		this.tariff_rent = tariff_rent;
	}

	public String getBilling_type() {
		return billing_type;
	}

	public String getIs_invalid_tariff() {
		return is_invalid_tariff;
	}

	public void setIs_invalid_tariff(String is_invalid_tariff) {
		this.is_invalid_tariff = is_invalid_tariff;
	}

	public void setBilling_type(String billingType) {
		billing_type = billingType;
	}

	

	public Integer getOwe_fee() {
		return owe_fee;
	}

	public void setOwe_fee(Integer owe_fee) {
		this.owe_fee = owe_fee;
	}

	public Integer getAll_balance() {
		return all_balance;
	}

	public void setAll_balance(Integer all_balance) {
		this.all_balance = all_balance;
	}

	public Integer getReal_bill() {
		return real_bill;
	}

	public void setReal_bill(Integer real_bill) {
		this.real_bill = real_bill;
	}

	public String getHas_dyn() {
		return has_dyn;
	}

	public void setHas_dyn(String has_dyn) {
		this.has_dyn = has_dyn;
	}

	public String getIs_pause() {
		return is_pause;
	}

	public void setIs_pause(String is_pause) {
		this.is_pause = is_pause;
	}

	public Integer getInactive_balance() {
		return inactive_balance;
	}

	public void setInactive_balance(Integer inactive_balance) {
		this.inactive_balance = inactive_balance;
	}

	/**
	 * @return the real_balance
	 */
	public Integer getReal_balance() {
		return real_balance;
	}

	/**
	 * @param real_balance the real_balance to set
	 */
	public void setReal_balance(Integer real_balance) {
		this.real_balance = real_balance;
	}

	/**
	 * @return the active_balance
	 */
	public Integer getActive_balance() {
		return active_balance;
	}

	/**
	 * @param active_balance the active_balance to set
	 */
	public void setActive_balance(Integer active_balance) {
		this.active_balance = active_balance;
	}

	/**
	 * @return the real_fee
	 */
	public Integer getReal_fee() {
		return real_fee;
	}

	/**
	 * @param real_fee the real_fee to set
	 */
	public void setReal_fee(Integer real_fee) {
		this.real_fee = real_fee;
	}

	public String getP_bank_pay() {
		return p_bank_pay;
	}

	public void setP_bank_pay(String p_bank_pay) {
		this.p_bank_pay = p_bank_pay;
	}


	
}
