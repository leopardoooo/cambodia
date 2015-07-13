package com.ycsoft.report.query.cube.graph;

import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.Measure;

/**
 * 图形数据提取接口
 */
public interface CubeGraph {
	/**
	 * 提取图形的访问的数据源
	 * @return
	 */
	String getDatabase();
	/**
	 * 提取指标
	 * @return
	 */
	String getMeasure();
	/**
	 * 提取维度配置
	 * 第一个横向
	 * @return
	 */
	Dimension getCrossDimension();
	/**
	 * 数据粒度维
	 * @return
	 */
	Dimension getVertDimension();
	
	/**
	 * 获得图形数据体
	 * @return
	 */
	GraphData getGraphData();
	/**
	 * 图形类型
	 * @return
	 */
	CubeGraphType getGraphType();
}
