/**
 * JBandCommand.java	2011/05/09
 */
 
package com.ycsoft.business.dto.core.prod; 

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;



public class JBandCommandDto extends JBandCommand implements Serializable {
	
	// JBandCommand all properties 

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String login_name="";
	private String supplier_name="";
	private String server_name="";

	

	public String getLogin_name() {
		return login_name;
	}



	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}



	public String getSupplier_name() {
		return supplier_name;
	}



	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}



	public String getServer_name() {
		return server_name;
	}



	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}



	public JBandCommandDto() {}
	
	 
}