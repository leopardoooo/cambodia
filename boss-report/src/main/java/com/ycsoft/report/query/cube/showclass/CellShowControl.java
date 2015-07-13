package com.ycsoft.report.query.cube.showclass;

import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
/**
 * 单元格显示格式控制
 * 背景色 深色 background: #eee
 */
public class CellShowControl {
	
	private static final String group_background="background: rgb(247, 247, 249); font-weight: bold;";
	private static final String total_background="background: rgb(247, 247, 249);";


	/**
	 * 单元格背景显示
	 * @param head
	 * @param cell
	 * @return
	 */
	public static String getCrossCellShow(CubeCell cell){
		if(cell==null)
			return null;
		if(CellType.group.equals(cell.getCell_type()))
			return group_background;
		else if (CellType.total.equals(cell.getCell_type()))
			return total_background;
		return null;
	}
	
	/**
	 * 指标数值显示格式
	 * @param head
	 * @return
	 */
	public static String getMeaShowNmae(Object id,CubeHeadCell head){
		if(head!=null&&head.getMea_datatype()!=null)
			return head.getMea_datatype().fromat(id);
		return String.valueOf(id);
	}
}
