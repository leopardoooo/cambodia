package com.ycsoft.report.dto;

import com.ycsoft.report.bean.RepKeyCon;

/**
 * 查询条件
 * @author new
 *
 */
public class RepKeyDto extends RepKeyCon {
	//页面取到的值
	private String value;
	//是否有子集
	private boolean sonkey=false;
	
	//关联条件 例如的 开始时间对应的结束时间，目的是为了让开始结束时间在同一列显示
	private RepKeyDto samelinekey=null;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSonkey() {
		return sonkey;
	}

	public void setSonkey(boolean sonkey) {
		this.sonkey = sonkey;
	}

	public RepKeyDto getSamelinekey() {
		return samelinekey;
	}

	public void setSamelinekey(RepKeyDto samelinekey) {
		this.samelinekey = samelinekey;
	}

}
