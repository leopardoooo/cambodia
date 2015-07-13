package com.ycsoft.report.query.cube.graph;
/**
 * 前台显示图形
 */
public enum GraphType {
	column("柱状图"), spline("折线图"),pie("饼图");
	private String desc;
	GraphType(String desc){
		this.desc=desc;
	}
	public String getDesc() {
		return desc;
	}
}
