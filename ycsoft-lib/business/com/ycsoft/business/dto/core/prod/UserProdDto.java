package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.core.prod.CProd;

public class UserProdDto extends CProd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1551995457677193352L;
	
	private String user_id;
	private String user_name;
	private String user_type;
	private String user_status;
	private String card_id;
	private String stb_id;
	private String modem_mac;
	private String terminal_type;
	private String serv_type;
	private String login_name;	//宽带登录名
	
	private Date done_date;		//用户产品退订日期
	private String remark;		//退订备注
	
	public Date getDone_date() {
		return done_date;
	}
	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTerminal_type() {
		return terminal_type;
	}
	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}
	public String getServ_type() {
		return serv_type;
	}
	public void setServ_type(String serv_type) {
		this.serv_type = serv_type;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getStb_id() {
		return stb_id;
	}
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}
	public String getModem_mac() {
		return modem_mac;
	}
	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	
}
