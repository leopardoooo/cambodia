/**
 * CVoucherDoneVoucherid.java	2011/03/23
 */
 
package com.ycsoft.beans.core.voucher; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * CVoucherDoneVoucherid -> C_VOUCHER_DONE_VOUCHERID mapping 
 */
@POJO(
	tn="C_VOUCHER_DONE_VOUCHERID",
	sn="",
	pk="")
public class CVoucherDoneVoucherid implements Serializable {
	
	// CVoucherDoneVoucherid all properties 

	private Integer voucher_done_code ;	
	private String voucher_id ;	
	
	/**
	 * default empty constructor
	 */
	public CVoucherDoneVoucherid() {}
	
	
	// voucher_done_code getter and setter
	public Integer getVoucher_done_code(){
		return this.voucher_done_code ;
	}
	
	public void setVoucher_done_code(Integer voucher_done_code){
		this.voucher_done_code = voucher_done_code ;
	}
	
	// voucher_id getter and setter
	public String getVoucher_id(){
		return this.voucher_id ;
	}
	
	public void setVoucher_id(String voucher_id){
		this.voucher_id = voucher_id ;
	}

}