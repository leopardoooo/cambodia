package com.ycsoft.beans.core.prod;

/**
 * CProdMobileBill.java	2011/12/08
 */
 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CProdMobileBill -> C_PROD_MOBILE_BILL mapping 
 */
@POJO(
	tn="C_PROD_MOBILE_BILL",
	sn="",
	pk="")
public class CProdMobileBill implements Serializable {
	
	// CProdMobileBill all properties 

	private String cust_no ;	
	private String cust_name ;	
	private String user_id ;	
	private String acctitem_id ;	
	private String prod_name ;	
	private Integer fee ;	
	private Integer done_code ;	
	private String create_time ;	
	private String invoice_id ;	
	private String invoice_book_id ;	
	private Date done_date ;	
	private Integer create_done_code ;	
	
	/**
	 * default empty constructor
	 */
	public CProdMobileBill() {}
	
	
	// cust_no getter and setter
	public String getCust_no(){
		return this.cust_no ;
	}
	
	public void setCust_no(String cust_no){
		this.cust_no = cust_no ;
	}
	
	// cust_name getter and setter
	public String getCust_name(){
		return this.cust_name ;
	}
	
	public void setCust_name(String cust_name){
		this.cust_name = cust_name ;
	}
	
	// user_id getter and setter
	public String getUser_id(){
		return this.user_id ;
	}
	
	public void setUser_id(String user_id){
		this.user_id = user_id ;
	}
	
	// acctitem_id getter and setter
	public String getAcctitem_id(){
		return this.acctitem_id ;
	}
	
	public void setAcctitem_id(String acctitem_id){
		this.acctitem_id = acctitem_id ;
	}
	
	// prod_name getter and setter
	public String getProd_name(){
		return this.prod_name ;
	}
	
	public void setProd_name(String prod_name){
		this.prod_name = prod_name ;
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
	
	
	public String getCreate_time() {
		return create_time;
	}


	public void setCreate_time(String createTime) {
		create_time = createTime;
	}


	// invoice_id getter and setter
	public String getInvoice_id(){
		return this.invoice_id ;
	}
	
	public void setInvoice_id(String invoice_id){
		this.invoice_id = invoice_id ;
	}
	
	// invoice_book_id getter and setter
	public String getInvoice_book_id(){
		return this.invoice_book_id ;
	}
	
	public void setInvoice_book_id(String invoice_book_id){
		this.invoice_book_id = invoice_book_id ;
	}
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// create_done_code getter and setter
	public Integer getCreate_done_code(){
		return this.create_done_code ;
	}
	
	public void setCreate_done_code(Integer create_done_code){
		this.create_done_code = create_done_code ;
	}

}