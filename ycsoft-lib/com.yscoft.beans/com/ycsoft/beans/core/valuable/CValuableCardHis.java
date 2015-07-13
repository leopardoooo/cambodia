package com.ycsoft.beans.core.valuable;

/**
 * CValuableCard.java	2011/01/25
 */
 

import java.io.Serializable ;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.daos.config.POJO ;


/**
 * CValuableCard -> C_VALUABLE_CARD mapping 
 */
@POJO(
	tn="C_VALUABLE_CARD_HIS",
	sn="",
	pk="")
public class CValuableCardHis extends BusiBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8851799144744023996L;
	private String valuable_id ;
	private Integer busi_done_code;
	private String cust_name;
	private Integer amount ;	
	private String remark ;	

	/**
	 * default empty constructor
	 */
	public CValuableCardHis() {}

	public String getValuable_id() {
		return valuable_id;
	}

	public void setValuable_id(String valuableId) {
		valuable_id = valuableId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBusi_done_code() {
		return busi_done_code;
	}

	public void setBusi_done_code(Integer busiDoneCode) {
		busi_done_code = busiDoneCode;
	}

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String custName) {
		cust_name = custName;
	}
	


}