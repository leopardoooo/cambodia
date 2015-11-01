package com.ycsoft.business.dto.core.cust;

import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;


public class DoneInfoDto {

	private String user_type;
	private String user_name;
	private String stb_id;
	private String card_id;
	private String modem_mac;
	private String prod_name;
	private Date invalid_date;
	private String tariff_name;
	private String status;
	private String buy_mode;
	private String device_type;
	private String device_code;
	private String pair_card_code;
	private String pair_modem_code;
	private String column_name;
	private String old_value;
	private String new_value;
	private String promotion_name;
	
	private String column_name_text;
	private String user_type_text = "";
	private String status_text = "";
	private String buy_mode_text;
	private String device_type_text;
	
	private String user_id;
	private Integer months ;
	private Integer real_pay ;	
	private Integer should_pay ;
	private Integer refund_pay;
	private Date bind_invalid_date;
	
	private String old_prod_name;
	private String old_tariff_name;
	private Integer pre_fee;
	private Integer change_fee;
	private Integer fee;
	private String change_type ;	
	private String fee_type ;
	private String change_type_text ;	
	private String fee_type_text ;
	private String remark;
	
	private Integer done_code;
	private String order_sn;
	private String prod_id;
	private Date order_time;
	private Integer order_fee;
	private Integer active_fee;
	private Integer order_months;
	private Date eff_date;
	private Date exp_date;
	
	public Integer getDone_code() {
		return done_code;
	}

	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}
	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public Date getOrder_time() {
		return order_time;
	}

	public void setOrder_time(Date order_time) {
		this.order_time = order_time;
	}

	public Integer getOrder_fee() {
		return order_fee;
	}

	public void setOrder_fee(Integer order_fee) {
		this.order_fee = order_fee;
	}

	public Integer getActive_fee() {
		return active_fee;
	}

	public void setActive_fee(Integer active_fee) {
		this.active_fee = active_fee;
	}

	public Integer getOrder_months() {
		return order_months;
	}

	public void setOrder_months(Integer order_months) {
		this.order_months = order_months;
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

	public String getOld_prod_name() {
		return old_prod_name;
	}

	public void setOld_prod_name(String old_prod_name) {
		this.old_prod_name = old_prod_name;
	}

	public Integer getPre_fee() {
		return pre_fee;
	}

	public void setPre_fee(Integer pre_fee) {
		this.pre_fee = pre_fee;
	}

	public Integer getChange_fee() {
		return change_fee;
	}

	public void setChange_fee(Integer change_fee) {
		this.change_fee = change_fee;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public Integer getReal_pay() {
		return real_pay;
	}

	public void setReal_pay(Integer realPay) {
		real_pay = realPay;
	}

	public Integer getShould_pay() {
		return should_pay;
	}

	public void setShould_pay(Integer shouldPay) {
		should_pay = shouldPay;
	}

	public Date getBind_invalid_date() {
		return bind_invalid_date;
	}

	public void setBind_invalid_date(Date bindInvalidDate) {
		bind_invalid_date = bindInvalidDate;
	}

	public String getPromotion_name() {
		return promotion_name;
	}

	public void setPromotion_name(String promotionName) {
		promotion_name = promotionName;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String columnName) {
		column_name = columnName;
	}

	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String oldValue) {
		old_value = oldValue;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String newValue) {
		new_value = newValue;
	}

	public String getColumn_name_text() {
		return column_name_text;
	}

	public void setColumn_name_text(String columnNameText) {
		column_name_text = columnNameText;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String deviceCode) {
		device_code = deviceCode;
	}

	public String getBuy_mode() {
		return buy_mode;
	}

	public void setBuy_mode(String buyMode) {
		buy_mode = buyMode;
		buy_mode_text = MemoryDict.getDictName(DictKey.DEVICE_BUY_MODE, buy_mode);
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String deviceType) {
		device_type = deviceType;
		device_type_text = MemoryDict.getDictName(DictKey.DEVICE_TYPE, device_type);
	}

	public String getPair_card_code() {
		return pair_card_code;
	}

	public void setPair_card_code(String pairCardCode) {
		pair_card_code = pairCardCode;
	}

	public String getPair_modem_code() {
		return pair_modem_code;
	}

	public void setPair_modem_code(String pairModemCode) {
		pair_modem_code = pairModemCode;
	}

	public String getBuy_mode_text() {
		return buy_mode_text;
	}

	public void setBuy_mode_text(String buyModeText) {
		buy_mode_text = buyModeText;
	}

	public String getDevice_type_text() {
		return device_type_text;
	}

	public void setDevice_type_text(String deviceTypeText) {
		device_type_text = deviceTypeText;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String statusText) {
		status_text = statusText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prodName) {
		prod_name = prodName;
	}

	public Date getInvalid_date() {
		return invalid_date;
	}

	public void setInvalid_date(Date invalidDate) {
		invalid_date = invalidDate;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariffName) {
		tariff_name = tariffName;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String userType) {
		user_type = userType;
		user_type_text = MemoryDict.getDictName(DictKey.USER_TYPE, this.user_type);
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
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

	public String getUser_type_text() {
		return user_type_text;
	}

	public void setUser_type_text(String userTypeText) {
		user_type_text = userTypeText;
	}

	public String getOld_tariff_name() {
		return old_tariff_name;
	}

	public void setOld_tariff_name(String old_tariff_name) {
		this.old_tariff_name = old_tariff_name;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		this.change_type = change_type;
		if(StringHelper.isNotEmpty(change_type))
			this.change_type_text = MemoryDict.getDictName(DictKey.ACCT_CHANGE_TYPE, change_type);
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
		if(StringHelper.isNotEmpty(fee_type))
			this.fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
	}

	public String getChange_type_text() {
		return change_type_text;
	}

	public String getFee_type_text() {
		return fee_type_text;
	}

	public Integer getRefund_pay() {
		return refund_pay;
	}

	public void setRefund_pay(Integer refund_pay) {
		this.refund_pay = refund_pay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
