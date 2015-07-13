package com.ycsoft.report.query.cube.impl;

import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeCell;

public class CellImpl implements CubeCell {

	private Object id;
	private String name;
	private Integer colspan   ;
	private Integer rowspan   ;
	private CellType cell_type;
	private String show_class;
	
	private Boolean mea_custom;//指标是否编辑
	
	public CellImpl(){}
	
	public CellImpl(CubeCell cell){
		this.id=cell.getId();
		this.name=cell.getName();
		this.colspan=cell.getColspan()==null?null:cell.getColspan().intValue();
		this.rowspan=cell.getRowspan()==null?null:cell.getRowspan().intValue();
		this.cell_type=cell.getCell_type();
		this.show_class=cell.getShow_class();
		this.mea_custom=cell.getMea_custom()==null?null:cell.getMea_custom().booleanValue();
	}
	
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getColspan() {
		return colspan;
	}
	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}
	public Integer getRowspan() {
		return rowspan;
	}
	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}
	public CellType getCell_type() {
		return cell_type;
	}
	public void setCell_type(CellType cell_type) {
		this.cell_type = cell_type;
	}
	public String getShow_class() {
		return show_class;
	}
	public void setShow_class(String show_class) {
		this.show_class = show_class;
	}
	
	public Boolean getMea_custom() {
		return mea_custom;
	}
	public void setMea_custom(Boolean mea_custom) {
		this.mea_custom = mea_custom;
	}
}
