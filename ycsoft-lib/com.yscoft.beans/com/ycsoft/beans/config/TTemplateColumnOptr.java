/**
 * TTemplateColumnOptr.java	2013/01/14
 */
 
package com.ycsoft.beans.config; 

import java.io.Serializable ;
import com.ycsoft.daos.config.POJO ;


/**
 * TTemplateColumnOptr -> T_TEMPLATE_COLUMN_OPTR mapping 
 */
@POJO(
	tn="T_TEMPLATE_COLUMN_OPTR",
	sn="",
	pk="")
public class TTemplateColumnOptr implements Serializable {
	
	// TTemplateColumnOptr all properties 

	private String column_id ;	
	private String optr_id ;	
	
	/**
	 * default empty constructor
	 */
	public TTemplateColumnOptr() {}
	
	
	// column_id getter and setter
	public String getColumn_id(){
		return this.column_id ;
	}
	
	public void setColumn_id(String column_id){
		this.column_id = column_id ;
	}
	
	// optr_id getter and setter
	public String getOptr_id(){
		return this.optr_id ;
	}
	
	public void setOptr_id(String optr_id){
		this.optr_id = optr_id ;
	}

}