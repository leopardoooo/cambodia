package com.ycsoft.beans.core.acct;

/**
 * CGeneralContractHis.java	2011/04/19
 */
 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * CGeneralContractHis -> C_GENERAL_CONTRACT_HIS mapping 
 */
@POJO(
	tn="C_GENERAL_CONTRACT_HIS",
	sn="",
	pk="")
public class CGeneralContractHis extends CGeneralContract implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8775883837494995567L;
	// CGeneralContractHis all properties 
	private Integer done_code ;	
	
	/**
	 * default empty constructor
	 */
	public CGeneralContractHis() {}
	
	
	// done_code getter and setter
	public Integer getDone_code(){
		return this.done_code ;
	}
	
	public void setDone_code(Integer done_code){
		this.done_code = done_code ;
	}

}