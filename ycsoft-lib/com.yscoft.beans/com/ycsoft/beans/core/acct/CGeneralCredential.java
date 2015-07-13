package com.ycsoft.beans.core.acct;

/**
 * CGeneralCredential.java	2011/01/24
 */
 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralCredential -> C_GENERAL_CREDENTIAL mapping 
 */
@POJO(
	tn="C_GENERAL_CREDENTIAL",
	sn="",
	pk="credential_no")
public class CGeneralCredential implements Serializable {
	
	// CGeneralCredential all properties 

	private Integer contract_id ;	
	private Integer amount ;	
	private String credential_no ;
	private Integer balance ;
	private Integer percent ;
	
	/**
	 * default empty constructor
	 */
	public CGeneralCredential() {}
	
	
	// contract_id getter and setter
	public Integer getContract_id(){
		return this.contract_id ;
	}
	
	public void setContract_id(Integer contract_id){
		this.contract_id = contract_id ;
	}
	
	// amount getter and setter
	public Integer getAmount(){
		return this.amount ;
	}
	
	public void setAmount(Integer amount){
		this.amount = amount ;
	}
	
	// credential_no getter and setter
	public String getCredential_no(){
		return this.credential_no ;
	}
	
	public void setCredential_no(String credential_no){
		this.credential_no = credential_no ;
	}


	public Integer getBalance() {
		return balance;
	}


	public void setBalance(Integer balance) {
		this.balance = balance;
	}


	public Integer getPercent() {
		return percent;
	}


	public void setPercent(Integer percent) {
		this.percent = percent;
	}

}