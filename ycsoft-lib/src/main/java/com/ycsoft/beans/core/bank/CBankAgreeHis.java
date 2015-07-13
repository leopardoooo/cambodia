/**
 * CBankAgreeHis.java	2010/11/01
 */

package com.ycsoft.beans.core.bank;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * CBankAgreeHis -> C_BANK_AGREE_HIS mapping
 */
@POJO(
	tn="C_BANK_AGREE_HIS",
	sn="",
	pk="")
public class CBankAgreeHis extends CBankAgree implements Serializable {

	// CBankAgreeHis all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6045796435722165018L;
	private String cancel_done_code ;

	/**
	 * default empty constructor
	 */
	public CBankAgreeHis() {}

	// cancel_done_code getter and setter
	public String getCancel_done_code(){
		return cancel_done_code ;
	}

	public void setCancel_done_code(String cancel_done_code){
		this.cancel_done_code = cancel_done_code ;
	}

}