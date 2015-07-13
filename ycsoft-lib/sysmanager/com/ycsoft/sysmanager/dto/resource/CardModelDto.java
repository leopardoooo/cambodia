package com.ycsoft.sysmanager.dto.resource;

import com.ycsoft.beans.device.RCardModel;

public class CardModelDto extends RCardModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 9065250604264317362L;
	private String ca_supplier_name;

	/**
	 * @return the ca_supplier_name
	 */
	public String getCa_supplier_name() {
		return ca_supplier_name;
	}

	/**
	 * @param ca_supplier_name the ca_supplier_name to set
	 */
	public void setCa_supplier_name(String ca_supplier_name) {
		this.ca_supplier_name = ca_supplier_name;
	}


}
