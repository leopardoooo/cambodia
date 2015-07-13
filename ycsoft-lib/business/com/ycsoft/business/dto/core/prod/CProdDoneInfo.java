package com.ycsoft.business.dto.core.prod;


public class CProdDoneInfo  {
	/**
	 *
	 */
	private static final long serialVersionUID = -2590105154516096344L;
	private String prod_name; // 产品名
	private String tariff_name;// 资费名 
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	
}
