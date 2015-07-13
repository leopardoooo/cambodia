package com.ycsoft.report.query.cube;

import java.io.Serializable;

/**
 * cube单元格接口
 * @author new
 *
 */
public interface CubeCell extends Serializable {
	public  Object getId();
	
	public String getName();//单元格显示名称
	public void setName(String name);
	
    public Integer getColspan();//   单元格横向合并值(值>1合并,值=0被合并格,值=1 正常格),
    public void setColspan(Integer colspan);
    public Integer getRowspan();//   单元格纵向合并值(值>1合并,值=0被合并格,值=1 正常格),  
    public void setRowspan(Integer rowspan);
    public CellType getCell_type();//   单元格类型:维度，数据，小计，合计,
	public String getShow_class();//  数据显示样式, 
	public void setShow_class(String show_class);
	
	public Boolean getMea_custom();//指标是否可编辑
}
