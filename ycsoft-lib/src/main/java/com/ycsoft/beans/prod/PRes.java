/**
 * PRes.java	2010/06/21
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PRes -> P_RES mapping
 */
@POJO(tn = "P_RES", sn = "SEQ_RES_ID", pk = "res_id")
public class PRes extends PResgroup implements Serializable {

	// PRes all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1695953821627009217L;
	private String res_id;
	private String res_name;
	private String res_desc;
	private String serv_id;
	private String status;
	private Date create_time;
	private String res_type;
	private String currency;
	private Integer band_width;
	
	private String status_text;
	private String serv_id_text;
	private String res_type_text;
	private String currency_text;
	private String external_res_id;

	public Integer getBand_width() {
		return band_width;
	}

	public void setBand_width(Integer band_width) {
		this.band_width = band_width;
	}
	
	public String getStatus_text() {
		return status_text;
	}

	public String getServ_id_text() {
		return serv_id_text;
	}

	public String getCurrency_text() {
		return currency_text;
	}

	/**
	 * default empty constructor
	 */
	public PRes() {
	}

	// res_id getter and setter
	public String getRes_id() {
		return res_id;
	}

	public void setRes_id(String res_id) {
		this.res_id = res_id;
	}

	// res_name getter and setter
	public String getRes_name() {
		return res_name;
	}

	public void setRes_name(String res_name) {
		this.res_name = res_name;
	}

	// res_desc getter and setter
	public String getRes_desc() {
		return res_desc;
	}

	public void setRes_desc(String res_desc) {
		this.res_desc = res_desc;
	}

	// serv_id getter and setter
	public String getServ_id() {
		return serv_id;
	}

	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
		this.serv_id_text = MemoryDict.getDictName(DictKey.SERV_ID, serv_id);
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.status_text = MemoryDict.getDictName(DictKey.STATUS, status);
	}

	// create_time getter and setter
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getRes_type() {
		return res_type;
	}

	public void setRes_type(String resType) {
		res_type = resType;
		this.res_type_text = MemoryDict.getDictName(DictKey.RES_TYPE, resType);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		this.currency_text = MemoryDict.getDictName(DictKey.BOOLEAN, currency);
	}

	public String getRes_type_text() {
		return res_type_text;
	}

	public String getExternal_res_id() {
		return external_res_id;
	}

	public void setExternal_res_id(String external_res_id) {
		this.external_res_id = external_res_id;
	}



}