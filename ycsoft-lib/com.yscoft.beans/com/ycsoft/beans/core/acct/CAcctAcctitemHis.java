/**
 * CAcctAcctitemHis.java	2011/03/17
 */
 
package com.ycsoft.beans.core.acct; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CAcctAcctitemHis -> C_ACCT_ACCTITEM_HIS mapping 
 */
@POJO(
	tn="C_ACCT_ACCTITEM_HIS",
	sn="",
	pk="")
public class CAcctAcctitemHis implements Serializable {
	
	// CAcctAcctitemHis all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = 5057996223452673469L;
	private Integer done_code ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private Integer active_balance ;	
	private Integer owe_fee ;	
	private Integer real_fee ;	
	private Integer real_bill ;	
	private Integer order_balance ;	
	private Integer real_balance ;	
	private Integer can_trans_balance ;	
	private Integer can_refund_balance ;	
	private Integer inactive_balance ;	
	private Date open_time ;	
	private String area_id ;	
	private String county_id ;	
	
	/**
	 * default empty constructor
	 */
	public CAcctAcctitemHis() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
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
	
	// active_balance getter and setter
	public Integer getActive_balance(){
		return this.active_balance ;
	}
	
	public void setActive_balance(Integer active_balance){
		this.active_balance = active_balance ;
	}
	
	// owe_fee getter and setter
	public Integer getOwe_fee(){
		return this.owe_fee ;
	}
	
	public void setOwe_fee(Integer owe_fee){
		this.owe_fee = owe_fee ;
	}
	
	// real_fee getter and setter
	public Integer getReal_fee(){
		return this.real_fee ;
	}
	
	public void setReal_fee(Integer real_fee){
		this.real_fee = real_fee ;
	}
	
	// real_bill getter and setter
	public Integer getReal_bill(){
		return this.real_bill ;
	}
	
	public void setReal_bill(Integer real_bill){
		this.real_bill = real_bill ;
	}
	
	// order_balance getter and setter
	public Integer getOrder_balance(){
		return this.order_balance ;
	}
	
	public void setOrder_balance(Integer order_balance){
		this.order_balance = order_balance ;
	}
	
	// real_balance getter and setter
	public Integer getReal_balance(){
		return this.real_balance ;
	}
	
	public void setReal_balance(Integer real_balance){
		this.real_balance = real_balance ;
	}
	
	// can_trans_balance getter and setter
	public Integer getCan_trans_balance(){
		return this.can_trans_balance ;
	}
	
	public void setCan_trans_balance(Integer can_trans_balance){
		this.can_trans_balance = can_trans_balance ;
	}
	
	// can_refund_balance getter and setter
	public Integer getCan_refund_balance(){
		return this.can_refund_balance ;
	}
	
	public void setCan_refund_balance(Integer can_refund_balance){
		this.can_refund_balance = can_refund_balance ;
	}
	
	// inactive_balance getter and setter
	public Integer getInactive_balance(){
		return this.inactive_balance ;
	}
	
	public void setInactive_balance(Integer inactive_balance){
		this.inactive_balance = inactive_balance ;
	}
	
	// open_time getter and setter
	public Date getOpen_time(){
		return this.open_time ;
	}
	
	public void setOpen_time(Date open_time){
		this.open_time = open_time ;
	}
	
	// area_id getter and setter
	public String getArea_id(){
		return this.area_id ;
	}
	
	public void setArea_id(String area_id){
		this.area_id = area_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

}