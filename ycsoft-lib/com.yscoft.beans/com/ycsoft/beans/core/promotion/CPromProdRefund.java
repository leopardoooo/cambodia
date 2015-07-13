/**
 * CPromProdRefund.java	2013/03/28
 */
 
package com.ycsoft.beans.core.promotion; 

import java.io.Serializable ;
import java.util.Date;

import com.ycsoft.daos.config.POJO ;


/**
 * CPromProdRefund -> C_PROM_PROD_REFUND mapping 
 */
@POJO(
	tn="C_PROM_PROD_REFUND",
	sn="",
	pk="")
public class CPromProdRefund implements Serializable {
	
	// CPromProdRefund all properties 

	/**
	 * 
	 */
	private static final long serialVersionUID = -8746337569602285171L;
	private String prom_fee_sn ;	
	private String prod_sn ;	
	private Integer done_code;
	private Integer refund_pay;
	
	private Date create_time;	
	private String prom_fee_name ;
	private String prom_fee_id;
	/**
	 * default empty constructor
	 */
	public CPromProdRefund() {}
	
	
	// prom_fee_sn getter and setter
	public String getProm_fee_sn(){
		return this.prom_fee_sn ;
	}
	
	public void setProm_fee_sn(String prom_fee_sn){
		this.prom_fee_sn = prom_fee_sn ;
	}
	
	// prod_sn getter and setter
	public String getProd_sn(){
		return this.prod_sn ;
	}
	
	public void setProd_sn(String prod_sn){
		this.prod_sn = prod_sn ;
	}



	
	public Integer getDone_code() {
		return done_code;
	}


	public void setDone_code(Integer done_code) {
		this.done_code = done_code;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	public String getProm_fee_name() {
		return prom_fee_name;
	}


	public void setProm_fee_name(String prom_fee_name) {
		this.prom_fee_name = prom_fee_name;
	}


	public String getProm_fee_id() {
		return prom_fee_id;
	}


	public void setProm_fee_id(String prom_fee_id) {
		this.prom_fee_id = prom_fee_id;
	}


	public Integer getRefund_pay() {
		return refund_pay;
	}


	public void setRefund_pay(Integer refund_pay) {
		this.refund_pay = refund_pay;
	}
	

}