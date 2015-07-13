package com.ycsoft.beans.core.acct;

/**
 * CGeneralContractPay.java	2012/05/23
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralContractPay -> C_GENERAL_CONTRACT_PAY mapping 
 */
@POJO(
	tn="C_GENERAL_CONTRACT_PAY",
	sn="",
	pk="")
public class CGeneralContractPay implements Serializable {
	
	// CGeneralContractPay all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 3032375341981904894L;
	private Integer contract_id ;	
	private Integer fee ;	
	private Integer done_code ;	
	private String optr_id ;	
	private Date create_time ;
	
	private String optr_name;
	private Date acct_date;
	private String fee_sn;
	
	/**
	 * default empty constructor
	 */
	public CGeneralContractPay() {}
	
	
	// contract_id getter and setter
	public Integer getContract_id(){
		return this.contract_id ;
	}
	
	public void setContract_id(Integer contract_id){
		this.contract_id = contract_id ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}


	public String getOptr_name() {
		return optr_name;
	}


	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}


	public Date getAcct_date() {
		return acct_date;
	}


	public void setAcct_date(Date acctDate) {
		acct_date = acctDate;
	}


	public String getFee_sn() {
		return fee_sn;
	}


	public void setFee_sn(String feeSn) {
		fee_sn = feeSn;
	}
}