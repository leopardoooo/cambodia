package com.ycsoft.report.query.cube;


/**
 * cube表头单元结构接口
 * 
 * 警戒值配置
 * 
 * 分组合计配置
 * 
 * 数值显示格式配置例子：整数；小数保留两位等
 * 
 * 是否存在指标明细报表
 */
public interface CubeHeadCell extends CubeCell {


	public Object getPid();
	/**
	 * 表头属性
	 */
	public String getDim();//维定义值
	
	public DimensionType getDim_type();//维类型
	
	public Integer getLevel();//维层级
	
	public String getMea_detail_id();//指标明细报表ID
	
	public String getMea_code();//指标定义值
	
	public MeasureDataType getMea_datatype();//指标数值显示格式
	
	//public 
	/**
	 * 显示收缩按钮
	 */
	public Boolean getShow_shrink();
	/**
	 * 显示展开按钮
	 */
	public Boolean getShow_expand();
	/**
	 * 显示过滤切片按钮
	 */
	public Boolean getShow_slices();
	/**
	 * 显示排序界面按钮
	 */
	public Boolean getShow_sort();
	
}