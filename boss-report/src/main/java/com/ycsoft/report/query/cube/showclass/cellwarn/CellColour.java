package com.ycsoft.report.query.cube.showclass.cellwarn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CellType;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;

public class CellColour implements MeaWarnApplyTo {
	
	
	
	private List<MeaWarnCheck> meawarncheck=null;
	
	private Map<CellType,String> applytoMap =null;
	
	public static List<MeaWarnApplyTo> createCellColour(AbstractDataSet dataset) throws ReportException{
		
		List<MeaWarn> warnlist=dataset.getMeawarns();
		if(warnlist==null||warnlist.size()==0)
			return null;
		List<MeaWarnApplyTo> checks=new ArrayList<MeaWarnApplyTo>();
		for(MeaWarn warn:warnlist){
			CellColour colour=new CellColour(warn,dataset.getBaseHeadCells());
			checks.add(colour);
		}
		dataset.clearMeawarn();
		return checks;
	}
	
	public static List<MeaWarnCheck> getMeaWarnCheck(List<MeaWarnApplyTo> warns,CellType celltype){
		if(warns==null||warns.size()==0)
			return null;
		List<MeaWarnCheck> checks=new ArrayList<MeaWarnCheck>();
		for(MeaWarnApplyTo o:warns){
			if(o.applyTo(celltype))
				checks.add(o);
		}
		return checks;
	}
	
	public CellColour(MeaWarn warn,CubeHeadCell[] headcells) throws ReportException{
		
		List<MeaWarnCheck> rolsignlist=new ArrayList<MeaWarnCheck>();
		List<MeaWarnCheck> colheadlist=new ArrayList<MeaWarnCheck>();
		
		for(MeaWarnRow row: warn.getRowlist()){
			if(WarnRowType.rolsign.equals(row.getWarnrowtype())){
				rolsignlist.add(new RolsignWarn(row,headcells));
			}else if(WarnRowType.colhead.equals(row.getWarnrowtype())){
				colheadlist.add(new ColheadWarn(row,warn.getMea(),headcells));
			}else if(WarnRowType.datatype.equals(row.getWarnrowtype())){
				if(row.getWarnvaluelist()!=null&&row.getWarnvaluelist().size()>0){
					applytoMap=new HashMap<CellType,String>();
					for(String o:row.getWarnvaluelist()){
						CellType  celltype =  CellType.valueOf(o);
						if(celltype==null)
							throw new ReportException(o+" is not a CellType");
						applytoMap.put(CellType.valueOf(o), null);
					}
				}
			}
		}
		
		rolsignlist.addAll(colheadlist);
		
		meawarncheck=rolsignlist;
	}
	
	
	public boolean applyTo(CellType celltype){
		if(applytoMap==null||applytoMap.containsKey(celltype))
			return true;
		else 
			return false;
	}

	public boolean check(CubeCell[] cells) throws ReportException {
		if(this.meawarncheck!=null){
			for(MeaWarnCheck check:this.meawarncheck){
				if(!check.check(cells))
					return false;
			}
		}
		return true;
	}


}
