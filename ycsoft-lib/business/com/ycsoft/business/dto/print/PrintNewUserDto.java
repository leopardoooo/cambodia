/*
 * @(#) PrintNewUserDto.java 1.0.0 Aug 5, 2011 10:54:09 AM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ycsoft.business.dto.print;


/**
 * 
 *
 * @author allex
 * @since 1.0
 */
public class PrintNewUserDto {

	//根据user_type设置相应的值
	private String user_type;
	
	//数字用户的打印信息
	private String stb_buy_mode;
	private String stb_id;
	private String card_id;
	private String terminal_type;
	
	//宽带的打印信息
	private String modem_mac;
	private String login_name;
	private String login_password;
	
	
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getStb_buy_mode() {
		return stb_buy_mode;
	}
	public void setStb_buy_mode(String stb_buy_mode) {
		this.stb_buy_mode = stb_buy_mode;
	}
	public String getStb_id() {
		return stb_id;
	}
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getTerminal_type() {
		return terminal_type;
	}
	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}
	public String getModem_mac() {
		return modem_mac;
	}
	public void setModem_mac(String modem_mac) {
		this.modem_mac = modem_mac;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getLogin_password() {
		return login_password;
	}
	public void setLogin_password(String login_password) {
		this.login_password = login_password;
	}
	
}
