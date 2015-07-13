package com.ycsoft.report.query.cube.detail.custom;

/**
 * 自定义明细报表的配置界面的列类型定义
 */
public enum RowType {
	textfield("文本框"),datefield("日期"),combo("下拉单选框"),
	checkbox("复选框"),numberfield("数值框");
	
	RowType(String desc){
		this.desc=desc;
	}
	
	private String desc;
	
	public String getDesc() {
		return desc;
	}
}
