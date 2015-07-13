package com.ycsoft.report.query.cube.compute;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.impl.CellImpl;
import com.ycsoft.report.query.cube.impl.DimensionLevelValueManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.cube.showclass.CellShowControl;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarnCheck;
import com.ycsoft.report.query.daq.DataReader;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 行数据计算
 */
public class DataCompute implements CellCompute<CubeCell[]> {

	private DataReader rs = null;
	private CubeHeadCell[] headcells = null;

	private CubeCell[] datacells=null;
	private int rs_index=1;
	
	private List<MeaWarnCheck> checks=null;
	
	/**
	 * Map<level,Map<index,List<data_index>>>
	 */
	private Map<Integer,GroupBean> groupmap=new HashMap<Integer,GroupBean>();
	
	class GroupBean{
		public int start_index;
		public Map<Integer,List<Integer>> datamap=new HashMap<Integer,List<Integer>>();
	}
	
	//private Map<Dimension,>;
	/**
	 * 参数表头配置
	 * 
	 * @param heads
	 */
	public DataCompute(CubeHeadCell[] heads, DataReader rs) {
		this.rs = rs;
		this.headcells=heads;

		//组装纵向维合计头计算公式
		for(int i=0;i<heads.length;i++){
			if(heads[i].getDim_type().equals(DimensionType.vertical)
					&&heads[i].getCell_type().equals(CellType.group)){
		         //纵向维合计头
				 if(groupmap.containsKey(heads[i].getLevel())){
					 GroupBean bean=groupmap.get(heads[i].getLevel());
					 List<Integer> datalist=new ArrayList<Integer>();
					 for(int j=bean.start_index;j<i;j++){
						 if(!heads[j].getDim_type().equals(DimensionType.crosswise)
								 &&!heads[j].getCell_type().equals(CellType.group))
							 //非横向维，非合计头，就是数据头
							 datalist.add(j);
					 }
					 bean.datamap.put(i, datalist);
 					 bean.start_index=i;
				 }else{
					 GroupBean bean=new GroupBean();
					 bean.start_index=0;
					 List<Integer> datalist=new ArrayList<Integer>();
 					 for(int j=bean.start_index;j<i;j++){
						 if(!heads[j].getDim_type().equals(DimensionType.crosswise)
								 &&!heads[j].getCell_type().equals(CellType.group))
							 //非横向维，非合计头，就是数据头
							 datalist.add(j);
					 }
 					 bean.datamap.put(i, datalist);
 					 bean.start_index=i;
 					 groupmap.put(heads[i].getLevel(), bean);
				 }
				 
			}
		}
	}
	
	/**
	 * 横向维数据
	 * @return
	 * @throws SQLException
	 */
	private CubeCell computeCrossCell(CubeHeadCell head) throws ReportException{
		CellImpl data = new CellImpl();
		Object id=rs.getObject(rs_index);
		rs_index++;
		data.setId(id);
		String name=DimensionLevelValueManage.getDimLevelValueMap(
					DimensionManage.getDimension(head.getDim())
					.getLevel(head.getLevel())
				).get(id.toString()).getName();
		data.setName(name);
		data.setRowspan(1);
		data.setColspan(1);
		data.setCell_type(CellType.data);
		return data;
	}
	/**
	 * 纵向维头或指标头对应的 数值
	 * @return
	 * @throws SQLException
	 */
	private CubeCell computeVerticalCell(int i) throws ReportException{
		CellImpl data = new CellImpl();
		data.setId(rs.getObject(rs_index));
		data.setName(CellShowControl.getMeaShowNmae(data.getId(), this.headcells[i]));
		rs_index++;
		data.setRowspan(1);
		data.setColspan(1);
		data.setCell_type(CellType.data);
		return data;
	}
	/**
	 * 纵向维头对应合计头的数值计算
	 * @param i
	 * @return
	 */
	private CubeCell computeVertGroupCell(int i){
		
		CubeHeadCell head = headcells[i];
		double group=0;
		
		for(Integer o:this.groupmap.get(head.getLevel()).datamap.get(i)){
			group=group+((Number)this.datacells[o].getId()).doubleValue();
		}
		
		CellImpl data = new CellImpl();
		data.setId(group);
		data.setName(CellShowControl.getMeaShowNmae(data.getId(), this.headcells[i]));
		data.setRowspan(1);
		data.setColspan(1);
		data.setCell_type(CellType.group);
		return data;
	}
	public boolean compute(CubeCell[] t) throws ReportException {
		try {
			rs_index = 1;
			this.datacells=t;
			for (int i = 0; i < headcells.length; i++) {
				CubeHeadCell head = headcells[i];
				if (head.getDim_type().equals(DimensionType.crosswise)) {
					// 横向展开维
					datacells[i] = computeCrossCell(head);
				} else if (head.getDim_type().equals(DimensionType.vertical)
						&& !head.getCell_type().equals(CellType.group)) {
					// 纵向指标维 数据头和指标
					datacells[i] = this.computeVerticalCell(i);
				} else if (head.getDim_type().equals(DimensionType.vertical)
						&& head.getCell_type().equals(CellType.group)) {
					// 纵向指标维 合计头
					datacells[i] = this.computeVertGroupCell(i);
				} else if (head.getDim_type().equals(DimensionType.measure)) {
					// 指标度量头
					datacells[i] = this.computeVerticalCell(i);
				} else {
					throw new ReportException("head type is error.");
				}
			}

			return true;
		} catch (ReportException e) {
			throw e;
		}

	}

	public CubeCell[] getResult() throws ReportException {

		if(this.checks!=null&&this.checks.size()>0){
			for(MeaWarnCheck check:this.checks)
				check.check(this.datacells);
		}
		return this.datacells;
	}
	public void setWarnCheck(List<MeaWarnCheck> checks) {
		this.checks=checks;
	}

}
