/**
 * PProd.java	2010/06/07
 */
package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PProd -> P_PROD mapping
 */
@POJO(tn = "P_PROD", sn = "SEQ_PROD_ID", pk = "PROD_ID")
public class PProd extends BusiBase implements Serializable {

	// PProd all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -863780339196079685L;
	private String prod_id;
	private String prod_name;
	private String printitem_id;
	private String prod_desc;
	private String serv_id;
	private String prod_type;
	private String is_base;
	private String is_bind_base;
	private String just_for_once;
	private Date invalid_date;
	private Date eff_date;
	private Date exp_date;
	private String status;
	private String for_area_id;
	private String refund;
	private String trans;
	private String str1 ;
	private String str2 ;
	private String str3 ;
	private String str4 ;
	private String str5 ;
	private String str6 ;
	private String str7 ;
	private String str8 ;
	private String str9 ;
	private String str10 ;
	private String allow_pay;
	private String public_acctitem_type;
	private String is_extend;
	private String is_bank_pay;
	private String tariff_id;

	private boolean pkg = false; // 是否为套餐
	private String is_invalid;	//是否失效

	private String refund_text;
	private String trans_text;
	private String is_base_text;
	private String is_bind_base_text;
	private String status_text;
	private String printitem_name;
	private String for_area_id_text;
	private String prod_type_text;
	private String serv_id_text;
	private String just_for_once_text;
	private String is_extend_text;
	private String is_bank_pay_text;
	
