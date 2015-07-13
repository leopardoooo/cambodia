package com.ycsoft.report.query.key.Impl;

import java.io.Serializable;

import com.ycsoft.report.query.key.ConKey;
/**
 * 查询页面返回键-值关系bean
 *
 */
public class ConKeyValue implements ConKey{
	private String key;
	private String value;
	public String getId() {
		return key;
	}
	public String getName() {
		return value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
