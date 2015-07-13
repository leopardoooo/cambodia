package com.ycsoft.business.dto.core.acct;

public class UnitPayDto extends AcctitemDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2230291886914787483L;
	
	private String cust_id;
	private String cust_name;
	private String card_id;
	private String user_id;
	
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
}
