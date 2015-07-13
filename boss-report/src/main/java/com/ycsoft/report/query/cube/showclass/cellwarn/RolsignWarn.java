package com.ycsoft.report.query.cube.showclass.cellwarn;

import java.util.HashMap;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.dto.WarnDimLevel;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;

/**
 * 行警戒判断
 * @author new
 *
 */
public class RolsignWarn implements MeaWarnCheck {
	
	private Map<String, String> levelvalueMap=new HashMap<String, String>();
	
	private int index=-1;
	
	public RolsignWarn(MeaWarnRow row,CubeHeadCell[] headcells) throws ReportException{
		try {
			if(!WarnRowType.rolsign.equals(row.getWarnrowtype()))
				throw new ReportException("WarnRowType is not rolsign");
			WarnDimLevel wdl=JsonHelper.toObject(row.getDimleveljson(), WarnDimLevel.class);
			
			for(int i=0;i<headcells.length;i++){
				if(DimensionType.crosswise.equals(headcells[i].getDim_type())
					&&wdl.getDim().equals(headcells[i].getDim())
					&&wdl.getLevel()==headcells[i].getLevel()){
					index=i;
					break;
				}
			}
			
			if(index==-1)
				throw new ReportException("WarnRowType is rolsign but not find dim level");
			
			for(String o: row.getWarnvaluelist()){
				levelvalueMap.put(o, null);
			}
		
		} catch(ReportException e){
			throw e;
		}catch (Exception e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 判断一行数据的行警戒是否生效
	 * @param cells
	 * @return
	 */
	public boolean  check(CubeCell[] cells){
		if(levelvalueMap.containsKey(cells[this.index].getId().toString())){
			return true;
		}else{
			return false;
		}
	}

}
