/**
 * TPayType.java	2010/11/11
 */

package com.ycsoft.beans.config;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * TPayType -> T_PAY_TYPE mapping
 */
@POJO(
	tn="T_PAY_TYPE",
	sn="",
	pk="PAY_TYPE")
public class TPayType implements Serializable {

	// TPayType all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6520816423735110274L;
	private String pay_type ;
	private String pay_type_name ;
	private String acct_feetype ;
	private String is_print;

	/**
	 * @return the is_print
	 */
	public String getIs_print() {
		return is_print;
	}


	/**
	 * @param is_print the is_print to set
	 */
	public void setIs_print(String is_print) {
		this.is_print = is_print;
	}


	/**
	 * default empty constructor
	 */
	public TPayType() {}


	// pay_type getter and setter
	public String getPay_type(){
		return pay_type ;
	}

	public void setPay_type(String pay_type){
		this.pay_type = pay_type ;
	}

	// pay_type_name getter and setter
	public String getPay_type_name(){
		return pay_type_name ;
	}

	public void setPay_type_name(String pay_type_name){
		this.pay_type_name = pay_type_name ;
	}

	// acct_feetype getter and setter
	public String getAcct_feetype(){
		return acct_feetype ;
	}

	public void setAcct_feetype(String acct_feetype){
		this.acct_feetype = acct_feetype ;
	}

}