package com.ycsoft.beans.core.acct;

/**
 * CGeneralContractDetail.java	2012/05/22
 */
 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralContractDetail -> C_GENERAL_CONTRACT_DETAIL mapping 
 */
@POJO(
	tn="C_GENERAL_CONTRACT_DETAIL",
	sn="",
	pk="contract_id")
public class CGeneralContractDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6068970139592749400L;
	// CGeneralContractDetail all properties 

	private Integer contract_id ;	
	private Integer credential_start_no ;	
	private Integer credential_end_no ;	
	private Integer credential_amount ;	
	private Integer percent ;	
	
	/**
	 * default empty constructor
	 */
	public CGeneralContractDetail() {}
	
	
	// contract_id getter and setter
	public Integer getContract_id(){
		return this.contract_id ;
	}
	
	public void setContract_id(Integer contract_id){
		this.contract_id = contract_id ;
	}
	
	// credential_start_no getter and setter
	public Integer getCredential_start_no(){
		return this.credential_start_no ;
	}
	
	public void setCredential_start_no(Integer credential_start_no){
		this.credential_start_no = credential_start_no ;
	}
	
	// credential_end_no getter and setter
	public Integer getCredential_end_no(){
		return this.credential_end_no ;
	}
	
	public void setCredential_end_no(Integer credential_end_no){
		this.credential_end_no = credential_end_no ;
	}
	
	// credential_amount getter and setter
	public Integer getCredential_amount(){
		return this.credential_amount ;
	}
	
	public void setCredential_amount(Integer credential_amount){
		this.credential_amount = credential_amount ;
	}
	
	// percent getter and setter
	public Integer getPercent(){
		return this.percent ;
	}
	
	public void setPercent(Integer percent){
		this.percent = percent ;
	}

}