/*
 * @(#) PrintOrderProdDto.java 1.0.0 Aug 5, 2011 10:49:08 AM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ycsoft.business.dto.print;



/**
 * 订购产品的业务单据数据格式
 *
 * @author allex
 * @since 1.0
 */
public class PrintOrderProdDto {

	private String prod_name;
	private String tariff_name;
	
	private String card_id;
	
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getTariff_name() {
		return tariff_name;
	}
	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
}