	private String public_acctitem_type_text;
	private String allow_pay_text;

	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}
	
	public String getIs_extend() {
		return is_extend;
	}

	public void setIs_extend(String isExtend) {
		is_extend = isExtend;
		is_extend_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_extend);
	}

	public String getIs_extend_text() {
		return is_extend_text;
	}

	public void setIs_extend_text(String isExtendText) {
		is_extend_text = isExtendText;
	}

	public String getJust_for_once_text() {
		return just_for_once_text;
	}

	public void setJust_for_once_text(String just_for_once_text) {
		this.just_for_once_text = just_for_once_text;
	}

	public String getServ_id_text() {
		return serv_id_text;
	}

	public void setServ_id_text(String serv_id_text) {
		this.serv_id_text = serv_id_text;
	}

	/**
	 * default empty constructor
	 */
	public PProd() {
	}

	// prod_id getter and setter
	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	// prod_name getter and setter
	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	// printitem_id getter and setter
	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
		printitem_name = MemoryDict.getDictName(DictKey.PRINTITEM_NAME,
				this.printitem_id);
	}

	// prod_desc getter and setter
	public String getProd_desc() {
		return prod_desc;
	}

	public void setProd_desc(String prod_desc) {
		this.prod_desc = prod_desc;
	}

	// prod_type getter and setter
	public String getProd_type() {
		return prod_type;
	}

	public void setProd_type(String prod_type) {
		prod_type_text = MemoryDict.getDictName(DictKey.PROD_TYPE, prod_type);
		this.prod_type = prod_type;
		if (!prod_type.equals(SystemConstants.PROD_TYPE_BASE))
			pkg = true;
	}

	// is_base getter and setter
	public String getIs_base() {
		return is_base;
	}

	public void setIs_base(String is_base) {
		this.is_base = is_base;
		is_base_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.is_base);
	}

	// is_bind_base getter and setter
	public String getIs_bind_base() {
		return is_bind_base;
	}

	public void setIs_bind_base(String is_bind_base) {
		this.is_bind_base = is_bind_base;
		is_bind_base_text = MemoryDict
				.getDictName(DictKey.BOOLEAN, this.is_bind_base);
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	public boolean isPkg() {
		return pkg;
	}

	/**
	 * @return the serv_id
	 */
	public String getServ_id() {
		return serv_id;
	}

	/**
	 * @param serv_id
	 *            the serv_id to set
	 */
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
		serv_id_text = MemoryDict.getDictName(DictKey.SERV_ID,this.serv_id);
	}

	public String getIs_base_text() {
		return is_base_text;
	}

	public void setIs_base_text(String is_base_text) {
		this.is_base_text = is_base_text;
	}

	public String getIs_bind_base_text() {
		return is_bind_base_text;
	}

	public void setIs_bind_base_text(String is_bind_base_text) {
		this.is_bind_base_text = is_bind_base_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public String getFor_area_id() {
		return for_area_id;
	}

	public void setFor_area_id(String for_area_id) {
		this.for_area_id = for_area_id;
		for_area_id_text = MemoryDict.getDictName(DictKey.AREA, this.for_area_id);
	}

	public String getFor_area_id_text() {
		return for_area_id_text;
	}

	public void setFor_area_id_text(String for_area_id_text) {
		this.for_area_id_text = for_area_id_text;

	}

	public String getProd_type_text() {
		return prod_type_text;
	}

	/**
	 * @return the just_for_once
	 */
	public String getJust_for_once() {
		return just_for_once;
	}

	/**
	 * @param just_for_once the just_for_once to set
	 */
	public void setJust_for_once(String just_for_once) {
		this.just_for_once = just_for_once;
		just_for_once_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.just_for_once);
	}

	public Date getInvalid_date() {
		return invalid_date;
	}

	public void setInvalid_date(Date invalid_date) {
		this.invalid_date = invalid_date;
	}

	/**
	 * @param pkg the pkg to set
	 */
	public void setPkg(boolean pkg) {
		this.pkg = pkg;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
		refund_text = MemoryDict
		.getDictName(DictKey.BOOLEAN, this.refund);
	}

	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
		trans_text = MemoryDict
		.getDictName(DictKey.BOOLEAN, this.trans);
	}

	public String getRefund_text() {
		return refund_text;
	}

	public void setRefund_text(String refund_text) {
		this.refund_text = refund_text;
	}

	public String getTrans_text() {
		return trans_text;
	}

	public void setTrans_text(String trans_text) {
		this.trans_text = trans_text;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	public String getStr4() {
		return str4;
	}

	public void setStr4(String str4) {
		this.str4 = str4;
	}

	public String getStr5() {
		return str5;
	}

	public void setStr5(String str5) {
		this.str5 = str5;
	}

	public String getStr6() {
		return str6;
	}

	public void setStr6(String str6) {
		this.str6 = str6;
	}

	public String getStr7() {
		return str7;
	}

	public void setStr7(String str7) {
		this.str7 = str7;
	}

	public String getStr8() {
		return str8;
	}

	public void setStr8(String str8) {
		this.str8 = str8;
	}

	public String getStr9() {
		return str9;
	}

	public void setStr9(String str9) {
		this.str9 = str9;
	}

	public String getStr10() {
		return str10;
	}

	public void setStr10(String str10) {
		this.str10 = str10;
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

	public String getPrintitem_name() {
		return printitem_name;
	}

	public void setPrintitem_name(String printitemName) {
		printitem_name = printitemName;
	}
	
	public String getAllow_pay() {
		return allow_pay;
	}

	public void setAllow_pay(String allow_pay) {
		this.allow_pay = allow_pay;
		this.allow_pay_text = MemoryDict.getDictName(DictKey.BOOLEAN, allow_pay);
	}
	
	public String getPublic_acctitem_type() {
		return public_acctitem_type;
	}

	public void setPublic_acctitem_type(String public_acctitem_type) {
		this.public_acctitem_type = public_acctitem_type;
		public_acctitem_type_text = MemoryDict.getDictName(DictKey.PUBLIC_ACCTITEM_TYPE,
				public_acctitem_type);
	}

	public String getPublic_acctitem_type_text() {
		return public_acctitem_type_text;
	}

	public void setPublic_acctitem_type_text(String publicAcctitemTypeText) {
		public_acctitem_type_text = publicAcctitemTypeText;
	}

	public String getAllow_pay_text() {
		return allow_pay_text;
	}

	public void setAllow_pay_text(String allowPayText) {
		allow_pay_text = allowPayText;
	}

	public String getIs_invalid() {
		return is_invalid;
	}

	public void setIs_invalid(String is_invalid) {
		this.is_invalid = is_invalid;
	}

	public String getIs_bank_pay() {
		return is_bank_pay;
	}

	public void setIs_bank_pay(String is_bank_pay) {
		this.is_bank_pay = is_bank_pay;
		this.is_bank_pay_text = MemoryDict.getDictName(DictKey.BOOLEAN, is_bank_pay);
	}

	public String getIs_bank_pay_text() {
		return is_bank_pay_text;
	}

	public void setIs_bank_pay_text(String is_bank_pay_text) {
		this.is_bank_pay_text = is_bank_pay_text;
	}

}