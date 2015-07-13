package com.ycsoft.report.query.cube.compute;

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
 * 合计行计算
 */
public class TotalCompute implements CellCompute<CubeCell[]> {

	private double[] totals;
	private boolean[] istotals;
	private CubeHeadCell[] heads;
	
	private List<MeaWarnCheck> checks=null;
	
	public TotalCompute(CubeHeadCell[] heads){
		this.totals=new double[heads.length];
		this.istotals=new boolean[heads.length];
		this.heads=heads;
		for(int i=0;i<heads.length;i++){
			if(heads[i].getDim_type().equals(DimensionType.crosswise))
				istotals[i]=false;
			else
				istotals[i]=true;
		}
	}
	public boolean compute(CubeCell[] t) {
		
		for(int i=0;i<istotals.length;i++){
			if(istotals[i]){
				totals[i]=totals[i]+((Number)t[i].getId()).doubleValue();
			}
		}
		return false;
	}

	public CubeCell[] getResult() throws ReportException {
		CubeCell[] totalcells=new CubeCell[totals.length];
		for(int i=0;i<istotals.length;i++){
			if(istotals[i]){
				CellImpl cell=new CellImpl();
				cell.setId(totals[i]);
				//cell.setName(i==0?"合计"+String.valueOf(totals[i]):String.valueOf(totals[i]));
				cell.setName(i==0?
						"合计"+CellShowControl.getMeaShowNmae(cell.getId(), this.heads[i])
						:CellShowControl.getMeaShowNmae(cell.getId(), this.heads[i]));
				
				cell.setColspan(1);
				cell.setRowspan(1);
				cell.setCell_type(CellType.total);
				totalcells[i]=cell;
			}else{
				CellImpl cell=new CellImpl();
				cell.setId(null);
				cell.setName(i==0?"合计":"");
				cell.setColspan(1);
				cell.setRowspan(1);
				cell.setCell_type(CellType.total);
				totalcells[i]=cell;
			}
		}
		for(int i=0;i<totalcells.length;i++){
			totalcells[i].setShow_class(CellShowControl.getCrossCellShow(totalcells[i]));
		
		}
		if(this.checks!=null&&this.checks.size()>1){
			for(MeaWarnCheck check:this.checks)
				check.check(totalcells);
		}
		return totalcells;
	}
	public void setWarnCheck(List<MeaWarnCheck> checks) {
		this.checks=checks;
	}

}
