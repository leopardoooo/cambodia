package com.ycsoft.business.dto.core.prod;

import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class CProdBacthDto extends  BusiBase {
	/**
	 *
	 */
	private static final long serialVersionUID = -2590105154516096344L;
	private String is_invalid_tariff;
	private String is_zero_tariff;// 是否零资费
	private String prod_sn;
	private String tariff_name; // 产品资费名称
	private String next_tariff_name; // 产品资费名称
	private String package_name;
	private Integer tariff_rent;
	private String cust_name;
	private String user_name;
	private String card_id;
	private Boolean isEdit;
	private String terminal_type;
	private String cust_class;
	private String cust_colony;
	private String cust_id;
	private String user_id;
	private String tariff_id;
	private String next_tariff_id;
	private Date next_bill_date;
	private Date invalid_date;
	private Integer billing_cycle;
	private String billing_type;
	private Date eff_date;
	private Date exp_date;
	private Date order_date;
	private String prod_id;
	private String prod_name;
	
	
	
	private String cust_class_text;
	private String cust_colony_text;
	private String terminal_type_text;
	
	
	
	public Date getOrder_date() {
		return order_date;
	}

	public void setOrder_date(Date orderDate) {
		order_date = orderDate;
	}

	public String getTerminal_type_text() {
		return terminal_type_text;
	}

	public void setTerminal_type_text(String terminalTypeText) {
		terminal_type_text = terminalTypeText;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminalType) {
		terminal_type = terminalType;
		this.terminal_type_text = MemoryDict.getDictName(DictKey.TERMINAL_TYPE, terminal_type);
	}

	public String getCust_class() {
		return cust_class;
	}

	public void setCust_class(String custClass) {
		cust_class = custClass;
		cust_class_text = MemoryDict.getDictName(DictKey.CUST_CLASS, cust_class);
	}

	public String getCust_colony() {
		return cust_colony;
	}

	public void setCust_colony(String custColony) {
		cust_colony = custColony;
		cust_colony_text = MemoryDict.getDictName(DictKey.CUST_COLONY, cust_colony);
	}

	public String getCust_class_text() {
		return cust_class_text;
	}

	public void setCust_class_text(String custClassText) {
		cust_class_text = custClassText;
	}

	public String getCust_colony_text() {
		return cust_colony_text;
	}

	public void setCust_colony_text(String custColonyText) {
		cust_colony_text = custColonyText;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String cardId) {
		card_id = cardId;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String custName) {
		cust_name = custName;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
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

	public String getProd_sn() {
		return prod_sn;
	}

	public void setProd_sn(String prodSn) {
		prod_sn = prodSn;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String custId) {
		cust_id = custId;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariffId) {
		tariff_id = tariffId;
	}

	public String getNext_tariff_id() {
		return next_tariff_id;
	}

	public void setNext_tariff_id(String nextTariffId) {
		next_tariff_id = nextTariffId;
	}

	public Date getNext_bill_date() {
		return next_bill_date;
	}

	public void setNext_bill_date(Date nextBillDate) {
		next_bill_date = nextBillDate;
	}

	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date effDate) {
		eff_date = effDate;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date expDate) {
		exp_date = expDate;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prodId) {
		prod_id = prodId;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prodName) {
		prod_name = prodName;
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


	public String getIs_invalid_tariff() {
		return is_invalid_tariff;
	}

	public void setIs_invalid_tariff(String is_invalid_tariff) {
		this.is_invalid_tariff = is_invalid_tariff;
	}

	public Integer getBilling_cycle() {
		return billing_cycle;
	}

	public void setBilling_cycle(Integer billing_cycle) {
		this.billing_cycle = billing_cycle;
	}

	public String getBilling_type() {
		return billing_type;
	}

	public void setBilling_type(String billing_type) {
		this.billing_type = billing_type;
	}

	public Date getInvalid_date() {
		return invalid_date;
	}

	public void setInvalid_date(Date invalid_date) {
		this.invalid_date = invalid_date;
	}
	
}
