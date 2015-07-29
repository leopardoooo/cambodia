/**
 * PPackageProd.java	2010/07/05
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PPackageProd -> P_PACKAGE_PROD mapping
 */
@POJO(tn = "P_PACKAGE_PROD", sn = "", pk = "")
public class PPackageProd implements Serializable {

	// PPackageProd all properties

	/**
	 * 
	 */ 
	private static final long serialVersionUID = -2407441487593995420L;
	private String package_id;
	private String prod_id;
	private String tariff_id;
	private Integer max_prod_count;
	private Integer percent;
	private Float percent_value;
	private String package_tariff_id;
	private String type;

	private String package_tariff_name;
	private String tariff_name;
	private String prod_name;
	private PProd prod;
	private PProdTariff prodTariff;
	private String type_text;

	/**
	 * default empty constructor
	 */
	public PPackageProd() {
	}
	public PPackageProd(String packageId,String prodId,String tariffId,Integer maxProdCount,Integer percentId) {
		package_id = packageId;
		prod_id = prodId;
		tariff_id = tariffId;
		max_prod_count = maxProdCount;
		percent = percentId;
	}
	// package_id getter and setter
	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	// prod_id getter and setter
	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// max_prod_count getter and setter
	public Integer getMax_prod_count() {
		return max_prod_count;
	}

	public void setMax_prod_count(Integer max_prod_count) {
		this.max_prod_count = max_prod_count;
	}

	public PProd getProd() {
		return prod;
	}

	public void setProd(PProd prod) {
		this.prod = prod;
	}

	public PProdTariff getProdTariff() {
		return prodTariff;
	}

	public void setProdTariff(PProdTariff prodTariff) {
		this.prodTariff = prodTariff;
	}

	public String getTariff_name() {
		return tariff_name;
	}

	public void setTariff_name(String tariff_name) {
		this.tariff_name = tariff_name;
	}
	public Integer getPercent() {
		return percent;
	}
	public void setPercent(Integer percent) {
		this.percent = percent;
	}
	public Float getPercent_value() {
		return percent_value;
	}
	public void setPercent_value(Float percentValue) {
		percent_value = percentValue;
	}
	public String getPackage_tariff_id() {
		return package_tariff_id;
	}
	public void setPackage_tariff_id(String packageTariffId) {
		package_tariff_id = packageTariffId;
	}
	public String getPackage_tariff_name() {
		return package_tariff_name;
	}
	public void setPackage_tariff_name(String packageTariffName) {
		package_tariff_name = packageTariffName;
	}
	/**
	 * @return the prod_name
	 */
	public String getProd_name() {
		return prod_name;
	}
	/**
	 * @param prodName the prod_name to set
	 */
	public void setProd_name(String prodName) {
		prod_name = prodName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		this.type_text = MemoryDict.getDictName(DictKey.SEPARATE_TYPE, type);
	}
	public String getType_text() {
		return type_text;
	}

}