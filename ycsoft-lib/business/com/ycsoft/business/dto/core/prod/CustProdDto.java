/**
 *
 */
package com.ycsoft.business.dto.core.prod;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

/**
 *
 * @author pyb
 *
 * Sep 27, 2010
 *
 */
public class CustProdDto {
	private String cust_id;
	private String user_id;
	private String cust_name;
	private String user_type;
	private String prod_sn;
	private String prod_name;
	private String prod_id;
	private String stb_id;
	private String card_id;
	private String package_id;
	private String package_sn;
	private String user_type_text = "";
	private Integer max_prod_count;
	
	
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
		user_type_text = MemoryDict.getDictName(DictKey.USER_TYPE, this.user_type);
	}
	public String getProd_sn() {
		return prod_sn;
	}
	public void setProd_sn(String prod_sn) {
		this.prod_sn = prod_sn;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getStb_id() {
		return stb_id;
	}
	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public String getUser_type_text() {
		return user_type_text;
	}
	public String getPackage_id() {
		return package_id;
	}
	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}
	public String getPackage_sn() {
		return package_sn;
	}
	public void setPackage_sn(String package_sn) {
		this.package_sn = package_sn;
	}
	public Integer getMax_prod_count() {
		return max_prod_count;
	}
	public void setMax_prod_count(Integer max_prod_count) {
		this.max_prod_count = max_prod_count;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prodId) {
		prod_id = prodId;
	}


}
