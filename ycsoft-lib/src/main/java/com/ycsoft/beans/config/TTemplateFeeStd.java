/**
 * TTemplateFeeStd.java	2013/01/14
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * TTemplateFeeStd -> T_TEMPLATE_FEE_STD mapping 
 */
@POJO(
	tn="T_TEMPLATE_FEE_STD",
	sn="",
	pk="")
public class TTemplateFeeStd implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7161364291746100509L;
	// TTemplateColumn all properties 

	private String fee_std_id;
	private Integer column_id;
	
	/**
	 * default empty constructor
	 */
	public TTemplateFeeStd() {}

	public String getFee_std_id() {
		return fee_std_id;
	}

	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}

	public Integer getColumn_id() {
		return column_id;
	}

	public void setColumn_id(Integer column_id) {
		this.column_id = column_id;
	}
	
}