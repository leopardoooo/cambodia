package com.ycsoft.report.query.cube.compute;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.impl.CellImpl;
import com.ycsoft.report.query.cube.showclass.CellShowControl;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarnCheck;
/**
 * 分组小计计算
 */
public class GroupCompute implements  CellCompute<CubeCell[]> {

	public static GroupCompute[] createGroupCompute(CubeHeadCell[]  heads){
		List<GroupCompute> list =new ArrayList<GroupCompute>();
		for(int i=0;i<heads.length;i++){
			//小计列： 横向维，单元格类型为group
			if(heads[i].getDim_type().equals(DimensionType.crosswise)
					&&heads[i].getCell_type().equals(CellType.group)){
				//当前下一列是横向维时，当前列可以小计
				if(heads[i+1].getDim_type().equals(DimensionType.crosswise))
					list.add(new GroupCompute(heads,i));
			}	
		}
		GroupCompute[] groups=new GroupCompute[list.size()];
		for(int i=list.size();i>0;i--)
			groups[i-1]=list.get(list.size()-i);
		return groups;
	}
	
	private int group_max;
	
	private double[] group;
	
	private boolean[] isgroup;
	
	private double[] resultgroup;
	/**
	 * 下一个分组的判断条件
	 */
	private Object[] condition;
	
	private CubeCell[] resultdatacells;
	
	private CubeCell[] datacells;
	
	private CubeHeadCell[] heads;
	
	private List<MeaWarnCheck> checks=null;
	
	
	GroupCompute(CubeHeadCell[]  heads,int group_index){
		this.group_max=group_index+1;
		group=new double[heads.length];
		resultgroup=new double[heads.length];
		isgroup=new boolean[heads.length];
		for(int i=0;i<heads.length;i++){
			if(heads[i].getDim_type().equals(DimensionType.crosswise))
				isgroup[i]=false;
			else
				isgroup[i]=true;
		}
		condition=new Object[group_max];
		this.heads=heads;

	}
	/**
	 * 判断是否进入下一个分组
	 * @return
	 */
	private boolean changeGroup(CubeCell[] t){
		boolean groupsign=false;
		for(int i=0;i<this.group_max;i++){
			if(condition[i]==null){
				condition[i]=t[i].getId();
			}else if (!condition[i].equals(t[i].getId())){
				condition[i]=t[i].getId();
				groupsign=true;
			}
		}
		return groupsign;
	}
	
	public  boolean compute(CubeCell[] t) {
		if(t==null){
			//分组末尾计算
			for(int i=0;i<this.isgroup.length;i++){
				resultgroup[i]=group[i];
				group[i]=0;
			}
			resultdatacells=datacells.clone();
			return true;
		}
		if(datacells==null) datacells=t;
		boolean changesign=changeGroup(t);
		if(changesign){
			//重置数据
			for(int i=0;i<this.isgroup.length;i++){
				resultgroup[i]=group[i];
				group[i]=0;
			}
			resultdatacells=datacells.clone();
			datacells=t;
		}else{
			datacells=t;
		}
		//计算
		for(int i=0;i<this.isgroup.length;i++){
			if(isgroup[i]){
				group[i]=group[i]+((Number)t[i].getId()).doubleValue();
			}
		}
		return changesign;
	}
	public CubeCell[] getResult() throws ReportException {
		
		CubeCell[] groupcells=new CubeCell[this.resultgroup.length];
		for(int i=0;i<this.resultgroup.length;i++){
			if(i<this.group_max){
				CellImpl cell=new CellImpl(this.resultdatacells[i]);
				cell.setCell_type(CellType.group);
				groupcells[i]=cell;
			}else{
				if(isgroup[i]){
					CellImpl cell=new CellImpl();
					cell.setId(resultgroup[i]);
					//cell.setName(String.valueOf(resultgroup[i]));
					cell.setName(CellShowControl.getMeaShowNmae(cell.getId(), this.heads[i]));
					cell.setCell_type(CellType.group);
					cell.setColspan(1);
					cell.setRowspan(1);
					groupcells[i]=cell;
				}else{
					CellImpl cell=new CellImpl();
					cell.setId(null);
					//cell.setName(i==this.group_max?this.heads[i-1].getName()+"_小计":"");
					cell.setName(i==this.group_max?"小计":"");
					cell.setCell_type(CellType.group);
					cell.setColspan(1);
					cell.setRowspan(1);
					groupcells[i]=cell;
				}
			}
		}
		for(int i=0;i<groupcells.length;i++){
			groupcells[i].setShow_class(CellShowControl.getCrossCellShow(groupcells[i]));
		}
		if(this.checks!=null&&this.checks.size()>0){
			for(MeaWarnCheck check:this.checks)
				check.check(groupcells);
		}
		
		return groupcells;
	}
	public void setWarnCheck(List<MeaWarnCheck> checks) {
		this.checks=checks;
	}

}
