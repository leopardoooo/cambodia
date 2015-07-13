/**
 * RInvoiceTransfer.java	2010/11/08
 */

package com.ycsoft.beans.invoice;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * RInvoiceTransfer -> R_INVOICE_TRANSFER mapping
 */
@POJO(
	tn="R_INVOICE_TRANSFER",
	sn="",
	pk="")
public class RInvoiceTransfer implements Serializable {

	// RInvoiceTransfer all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 4550081887206363215L;
	private Integer done_code ;
	private String optr_id ;
	private Date create_time ;
	private String remark ;
	private Integer invoice_count ;
	private String invoice_type ;
	private String source_depot_id ;
	private String order_depot_id ;
	private String optr_type;

	/**
	 * default empty constructor
	 */
	public RInvoiceTransfer() {}


	// done_code getter and setter
	public Integer getDone_code(){
		return done_code ;
	}

	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}

	// optr_id getter and setter
	public String getOptr_id(){
		return optr_id ;
	}

	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

	// create_time getter and setter
	public Date getCreate_time(){
		return create_time ;
	}

	public void setCreate_time(Date create_time){
		this.create_time = create_time ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}

	// invoice_count getter and setter
	public Integer getInvoice_count(){
		return invoice_count ;
	}

	public void setInvoice_count(Integer invoice_count){
		this.invoice_count = invoice_count ;
	}

	// invoice_type getter and setter
	public String getInvoice_type(){
		return invoice_type ;
	}

	public void setInvoice_type(String invoice_type){
		this.invoice_type = invoice_type ;
	}

	// source_depot_id getter and setter
	public String getSource_depot_id(){
		return source_depot_id ;
	}

	public void setSource_depot_id(String source_depot_id){
		this.source_depot_id = source_depot_id ;
	}

	// order_depot_id getter and setter
	public String getOrder_depot_id(){
		return order_depot_id ;
	}

	public void setOrder_depot_id(String order_depot_id){
		this.order_depot_id = order_depot_id ;
	}


	public String getOptr_type() {
		return optr_type;
	}


	public void setOptr_type(String optr_type) {
		this.optr_type = optr_type;
	}
}