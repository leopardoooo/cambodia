package com.ycsoft.report.query.cube.impl;

import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.MeasureDataType;

public class CubeHeadCellImpl extends CellImpl implements CubeHeadCell {
	
	private Object pid;
	
	
	private String dim;
	private DimensionType dim_type;
	private Integer level;
	
	private String mea_detail_id;
    private String mea_code;
    
	private MeasureDataType mea_datatype;
	
	private Boolean show_slices;//显示过滤器(值=true),
	private Boolean show_expand;// 显示展开按钮(值=true),
	private Boolean show_shrink;//　显示收缩按钮(值=true),
	private Boolean show_sort;//显示排序按钮(值=true),
	
	public CubeHeadCellImpl(){};
	public CubeHeadCellImpl(CubeHeadCell cell){
		super(cell);
		this.pid=cell.getPid();
		this.dim=cell.getDim();
		this.dim_type=cell.getDim_type();
		this.level=cell.getLevel()==null?null:cell.getLevel().intValue();
		this.mea_detail_id=cell.getMea_detail_id();
		this.mea_code=cell.getMea_code();
		this.show_expand=cell.getShow_expand()==null?null:cell.getShow_expand().booleanValue();
		this.show_shrink=cell.getShow_shrink()==null?null:cell.getShow_shrink().booleanValue();
		this.show_slices=cell.getShow_slices()==null?null:cell.getShow_slices().booleanValue();
		this.show_sort  =cell.getShow_sort()==null?null:cell.getShow_sort().booleanValue();
		this.mea_datatype=cell.getMea_datatype();
	};
	
	public String getMea_detail_id() {
		return mea_detail_id;
	}
	public void setMea_detail_id(String mea_detail_id) {
		this.mea_detail_id = mea_detail_id;
	}
	

	
	public String getDim() {
		return dim;
	}
	public void setDim(String dim) {
		this.dim = dim;
	}
	public DimensionType getDim_type() {
		return dim_type;
	}
	public void setDim_type(DimensionType dim_type) {
		this.dim_type = dim_type;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Boolean getShow_slices() {
		return show_slices;
	}
	public void setShow_slices(Boolean show_slices) {
		this.show_slices = show_slices;
	}
	public Boolean getShow_expand() {
		return show_expand;
	}
	public void setShow_expand(Boolean show_expand) {
		this.show_expand = show_expand;
	}
	public Boolean getShow_shrink() {
		return show_shrink;
	}
	public void setShow_shrink(Boolean show_shrink) {
		this.show_shrink = show_shrink;
	}
	public Object getPid() {
		return pid;
	}
	public void setPid(Object pid) {
		this.pid = pid;
	}
	public String getMea_code() {
		return mea_code;
	}
	public void setMea_code(String mea_code) {
		this.mea_code = mea_code;
	}
	public MeasureDataType getMea_datatype() {
		return mea_datatype;
	}
	public void setMea_datatype(MeasureDataType mea_datatype) {
		this.mea_datatype = mea_datatype;
	}
	public Boolean getShow_sort() {
		return show_sort;
	}
	public void setShow_sort(Boolean show_sort) {
		this.show_sort = show_sort;
	}
	
}
