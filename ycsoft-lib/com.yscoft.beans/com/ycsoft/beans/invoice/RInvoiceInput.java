/**
 * RInvoiceInput.java	2010/11/08
 */

package com.ycsoft.beans.invoice;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * RInvoiceInput -> R_INVOICE_INPUT mapping
 */
@POJO(
	tn="R_INVOICE_INPUT",
	sn="SEQ_DONE_CODE",
	pk="DONE_CODE")
public class RInvoiceInput implements Serializable {

	// RInvoiceInput all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -500073262616061322L;
	private Integer done_code ;
	private String invoice_type ;
	private Integer invoice_count ;
	private Date create_time ;
	private String optr_id ;
	private String remark ;
	private String depot_id ;

	/**
	 * default empty constructor
	 */
	public RInvoiceInput() {}


	// done_code getter and setter
	public Integer getDone_code(){
		return done_code ;
	}

	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}

	// invoice_type getter and setter
	public String getInvoice_type(){
		return invoice_type ;
	}

	public void setInvoice_type(String invoice_type){
		this.invoice_type = invoice_type ;
	}

	// invoice_count getter and setter
	public Integer getInvoice_count(){
		return invoice_count ;
	}

	public void setInvoice_count(Integer invoice_count){
		this.invoice_count = invoice_count ;
	}

	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

	// optr_id getter and setter
	public String getOptr_id(){
		return optr_id ;
	}

	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}

	// depot_id getter and setter
	public String getDepot_id(){
		return depot_id ;
	}

	public void setDepot_id(String depot_id){
		this.depot_id = depot_id ;
	}

}