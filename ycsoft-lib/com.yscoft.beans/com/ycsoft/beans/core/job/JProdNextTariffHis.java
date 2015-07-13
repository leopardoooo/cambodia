package com.ycsoft.beans.core.job;

/**
 * JProdNextTariffHis.java	2011/04/06
 */
 

import java.io.Serializable ;
import java.util.Date ;
import com.ycsoft.daos.config.POJO ;


/**
 * JProdNextTariffHis -> J_PROD_NEXT_TARIFF_HIS mapping 
 */
@POJO(
	tn="J_PROD_NEXT_TARIFF_HIS",
	sn="",
	pk="")
public class JProdNextTariffHis extends JProdNextTariff implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6768510776401008848L;
	// JProdNextTariffHis all properties 
	private Date exec_time ;	
	
	/**
	 * default empty constructor
	 */
	public JProdNextTariffHis() {}
	
	
	// exec_time getter and setter
	public Date getExec_time(){
		return this.exec_time ;
	}
	
	public void setExec_time(Date exec_time){
		this.exec_time = exec_time ;
	}

}