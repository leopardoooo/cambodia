/**
 * CBankReturnPayerror.java	2013/09/05
 */
 
package com.ycsoft.beans.core.bank; 

import java.io.Serializable ;
import java.util.Date ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CBankReturnPayerror -> C_BANK_RETURN_PAYERROR mapping 
 */
@POJO(
	tn="C_BANK_RETURN_PAYERROR",
	sn="",
	pk="bank_trans_sn")
public class CBankReturnPayerror implements Serializable {
	
	// CBankReturnPayerror all properties 

	private String bank_trans_sn ;	
	private String refund_status ;	
	private String refund_bank_sn ;	
	private String trans_sn ;	
	private String busi_type ;	
	private String company_code ;	
	private String cust_no ;	
	private String bank_code ;	
	private String bank_account ;	
	private String qc ;	
	private String xjfg ;	
	private Integer need_fee ;	
	private Integer real_fee ;	
	private String is_success ;	
	private String failure_reason ;	
	private String pay_status ;	
	private Integer pay_done_code ;	
	private String pay_failure_reason ;	
	private Date trans_time ;	
	private Date create_time ;	
	private String bank_fee_name;
	private String cancel_done_code;
	

	public String getCancel_done_code() {
		return cancel_done_code;
	}


	public void setCancel_done_code(String cancel_done_code) {
		this.cancel_done_code = cancel_done_code;
	}


	/**
	 * default empty constructor
	 */
	public CBankReturnPayerror() {}
	
	
	// bank_trans_sn getter and setter
	public String getBank_trans_sn(){
		return this.bank_trans_sn ;
	}
	
	public void setBank_trans_sn(String bank_trans_sn){
		this.bank_trans_sn = bank_trans_sn ;
	}
	
	// refund_status getter and setter
	public String getRefund_status(){
		return this.refund_status ;
	}
	
	public void setRefund_status(String refund_status){
		this.refund_status = refund_status ;
	}
	
	// refund_bank_sn getter and setter
	public String getRefund_bank_sn(){
		return this.refund_bank_sn ;
	}
	
	public void setRefund_bank_sn(String refund_bank_sn){
		this.refund_bank_sn = refund_bank_sn ;
	}
	
	// trans_sn getter and setter
	public String getTrans_sn(){
		return this.trans_sn ;
	}
	
	public void setTrans_sn(String trans_sn){
		this.trans_sn = trans_sn ;
	}
	
	// busi_type getter and setter
	public String getBusi_type(){
		return this.busi_type ;
	}
	
	public void setBusi_type(String busi_type){
		this.busi_type = busi_type ;
	}
	
	// company_code getter and setter
	public String getCompany_code(){
		return this.company_code ;
	}
	
	public void setCompany_code(String company_code){
		this.company_code = company_code ;
	}
	
	// cust_no getter and setter
	public String getCust_no(){
		return this.cust_no ;
	}
	
	public void setCust_no(String cust_no){
		this.cust_no = cust_no ;
	}
	
	// bank_code getter and setter
	public String getBank_code(){
		return this.bank_code ;
	}
	
	public void setBank_code(String bank_code){
		this.bank_code = bank_code ;
	}
	
	// bank_account getter and setter
	public String getBank_account(){
		return this.bank_account ;
	}
	
	public void setBank_account(String bank_account){
		this.bank_account = bank_account ;
	}
	
	// qc getter and setter
	public String getQc(){
		return this.qc ;
	}
	
	public void setQc(String qc){
		this.qc = qc ;
	}
	
	// xjfg getter and setter
	public String getXjfg(){
		return this.xjfg ;
	}
	
	public void setXjfg(String xjfg){
		this.xjfg = xjfg ;
	}
	
	// need_fee getter and setter
	public Integer getNeed_fee(){
		return this.need_fee ;
	}
	
	public void setNeed_fee(Integer need_fee){
		this.need_fee = need_fee ;
	}
	
	// real_fee getter and setter
	public Integer getReal_fee(){
		return this.real_fee ;
	}
	
	public void setReal_fee(Integer real_fee){
		this.real_fee = real_fee ;
	}
	
	// is_success getter and setter
	public String getIs_success(){
		return this.is_success ;
	}
	
	public void setIs_success(String is_success){
		this.is_success = is_success ;
	}
	
	// failure_reason getter and setter
	public String getFailure_reason(){
		return this.failure_reason ;
	}
	
	public void setFailure_reason(String failure_reason){
		this.failure_reason = failure_reason ;
	}
	
	// pay_status getter and setter
	public String getPay_status(){
		return this.pay_status ;
	}
	
	public void setPay_status(String pay_status){
		this.pay_status = pay_status ;
	}
	
	// pay_done_code getter and setter
	public Integer getPay_done_code(){
		return this.pay_done_code ;
	}
	
	public void setPay_done_code(Integer pay_done_code){
		this.pay_done_code = pay_done_code ;
	}
	
	// pay_failure_reason getter and setter
	public String getPay_failure_reason(){
		return this.pay_failure_reason ;
	}
	
	public void setPay_failure_reason(String pay_failure_reason){
		this.pay_failure_reason = pay_failure_reason ;
	}
	
	// trans_time getter and setter
	public Date getTrans_time(){
		return this.trans_time ;
	}
	
	public void setTrans_time(Date trans_time){
		this.trans_time = trans_time ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}


	public String getBank_fee_name() {
		return bank_fee_name;
	}

	public void setBank_fee_name(String bank_fee_name) {
		this.bank_fee_name = bank_fee_name;
	}

}