package com.ycsoft.beans.config;

/**
 * TCountyAcctChange.java	2012/04/24
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * TCountyAcctChange -> T_COUNTY_ACCT_CHANGE mapping 
 */
@POJO(
	tn="T_COUNTY_ACCT_CHANGE",
	sn="",
	pk="")
public class TCountyAcctChange implements Serializable {
	
	// TCountyAcctChange all properties 

	private String t_acct_id ;	
	private Integer change_amount ;
	private String optr_id ;	
	private Date done_date ;	
	private Integer done_code ;	
	
	
	private String optr_name;
	
	/**
	 * default empty constructor
	 */
	public TCountyAcctChange() {}
	
	public TCountyAcctChange(String tAcctId,Integer changeAmount,String optrId,Integer doneCode){
		this.t_acct_id=tAcctId;
		this.optr_id= optrId;
		this.done_code = doneCode;
		this.change_amount = changeAmount;
	}
	
	
	// t_acct_id getter and setter
	public String getT_acct_id(){
		return this.t_acct_id ;
	}
	
	public void setT_acct_id(String t_acct_id){
		this.t_acct_id = t_acct_id ;
	}

	public Integer getChange_amount() {
		return change_amount;
	}


	public void setChange_amount(Integer changeAmount) {
		change_amount = changeAmount;
	}


	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}


	public String getOptr_name() {
		return optr_name;
	}


	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}
	
	

}