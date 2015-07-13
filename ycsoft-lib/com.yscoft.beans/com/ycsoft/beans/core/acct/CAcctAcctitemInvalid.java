package com.ycsoft.beans.core.acct;

/**
 * CAcctAcctitemInvalid.java	2011/04/16
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * CAcctAcctitemInvalid -> C_ACCT_ACCTITEM_INVALID mapping 
 */
@POJO(
	tn="C_ACCT_ACCTITEM_INVALID",
	sn="",
	pk="")
public class CAcctAcctitemInvalid  extends BusiBase implements Serializable {
	
	// CAcctAcctitemInvalid all properties 

	private Date done_date ;	
	private String cust_id ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private String fee_type ;	
	private Integer is_active ;	
	private String invalid_type ;	
	private Integer invalid_fee ;	
	
	private String fee_type_text;
	private String prod_sn;
	private String tariff_id;
	public String getProd_sn() {
		return prod_sn;
	}


	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}


	public String getTariff_id() {
		return tariff_id;
	}


	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}


	/**
	 * default empty constructor
	 */
	public CAcctAcctitemInvalid() {}
	
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// acct_id getter and setter
	public String getAcct_id(){
		return this.acct_id ;
	}
	
	public void setAcct_id(String acct_id){
		this.acct_id = acct_id ;
	}
	
	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return this.acctitem_id ;
	}
	
	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}
	
	// fee_type getter and setter
	public String getFee_type(){
		return this.fee_type ;
	}
	
	public void setFee_type(String fee_type){
		fee_type_text = MemoryDict.getDictName(DictKey.ACCT_FEE_TYPE, fee_type);
		this.fee_type = fee_type ;
	}
	
	// is_active getter and setter
	public Integer getIs_active(){
		return this.is_active ;
	}
	
	public void setIs_active(Integer is_active){
		this.is_active = is_active ;
	}
	
	// invalid_type getter and setter
	public String getInvalid_type(){
		return this.invalid_type ;
	}
	
	public void setInvalid_type(String invalid_type){
		this.invalid_type = invalid_type ;
	}
	
	// invalid_fee getter and setter
	public Integer getInvalid_fee(){
		return this.invalid_fee ;
	}
	
	public void setInvalid_fee(Integer invalid_fee){
		this.invalid_fee = invalid_fee ;
	}


	public String getFee_type_text() {
		return fee_type_text;
	}


	public void setFee_type_text(String feeTypeText) {
		fee_type_text = feeTypeText;
	}
}