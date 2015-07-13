/**
 * RInvoiceDetail.java	2010/11/08
 */

package com.ycsoft.beans.invoice;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * RInvoiceDetail -> R_INVOICE_DETAIL mapping
 */
@POJO(
	tn="R_INVOICE_DETAIL",
	sn="",
	pk="")
public class RInvoiceDetail implements Serializable {

	// RInvoiceDetail all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 8439284478972214818L;
	private Integer done_code ;
	private String invoice_id ;
	private String invoice_code ;

	/**
	 * default empty constructor
	 */
	public RInvoiceDetail() {}


	// done_code getter and setter
	public Integer getDone_code(){
		return done_code ;
	}

	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}

	// invoice_id getter and setter
	public String getInvoice_id(){
		return invoice_id ;
	}

	public void setInvoice_id(String invoice_id){
		this.invoice_id = invoice_id ;
	}

	// invoice_code getter and setter
	public String getInvoice_code(){
		return invoice_code ;
	}

	public void setInvoice_code(String invoice_code){
		this.invoice_code = invoice_code ;
	}

}