/**
 * BBillWriteoff.java	2011/04/11
 */
 
package com.ycsoft.beans.core.bill; 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * BBillWriteoff -> B_BILL_WRITEOFF mapping 
 */
@POJO(
	tn="B_BILL_WRITEOFF",
	sn="SEQ_WRITEOFF_SN",
	pk="")
public class BBillWriteoff implements Serializable {
	
	// BBillWriteoff all properties 

	private Integer done_code ;	
	private String writeoff_sn ;	
	private String bill_sn ;	
	private String cust_id ;	
	private String acct_id ;	
	private String acctitem_id ;	
	private String fee_type ;
	private String fee_type_text ;
	private Date writeoff_date ;	
	private Integer fee ;	
	private String area_id ;	
	private String county_id ;	
	private String status ;	
	private Integer cancel_done_code ;	
	private String writeoff_type ;	
	private String busi_code ;	
	private String years ;	
	private String bill_acctitem_id ;
	private String bill_tariff_id;
	private String addr_id ;
	
	
	
	/**
	 * default empty constructor
	 */
	public BBillWriteoff() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// writeoff_sn getter and setter
	public String getWriteoff_sn(){
		return this.writeoff_sn ;
	}
	
	public void setWriteoff_sn(String writeoff_sn){
		this.writeoff_sn = writeoff_sn ;
	}
	
	// bill_sn getter and setter
	public String getBill_sn(){
		return this.bill_sn ;
	}
	
	public void setBill_sn(String bill_sn){
		this.bill_sn = bill_sn ;
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
		this.fee_type = fee_type ;
		fee_type_text = MemoryDict.getDictName(DictKey.PAY_TYPE, fee_type);
	}
	
	// writeoff_date getter and setter
	public Date getWriteoff_date(){
		return this.writeoff_date ;
	}
	
	public void setWriteoff_date(Date writeoff_date){
		this.writeoff_date = writeoff_date ;
	}
	
	// fee getter and setter
	public Integer getFee(){
		return this.fee ;
	}
	
	public void setFee(Integer fee){
		this.fee = fee ;
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
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// cancel_done_code getter and setter
	public Integer getCancel_done_code(){
		return this.cancel_done_code ;
	}
	
	public void setCancel_done_code(Integer cancel_done_code){
		this.cancel_done_code = cancel_done_code ;
	}
	
	// writeoff_type getter and setter
	public String getWriteoff_type(){
		return this.writeoff_type ;
	}
	
	public void setWriteoff_type(String writeoff_type){
		this.writeoff_type = writeoff_type ;
	}
	
	// busi_code getter and setter
	public String getBusi_code(){
		return this.busi_code ;
	}
	
	public void setBusi_code(String busi_code){
		this.busi_code = busi_code ;
	}
	
	// years getter and setter
	public String getYears(){
		return this.years ;
	}
	
	public void setYears(String years){
		this.years = years ;
	}


	/**
	 * @return the addr_id
	 */
	public String getAddr_id() {
		return addr_id;
	}


	/**
	 * @param addr_id the addr_id to set
	 */
	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}


	/**
	 * @return the bill_acctitem_id
	 */
	public String getBill_acctitem_id() {
		return bill_acctitem_id;
	}


	/**
	 * @param bill_acctitem_id the bill_acctitem_id to set
	 */
	public void setBill_acctitem_id(String bill_acctitem_id) {
		this.bill_acctitem_id = bill_acctitem_id;
	}


	/**
	 * @return the bill_tariff_id
	 */
	public String getBill_tariff_id() {
		return bill_tariff_id;
	}


	/**
	 * @param bill_tariff_id the bill_tariff_id to set
	 */
	public void setBill_tariff_id(String bill_tariff_id) {
		this.bill_tariff_id = bill_tariff_id;
	}


	/**
	 * @return the fee_type_text
	 */
	public String getFee_type_text() {
		return fee_type_text;
	}


	/**
	 * @param fee_type_text the fee_type_text to set
	 */
	public void setFee_type_text(String fee_type_text) {
		this.fee_type_text = fee_type_text;
	}

}