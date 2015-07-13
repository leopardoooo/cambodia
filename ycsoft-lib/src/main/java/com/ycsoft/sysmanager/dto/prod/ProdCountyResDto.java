package com.ycsoft.sysmanager.dto.prod;

import com.ycsoft.beans.prod.PProdCountyRes;

public class ProdCountyResDto extends PProdCountyRes {
	/**
	 *
	 */
	private static final long serialVersionUID = 4689890052542428904L;
	private String  res_name;
	private String prod_name;
	private String serverIds;
	public String getRes_name() {
		return res_name;
	}
	public void setRes_name(String res_name) {
		this.res_name = res_name;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getServerIds() {
		return serverIds;
	}
	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

}
