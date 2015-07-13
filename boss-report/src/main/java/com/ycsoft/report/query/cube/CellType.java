package com.ycsoft.report.query.cube;

/**
 * 数据单元类型
 */
public enum CellType {
	
	data("数据"),group("小计"),total("合计"),dimension("维度");
	private String desc;
	CellType(String desc){
		this.desc=desc;
	}
	
	public String getDesc(){
		return this.desc;
	}
	
}
