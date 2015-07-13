/**
 * RInvoiceOptr.java	2010/11/08
 */

package com.ycsoft.beans.invoice;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;


/**
 * RInvoiceOptr -> R_INVOICE_OPTR mapping
 */
@POJO(
	tn="R_INVOICE_OPTR",
	sn="",
	pk="")
public class RInvoiceOptr implements Serializable {

	// RInvoiceOptr all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 1089554975731237714L;
	private Integer done_code ;
	private Integer invoice_count ;
	private String county_id ;
	private String dept_id ;
	private String optr_id ;
	private Date create_time ;
	private String remark ;
	private String optr_type ;

	/**
	 * default empty constructor
	 */
	public RInvoiceOptr() {}


	// done_code getter and setter
	public Integer getDone_code(){
		return done_code ;
	}

	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}

	// invoice_count getter and setter
	public Integer getInvoice_count(){
		return invoice_count ;
	}

	public void setInvoice_count(Integer invoice_count){
		this.invoice_count = invoice_count ;
	}

	// county_id getter and setter
	public String getCounty_id(){
		return county_id ;
	}

	public void setCounty_id(String county_id){
		this.county_id = county_id ;
	}

	// dept_id getter and setter
	public String getDept_id(){
		return dept_id ;
	}

	public void setDept_id(String dept_id){
		this.dept_id = dept_id ;
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

	// optr_type getter and setter
	public String getOptr_type(){
		return optr_type ;
	}

	public void setOptr_type(String optr_type){
		this.optr_type = optr_type ;
	}

}