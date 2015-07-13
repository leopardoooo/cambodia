/**
 * RInvoiceFeelist.java	2012/10/08
 */
 
package com.ycsoft.beans.invoice; 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * RInvoiceFeelist -> R_INVOICE_FEELIST mapping 
 */
@POJO(
	tn="R_INVOICE_FEELIST",
	sn="",
	pk="")
public class RInvoiceFeelist implements Serializable {
	
	// RInvoiceFeelist all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -1910882528849365539L;
	private Integer done_code ;	
	private String feelist_id ;	
	private String feelist_code ;	
	private String feelist_book_id ;	
	private String feelist_type ;	
	private String invoice_id ;	
	private String invoice_code ;	
	private Integer amount ;	
	private String cust_id ;	
	private Date create_time ;	
	private String status ;	
	private Date status_time ;	
	private String optr_id ;	
	private String county_id ;	
	private String remark ;	
	
	/**
	 * default empty constructor
	 */
	public RInvoiceFeelist() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}
	
	// feelist_id getter and setter
	public String getFeelist_id(){
		return this.feelist_id ;
	}
	
	public void setFeelist_id(String feelist_id){
		this.feelist_id = feelist_id ;
	}
	
	// feelist_code getter and setter
	public String getFeelist_code(){
		return this.feelist_code ;
	}
	
	public void setFeelist_code(String feelist_code){
		this.feelist_code = feelist_code ;
	}
	
	// feelist_book_id getter and setter
	public String getFeelist_book_id(){
		return this.feelist_book_id ;
	}
	
	public void setFeelist_book_id(String feelist_book_id){
		this.feelist_book_id = feelist_book_id ;
	}
	
	// feelist_type getter and setter
	public String getFeelist_type(){
		return this.feelist_type ;
	}
	
	public void setFeelist_type(String feelist_type){
		this.feelist_type = feelist_type ;
	}
	
	// invoice_id getter and setter
	public String getInvoice_id(){
		return this.invoice_id ;
	}
	
	public void setInvoice_id(String invoice_id){
		this.invoice_id = invoice_id ;
	}
	
	// invoice_code getter and setter
	public String getInvoice_code(){
		return this.invoice_code ;
	}
	
	public void setInvoice_code(String invoice_code){
		this.invoice_code = invoice_code ;
	}
	
	// amount getter and setter
	public Integer getAmount(){
		return this.amount ;
	}
	
	public void setAmount(Integer amount){
		this.amount = amount ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// create_time getter and setter
	public Date getCreate_time(){
		return this.create_time ;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
	}
	
	// status_time getter and setter
	public Date getStatus_time(){
		return this.status_time ;
	}
	
	public void setStatus_time(Date status_time){
		this.status_time = status_time ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}
	
	// county_id getter and setter
	public String getCounty_id(){
		return this.county_id ;
	}
	
	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}
	
	// remark getter and setter
	public String getRemark(){
		return this.remark ;
	}
	
	public void setRemark(String remark){
		this.remark = remark ;
	}

}