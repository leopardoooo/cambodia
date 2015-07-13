package com.ycsoft.report.query.cube.detail.custom;
/**
 * 自定义明细的 grid的列配置定义
 */
public class GridRow {
	//列索引
	private int row_idx;
	//列名
	private String row_name;
	//类型
	private RowType row_type;
	//列定义数据格式要求
	private String dataformat;
	
	public int getRow_idx() {
		return row_idx;
	}
	public void setRow_idx(int row_idx) {
		this.row_idx = row_idx;
	}
	public String getRow_name() {
		return row_name;
	}
	public void setRow_name(String row_name) {
		this.row_name = row_name;
	}
	public RowType getRow_type() {
		return row_type;
	}
	public void setRow_type(RowType row_type) {
		this.row_type = row_type;
	}
	public String getDataformat() {
		return dataformat;
	}
	public void setDataformat(String dataformat) {
		this.dataformat = dataformat;
	}
}
