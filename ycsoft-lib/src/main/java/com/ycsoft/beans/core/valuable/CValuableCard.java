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
	tn="C_VALUABLE_CARD",
	sn="",
	pk="valuable_id")
public class CValuableCard extends BusiBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1143053080592479533L;
	private String valuable_id ;	
	private Integer amount ;
	private String cust_name;
	private String remark ;	

	/**
	 * default empty constructor
	 */
	public CValuableCard() {}

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

	public String getCust_name() {
		return cust_name;
	}

	public void setCust_name(String custName) {
		cust_name = custName;
	}
	


}