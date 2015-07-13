package com.ycsoft.beans.core.acct;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * CAcctAcctitemTrans -> C_ACCT_ACCTITEM_TRANS mapping
 */
@POJO(
	tn="C_ACCT_ACCTITEM_TRANS",
	sn="",
	pk="")
public class CAcctAcctitemTrans implements Serializable {

	// CAcctAcctitemTrans all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5696921857336136184L;
	private Integer done_code ;
	private String cust_id ;
	private String out_acct_id ;
	private String out_acctitem_id ;
	private String in_acct_id ;
	private String in_acctitem_id ;
	private String fee_type ;
	private Integer amount ;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemTrans() {}




	public Integer getDone_code() {
		return done_code;
	}




	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}




	// cust_id getter and setter
	public String getCust_id(){
		return cust_id ;
	}

	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}

	// out_acct_id getter and setter
	public String getOut_acct_id(){
		return out_acct_id ;
	}

	public void setOut_acct_id(String out_acct_id){
		this.out_acct_id = out_acct_id ;
	}

	// out_acctitem_id getter and setter
	public String getOut_acctitem_id(){
		return out_acctitem_id ;
	}

	public void setOut_acctitem_id(String out_acctitem_id){
		this.out_acctitem_id = out_acctitem_id ;
	}

	// in_acct_id getter and setter
	public String getIn_acct_id(){
		return in_acct_id ;
	}

	public void setIn_acct_id(String in_acct_id){
		this.in_acct_id = in_acct_id ;
	}

	// in_acctitem_id getter and setter
	public String getIn_acctitem_id(){
		return in_acctitem_id ;
	}

	public void setIn_acctitem_id(String in_acctitem_id){
		this.in_acctitem_id = in_acctitem_id ;
	}

	// fee_type getter and setter
	public String getFee_type(){
		return fee_type ;
	}

	public void setFee_type(String fee_type){
		this.fee_type = fee_type ;
	}

	// amount getter and setter
	public Integer getAmount(){
		return amount ;
	}

	public void setAmount(Integer amount){
		this.amount = amount ;
	}

}