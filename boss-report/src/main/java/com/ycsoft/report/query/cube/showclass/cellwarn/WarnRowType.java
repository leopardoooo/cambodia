package com.ycsoft.report.query.cube.showclass.cellwarn;

/**
 * 行标签类型
 */
public enum WarnRowType {
	rolsign("行标签"),//行标签 取 当前模板下 横向维的最底层数据 对应 项目 单选
	colhead("列头"),//列头 取 纵向维的最底层数据  对应 双栏选择框 多选
	datatype("数据类型");//数据类型 取 CellType定义的 data,group
	
	private String desc;
	WarnRowType(String desc){
		this.desc=desc;
	}
	public String getDesc(){
		return this.desc;
	}
}
