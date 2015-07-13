package com.ycsoft.beans.core.bill;

public class BillDto extends BBill {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3743119038130392345L;
	private String acctitem_name;
	private String tariff_name;
	private String card_id;
	
	private String user_name;
	private String come_from_text ;
	
	
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAcctitem_name() {
		return acctitem_name;
	}
	public void setAcctitem_name(String acctitem_name) {
		this.acctitem_name = acctitem_name;
	}
	public String getCome_from_text() {
		return come_from_text;
	}
	public void setCome_from_text(String come_from_text) {
		this.come_from_text = come_from_text;
	}
	
	
	
}
