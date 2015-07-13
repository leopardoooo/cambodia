package com.ycsoft.report.query.cube;

public enum  DimensionType {
	vertical("列头_纵向维"),crosswise("行标签_横向维"),measure("指标_度量");
	private String desc;
	DimensionType(String desc){
		this.desc=desc;
	}
	public String getDesc(){
		return this.desc;
	}
}
