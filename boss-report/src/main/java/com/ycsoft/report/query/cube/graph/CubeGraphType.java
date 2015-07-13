package com.ycsoft.report.query.cube.graph;

import com.ycsoft.report.query.cube.DimensionType;
/**
 * type: 'column' 柱状图, 'spline' 折线图, 'pie' 饼图
 * @author new
 *
 */
public enum CubeGraphType {
	pie("饼图",2,DimensionType.crosswise,GraphType.pie),
	histogram_two("二维柱状图",2,DimensionType.vertical,GraphType.column),
	histogram_three("三维柱状图",3,DimensionType.measure,GraphType.column),
	linegraph_two("二维折线图",2,DimensionType.vertical,GraphType.spline),
	linegraph_three("三维折线图",3,DimensionType.measure,GraphType.spline);

	private String desc;//图形描述
	private Integer dimnum;//维度数量
	private DimensionType dimtype;
	private GraphType graphtype;//页面图形类型
	
	public GraphType getGraphtype() {
		return graphtype;
	}

	CubeGraphType(String desc,Integer dimnum,DimensionType dimtype,GraphType type){
		this.desc=desc;
		this.dimnum=dimnum;
		this.dimtype=dimtype;
		this.graphtype=type;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getDimnum() {
		return dimnum;
	}

	public DimensionType getDimtype() {
		return dimtype;
	}

}
