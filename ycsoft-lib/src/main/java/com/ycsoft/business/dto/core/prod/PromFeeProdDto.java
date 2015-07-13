package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ycsoft.beans.prod.PPromFeeProd;
import com.ycsoft.beans.prod.PRes;

public class PromFeeProdDto extends PPromFeeProd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1131192212385927854L;
	
	private String prod_sn;
	private String card_id;
	private String user_id;
	private String is_base;
	
	private String user_name;
	
	private Integer should_pay;
	
	private Integer prod_count;
	
	private Integer rent;
	private Integer billing_cycle;
	private List<PRes> resList; 
	
	private String user_name_text;
	private Date bind_invalid_date;
	private String user_type;
	private String serv_id;
	
	

	public String getServ_id() {
		return serv_id;
	}
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String userType) {
		user_type = userType;
	}
	public String getUser_name_text() {
		return user_name_text;
	}
	public void setUser_name_text(String userNameText) {
		user_name_text = userNameText;
	}
	public Date getBind_invalid_date() {
		return bind_invalid_date;
	}
	public void setBind_invalid_date(Date bind_invalid_date) {
		this.bind_invalid_date = bind_invalid_date;
	}
	public List<PRes> getResList() {
		return resList;
	}
	public void setResList(List<PRes> resList) {
		this.resList = resList;
	}
	public String getProd_sn() {
		return prod_sn;
	}
	public void setProd_sn(String prodSn) {
		prod_sn = prodSn;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String cardId) {
		card_id = cardId;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public Integer getShould_pay() {
		return should_pay;
	}
	public void setShould_pay(Integer shouldPay) {
		should_pay = shouldPay;
	}
	public Integer getProd_count() {
		return prod_count;
	}
	public void setProd_count(Integer prodCount) {
		prod_count = prodCount;
	}
	public Integer getRent() {
		return rent;
	}
	public void setRent(Integer rent) {
		this.rent = rent;
	}
	public Integer getBilling_cycle() {
		return billing_cycle;
	}
	public void setBilling_cycle(Integer billing_cycle) {
		this.billing_cycle = billing_cycle;
	}
	public String getIs_base() {
		return is_base;
	}
	public void setIs_base(String is_base) {
		this.is_base = is_base;
	}
	
}
