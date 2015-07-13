/**
 * TBusiCode.java	2010/03/08
 */

package com.ycsoft.beans.config;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * TBusiCode -> T_BUSI_CONFIRM mapping
 */
@POJO(tn = "T_BUSI_CONFIRM", sn = "",pk="")
public class TBusiConfirm implements Serializable{
	private String busi_code;
	private String busi_name;
	private String county_id;//
	private Date create_date;//
	private String creator_id;//
	private String creator_name;//操作员名称
	private String info_template;
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
		this.creator_name = MemoryDict.getDictName(DictKey.OPTR, this.creator_id);
	}
	public String getCreator_name() {
		return creator_name;
	}
	public String getBusi_code() {
		return busi_code;
	}
	public void setBusi_code(String busi_code) {
		this.busi_code = busi_code;
		this.busi_name = MemoryDict.getDictName(DictKey.BUSI_CODE, this.busi_code);
	}
	public String getBusi_name() {
		return busi_name;
	}
	public String getInfo_template() {
		return info_template;
	}
	public void setInfo_template(String info_template) {
		this.info_template = info_template;
	}
	public String getCounty_id() {
		return county_id;
	}
	public void setCounty_id(String county_id) {
		this.county_id = county_id;
	}
	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}
}