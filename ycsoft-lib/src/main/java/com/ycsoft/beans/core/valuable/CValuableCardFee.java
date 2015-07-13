package com.ycsoft.beans.core.valuable;

import java.io.Serializable;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO;


/**
 * CValuableCardFee -> C_VALUABLE_CARD_FEE mapping 
 */
@POJO(
		tn="C_VALUABLE_CARD_FEE",
		sn="",
		pk="valuable_id")
public class CValuableCardFee extends BusiBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cust_id="";
	private String user_id="";
	private String card_id="";
	private String acct_id="";
	private String acctitem_id="";
	private String busi_done_code="";
	private String valuable_type="";
	private String valuable_id="";
	private String sysflag="";
	private String remark="";
	private Integer amount ;
 	 
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
	public String getAcctitem_id() {
		return acctitem_id;
	}
	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
	}
	public String getBusi_done_code() {
		return busi_done_code;
	}
	public void setBusi_done_code(String busi_done_code) {
		this.busi_done_code = busi_done_code;
	}
	public String getValuable_type() {
		return valuable_type;
	}
	public void setValuable_type(String valuable_type) {
		this.valuable_type = valuable_type;
	}
	public String getValuable_id() {
		return valuable_id;
	}
	public void setValuable_id(String valuable_id) {
		this.valuable_id = valuable_id;
	}
	public String getSysflag() {
		return sysflag;
	}
	public void setSysflag(String sysflag) {
		this.sysflag = sysflag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
