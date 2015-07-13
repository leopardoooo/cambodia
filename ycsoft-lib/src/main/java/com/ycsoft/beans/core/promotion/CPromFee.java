package com.ycsoft.beans.core.promotion;

/**
 * CPromFee.java	2012/07/11
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * CPromFee -> C_PROM_FEE mapping 
 */
@POJO(
	tn="C_PROM_FEE",
	sn="SEQ_PROMOTION_SN",
	pk="prom_fee_sn")
public class CPromFee  extends BusiBase implements Serializable {
	
	// CPromFee all properties 

	private String prom_fee_sn ;
	private String prom_fee_id ;	
	private String cust_id ;	
	private String status ;	
	
	private String prom_fee_name;
	private Integer prom_fee;
	private String status_text;
	private Integer cancel_done_code;
	/**
	 * default empty constructor
	 */
	public CPromFee() {}
	
	
	public String getStatus_text() {
		return status_text;
	}


	public void setStatus_text(String statusText) {
		status_text = statusText;
	}


	public Integer getProm_fee() {
		return prom_fee;
	}


	public void setProm_fee(Integer promFee) {
		prom_fee = promFee;
	}


	public String getProm_fee_name() {
		return prom_fee_name;
	}


	public void setProm_fee_name(String promFeeName) {
		prom_fee_name = promFeeName;
	}


	// prom_fee_sn getter and setter
	public String getProm_fee_sn(){
		return this.prom_fee_sn ;
	}
	
	public void setProm_fee_sn(String prom_fee_sn){
		this.prom_fee_sn = prom_fee_sn ;
	}
	
	// prom_fee_id getter and setter
	public String getProm_fee_id(){
		return this.prom_fee_id ;
	}
	
	public void setProm_fee_id(String prom_fee_id){
		this.prom_fee_id = prom_fee_id ;
	}
	
	// cust_id getter and setter
	public String getCust_id(){
		return this.cust_id ;
	}
	
	public void setCust_id(String cust_id){
		this.cust_id = cust_id ;
	}
	
	// status getter and setter
	public String getStatus(){
		return this.status ;
	}
	
	public void setStatus(String status){
		this.status = status ;
		status_text = MemoryDict.getDictName(DictKey.STATUS, this.status);
	}

	public Integer getCancel_done_code() {
		return cancel_done_code;
	}

	public void setCancel_done_code(Integer cancel_done_code) {
		this.cancel_done_code = cancel_done_code;
	}
}