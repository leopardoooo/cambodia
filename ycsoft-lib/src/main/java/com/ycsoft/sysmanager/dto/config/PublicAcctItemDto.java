package com.ycsoft.sysmanager.dto.config;

import com.ycsoft.beans.config.TPublicAcctitem;

public class PublicAcctItemDto extends TPublicAcctitem {

	/**
	 *
	 */
	private static final long serialVersionUID = 7997530542786309854L;

	private String printitem_name;
	private String prodIds;
	private String prodNames;


	public String getPrintitem_name() {
		return printitem_name;
	}

	public void setPrintitem_name(String printitem_name) {
		this.printitem_name = printitem_name;
	}

	public String getProdIds() {
		return prodIds;
	}

	public void setProdIds(String prodIds) {
		this.prodIds = prodIds;
	}

	public String getProdNames() {
		return prodNames;
	}

	public void setProdNames(String prodNames) {
		this.prodNames = prodNames;
	}

}
