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
/**
 * @author yuben
 *
 */
@POJO(tn = "C_USER", sn = "SEQ_USER_ID", pk = "USER_ID")
public class CUser extends BusiBase implements Serializable, Cloneable {

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
	private String login_name;
	private String password;
	private String terminal_type;
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
	
	
	private String user_type_text = "";
	private String stop_type_text = "";
	private String status_text = "";
	private String net_type_text = "";
	private String user_class_text = "";
	private String terminal_type_text="";
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
	private String newPassword;
	private Date protocol_date;
	
	private Date prod_exp_date;
	

	public Date getProd_exp_date() {
		return prod_exp_date;
	}

	public void setProd_exp_date(Date prod_exp_date) {
		this.prod_exp_date = prod_exp_date;
	}

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

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
		this.terminal_type_text =  MemoryDict.getDictName(DictKey.TERMINAL_TYPE,terminal_type);
	}

	public String getTerminal_type_text() {
		return terminal_type_text;
	}

	public void setTerminal_type_text(String terminal_type_text) {
		this.terminal_type_text = terminal_type_text;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Date getProtocol_date() {
		return protocol_date;
	}

	public void setProtocol_date(Date protocol_date) {
		this.protocol_date = protocol_date;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

}