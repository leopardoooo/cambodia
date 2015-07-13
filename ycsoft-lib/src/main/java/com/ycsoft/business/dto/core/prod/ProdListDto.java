/**
 *
 */
package com.ycsoft.business.dto.core.prod;

import java.util.Date;

/**
 * @author YC-SOFT
 *
 */
public class ProdListDto {
	private String prodId;
	private String prodName;
	private String tariffId;
	private String tariffName;
	private String feeDate;
	private String expDate;
	private String dynamicRscList;
	private String publicAcctItemType;
	private Date preOpenTime;//预开通时间
	private String isBankPay;//银行扣费
	private String prod_remark;
	
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getTariffId() {
		return tariffId;
	}
	public void setTariffId(String tariffId) {
		this.tariffId = tariffId;
	}
	public String getFeeDate() {
		return feeDate;
	}
	public void setFeeDate(String feeDate) {
		this.feeDate = feeDate;
	}
	public String getDynamicRscList() {
		return dynamicRscList;
	}
	public void setDynamicRscList(String dynamicRscList) {
		this.dynamicRscList = dynamicRscList;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getPublicAcctItemType() {
		return publicAcctItemType;
	}
	public void setPublicAcctItemType(String publicAcctItemType) {
		this.publicAcctItemType = publicAcctItemType;
	}
	public Date getPreOpenTime() {
		return preOpenTime;
	}
	public void setPreOpenTime(Date preOpenTime) {
		this.preOpenTime = preOpenTime;
	}
	/**
	 * @return the prod_remark
	 */
	public String getProd_remark() {
		return prod_remark;
	}
	/**
	 * @param prod_remark the prod_remark to set
	 */
	public void setProd_remark(String prod_remark) {
		this.prod_remark = prod_remark;
	}
	/**
	 * @return the prodName
	 */
	public String getProdName() {
		return prodName;
	}
	/**
	 * @param prodName the prodName to set
	 */
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	/**
	 * @return the tariffName
	 */
	public String getTariffName() {
		return tariffName;
	}
	/**
	 * @param tariffName the tariffName to set
	 */
	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}
	public String getIsBankPay() {
		return isBankPay;
	}
	public void setIsBankPay(String isBankPay) {
		this.isBankPay = isBankPay;
	}
}
