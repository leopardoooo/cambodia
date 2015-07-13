package com.ycsoft.beans.prod;

/**
 * PPackageProdHis.java	2011/09/26
 */
 

import java.io.Serializable ;
import java.util.Date ;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO ;


/**
 * PPackageProdHis -> P_PACKAGE_PROD_HIS mapping 
 */
@POJO(
	tn="P_PACKAGE_PROD_HIS",
	sn="",
	pk="")
public class PPackageProdHis extends PPackageProd implements Serializable {
	
	// PPackageProdHis all properties 

	private Date done_date ;	
	private String optr_id ;	
	
	private String optr_name;
	
	/**
	 * default empty constructor
	 */
	public PPackageProdHis() {}
	
	
	// done_date getter and setter
	public Date getDone_date(){
		return this.done_date ;
	}
	
	public void setDone_date(Date done_date){
		this.done_date = done_date ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
		this.optr_name = MemoryDict.getDictName(DictKey.OPTR, optr_id);
	}


	/**
	 * @return the optr_name
	 */
	public String getOptr_name() {
		return optr_name;
	}


	/**
	 * @param optrName the optr_name to set
	 */
	public void setOptr_name(String optrName) {
		optr_name = optrName;
	}

}