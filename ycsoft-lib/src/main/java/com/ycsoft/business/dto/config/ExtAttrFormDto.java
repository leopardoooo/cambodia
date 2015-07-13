/**
 *
 */
package com.ycsoft.business.dto.config;

import java.util.Map;

/**
 * 扩展属性保存DTO封装
 *
 * @author hh
 */
public class ExtAttrFormDto {

	private String extendTable;
	//主键字段
	private String pkColumn ;
	//主键值
	private String pkValue ;
	private Map<String,String> extAttrs;



	public String getExtendTable() {
		return extendTable;
	}
	public void setExtendTable(String extendTable) {
		this.extendTable = extendTable;
	}
	public Map<String,String> getExtAttrs() {
		return extAttrs;
	}
	public void setExtAttrs(Map<String,String> extAttrs) {
		this.extAttrs = extAttrs;
	}
	public String getPkColumn() {
		return pkColumn;
	}
	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}
	public String getPkValue() {
		return pkValue;
	}
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}



}
