/**
 * CFeeUnitpre.java	2010/10/11
 */

package com.ycsoft.beans.core.fee;

import java.io.Serializable;

import com.ycsoft.daos.config.POJO;


/**
 * CFeeUnitpre -> C_FEE_UNITPRE mapping
 */
@POJO(
	tn="C_FEE_UNITPRE",
	sn="SEQ_FEE_SN",
	pk="")
public class CFeeUnitpre extends CFee implements Serializable {

	// CFeeUnitpre all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 5527984767537549293L;
	private String prefee_no ;
	private String cust_name ;
	private String remark ;
	private Integer used ;

	/**
	 * default empty constructor
	 */
	public CFeeUnitpre() {}


	// prefee_no getter and setter
	public String getPrefee_no(){
		return prefee_no ;
	}

	public void setPrefee_no(String prefee_no){
		this.prefee_no = prefee_no ;
	}

	// cust_name getter and setter
	public String getCust_name(){
		return cust_name ;
	}

	public void setCust_name(String cust_name){
		this.cust_name = cust_name ;
	}

	// remark getter and setter
	public String getRemark(){
		return remark ;
	}

	public void setRemark(String remark){
		this.remark = remark ;
	}

	// used getter and setter
	public Integer getUsed(){
		return used ;
	}

	public void setUsed(Integer used){
		this.used = used ;
	}

}