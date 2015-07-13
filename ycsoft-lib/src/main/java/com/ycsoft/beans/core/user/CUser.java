/**
 * CUser.java	2010/02/25
 */

package com.ycsoft.beans.core.user;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CUser -> C_USER mapping
 */
@POJO(tn = "C_USER", sn = "SEQ_USER_ID", pk = "USER_ID")
public class CUser extends BusiBase implements Serializable {

	// CUser all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1651740282024365109L;
	private String user_id;
	private String cust_id;
	private String acct_id;
	private String user_type;
	private String user_name;
	private String user_addr;
	private String user_class;
	private String user_class_area;
	private Date user_class_exp_date;
	private String stop_type;
	private String status;
	private String net_type;
	private String stb_id;
	private String card_id;
	private String modem_mac;
	private Date open_time;
	private Date status_date;
	private String is_rstop_fee;
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
	private String str11;
	private String str12;
	private String str13;
	private String str14;
	private String str15;
	private String str16;
	private String str17;
	private String str18;
	private String str19;
	private String str20;

	
	private String user_type_text = "";
	private String stop_type_text = "";
	private String status_text = "";
	private String net_type_text = "";
	private String user_class_text = "";
	//电视机厂家
	private String tv_model_text = "";
	
	private Integer seq;//第几个开户的用户
	private String str7_text;
	private String str11_text;
	private String str19_text;
	private String stb_buy;
	private String device_model;
	private String card_buy;
	private String modem_buy;
	
	private String auto_promotion;

	public String getAuto_promotion() {
		return auto_promotion;
	}

	public void setAuto_promotion(String auto_promotion) {
		this.auto_promotion = auto_promotion;
	}
	
	public String getStb_buy() {
		return stb_buy;
	}

	public void setStb_buy(String stb_buy) {
		this.stb_buy = stb_buy;
	}

	public String getCard_buy() {
		return card_buy;
	}

	public void setCard_buy(String card_buy) {
		this.card_buy = card_buy;
	}

	public String getModem_buy() {
		return modem_buy;
	}

	public void setModem_buy(String modem_buy) {
		this.modem_buy = modem_buy;
	}

	/**
	 * @return the seq
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * @return the user_class_text
	 */
	public String getUser_class_text() {
		return user_class_text;
	}

	/**
	 * default empty constructor
	 */
	public CUser() {
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// acct_id getter and setter
	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	// user_type getter and setter
	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
		user_type_text = MemoryDict.getDictName(DictKey.USER_TYPE, this.user_type);
	}

	// user_addr getter and setter
	public String getUser_addr() {
		return user_addr;
	}

	public void setUser_addr(String user_addr) {
		this.user_addr = user_addr;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		net_type_text = MemoryDict.getDictName(DictKey.USER_NET_TYPE, net_type);
		this.net_type = net_type;
	}

	// stb_id getter and setter
	public String getStb_id() {
		return stb_id;
	}

	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}

	// card_id getter and setter
	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	// modem_id getter and setter
	public String getModem_mac() {
		return modem_mac;
	}

	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}

	// open_time getter and setter
	public Date getOpen_time() {
		return open_time;
	}

	public void setOpen_time(Date open_time) {
		this.open_time = open_time;
	}

	public String getStop_type() {
		return stop_type;
	}

	public void setStop_type(String stop_type) {
		this.stop_type = stop_type;
		stop_type_text = MemoryDict.getDictName(DictKey.STOP_TYPE, this.stop_type);
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_type_text() {
		return user_type_text;
	}

	public String getStop_type_text() {
		return stop_type_text;
	}

	public String getStatus_text() {
		return status_text;
	}

	/**
	 * @return the net_type_text
	 */
	public String getNet_type_text() {
		return net_type_text;
	}

	/**
	 * @return the user_class
	 */
	public String getUser_class() {
		return user_class;
	}

	/**
	 * @param user_class the user_class to set
	 */
	public void setUser_class(String user_class) {
		user_class_text = MemoryDict.getDictName(DictKey.USER_CLASS, user_class);
		this.user_class = user_class;
	}

	public Date getStatus_date() {
		return status_date;
	}

	public void setStatus_date(Date status_date) {
		this.status_date = status_date;
	}

	public String getUser_class_area() {
		return user_class_area;
	}

	public void setUser_class_area(String user_class_area) {
		this.user_class_area = user_class_area;
	}

	public Date getUser_class_exp_date() {
		return user_class_exp_date;
	}

	public void setUser_class_exp_date(Date user_class_exp_date) {
		this.user_class_exp_date = user_class_exp_date;
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
		tv_model_text = MemoryDict.getDictName(DictKey.TV_MODEL,str4);
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
		this.str7_text = MemoryDict.getDictName(DictKey.USER_LEIBIE, str7);
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

	public String getIs_rstop_fee() {
		return is_rstop_fee;
	}

	public void setIs_rstop_fee(String is_rstop_fee) {
		this.is_rstop_fee = is_rstop_fee;
	}

	public String getTv_model_text() {
		return tv_model_text;
	}

	public String getStr7_text() {
		return str7_text;
	}



	public String getStr11_text() {
		return str11_text;
	}

	public String getStr11() {
		return str11;
	}

	public void setStr11(String str11) {
		this.str11 = str11;
		this.str11_text = MemoryDict.getDictName(DictKey.VOD_USER_TYPE, str11);
	}

	public String getStr12() {
		return str12;
	}

	public void setStr12(String str12) {
		this.str12 = str12;
	}

	public String getStr13() {
		return str13;
	}

	public void setStr13(String str13) {
		this.str13 = str13;
	}

	public String getStr14() {
		return str14;
	}

	public void setStr14(String str14) {
		this.str14 = str14;
	}

	public String getStr15() {
		return str15;
	}

	public void setStr15(String str15) {
		this.str15 = str15;
	}

	public String getStr16() {
		return str16;
	}

	public void setStr16(String str16) {
		this.str16 = str16;
	}

	public String getStr17() {
		return str17;
	}

	public void setStr17(String str17) {
		this.str17 = str17;
	}

	public String getStr18() {
		return str18;
	}

	public void setStr18(String str18) {
		this.str18 = str18;
	}

	public String getStr19() {
		return str19;
	}

	public void setStr19(String str19) {
		this.str19 = str19;
		this.str19_text = MemoryDict.getDictName(DictKey.BOOLEAN, str19);
	}
	
	public String getStr19_text() {
		return str19_text;
	}

	public String getStr20() {
		return str20;
	}

	public void setStr20(String str20) {
		this.str20 = str20;
	}

	public void setStr11_text(String str11_text) {
		this.str11_text = str11_text;
	}

	/**
	 * @return the device_model
	 */
	public String getDevice_model() {
		return device_model;
	}

	/**
	 * @param device_model the device_model to set
	 */
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	

}