package com.ycsoft.report.query.cube.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeDataSet;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.graph.CubeGraph;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarn;
import com.ycsoft.report.query.sql.cubesql.AnalyseCubeSql;
import com.ycsoft.report.query.sql.cubesql.CubeSqlContainer;
/**
 * cube变换用数据源
 * 数据源使用sql
 */
public abstract class AbstractDataSet implements CubeDataSet<String> {

	private CubeExec cube=null;
	
	private CubeHeadCell[] headcells=null;
	
	private String cubesql=null;
	
	private List<MeaWarn> meawarns=null;
	

	public List<MeaWarn> getMeawarns() {
		return meawarns;
	}

	public void setMeawarns(List<MeaWarn> meawarns) {
		this.meawarns = meawarns;
	}
	
	public void clearMeawarn(){
		this.meawarns=null;
	}

	public String getAssembleDataSet(){
		return this.cubesql;
	}
	
	/**
	 * 组装cube数据源
	 * 考虑被权限影响的未使用维度
	 */
	public void assembleCubeDataSet() throws ReportException {

		AnalyseCubeSql sqlcon=new CubeSqlContainer();
		//横向维，行标签
		List<DimensionRolap> crosswiselist=new ArrayList<DimensionRolap>();
		//纵向维，表头
		List<DimensionRolap> verticallist=new ArrayList<DimensionRolap>();
		//权限控制的未使用的维度
		List<DimensionRolap> datacotrollist=new ArrayList<DimensionRolap>();
		
		for (DimensionRolap dimrolap : this.cube.getDimensionRolaps()) {
			if(dimrolap.isUsesign()&&dimrolap.isVerticalsign())
				verticallist.add(dimrolap);
			if(dimrolap.isUsesign()&&!dimrolap.isVerticalsign())
				crosswiselist.add(dimrolap);
			if(!dimrolap.isUsesign()&&dimrolap.getDim().getDimLevelControlMap().size()>0)
				datacotrollist.add(dimrolap);
		}
		
		// 横向维装配
		for (DimensionRolap dimcon : crosswiselist) {
			//DimensionRolap dimcon = this.dimconMap.get(crossDim);
			sqlcon.appendSelect(dimcon);
			sqlcon.appendFrom(dimcon.getDim());
			sqlcon.appendWhere(dimcon);
			sqlcon.appendGroup(dimcon);
			sqlcon.appendOrderBy(dimcon);
		}
		// 纵向维
		if (verticallist.size() > 0) {		
			// 只处理一个纵向维
			DimensionRolap dimcon=verticallist.get(0);
			sqlcon.appendSelect(dimcon,this.cube.getMeasures(), null,1,false);
			sqlcon.appendFrom(dimcon.getDim());
			sqlcon.appendWhere(dimcon);
		} else {
			sqlcon.appendSelect(this.cube.getMeasures());
		}		
		
		//权限影响的未使用的维度
		for (DimensionRolap dimcon : datacotrollist) {
			sqlcon.appendFrom(dimcon.getDim());
			sqlcon.appendWhere(dimcon);
		}
		
		this.headcells=sqlcon.getBaseHeadCells();
		this.cubesql= sqlcon.getAnalyseSql(this.getDataSet());
	}

	/**
	 * 图形数据提取sql组装
	 */
	public void assembleGraphDataSet(CubeGraph graph) throws ReportException {
		AnalyseCubeSql sqlcon=new CubeSqlContainer();
		Map<Dimension, DimensionRolap> dimconMap=new HashMap<Dimension, DimensionRolap>();
		Map<String,Measure> meaMap=new HashMap<String,Measure>();
		for(DimensionRolap o: this.cube.getDimensionRolaps()){
			dimconMap.put(o.getDim(), o);
		}
		for(Measure o: this.cube.getDefaultMeasures()){
			meaMap.put(o.getColumnCode(), o);
		}
		//1.cube使用的维且图形未使用的维度，有权限影响和切片影响
		List<DimensionRolap> slicesRolap=new ArrayList<DimensionRolap>();
		//2.cube未使用的维，有权限影响
		List<DimensionRolap> datacotrolRolap=new ArrayList<DimensionRolap>();
		for(DimensionRolap rolap:dimconMap.values()){
			if(rolap.isUsesign()&&(rolap.getSlices_level()!=null||rolap.getDim().getDimLevelControlMap().size()>0)){
				if(!rolap.getDim().equals(graph.getCrossDimension())
						&&!rolap.getDim().equals(graph.getVertDimension()))
				slicesRolap.add(rolap);
			}
			if(!rolap.isUsesign()&&rolap.getDim().getDimLevelControlMap().size()>0)
				datacotrolRolap.add(rolap);
		}
		
		//图形横向维度 (x轴?)
		if(graph.getCrossDimension()!=null){
		    DimensionRolap dimcon=dimconMap.get(graph.getCrossDimension());
			sqlcon.appendSelect(dimcon);
			sqlcon.appendFrom(dimcon.getDim());
			sqlcon.appendWhere(dimcon);
			sqlcon.appendGroup(dimcon);
			sqlcon.appendOrderBy(dimcon);
		}
	
		//指标和纵向维(y轴?z轴)
		List<Measure> list=new ArrayList<Measure>();
		list.add(meaMap.get(graph.getMeasure()));
		//粒度维
		if(graph.getVertDimension()==null)
			sqlcon.appendSelect(list);
		else{
			DimensionRolap o =dimconMap.get(graph.getVertDimension());
			sqlcon.appendSelect(o,list, null,1,true);
			sqlcon.appendFrom(o.getDim());
			sqlcon.appendWhere(o);
		}
		
		//非图形维但cube使用的维有切片和权限影响  影响
		for (DimensionRolap o : slicesRolap) {
			sqlcon.appendFrom(o.getDim());
			sqlcon.appendWhere(o);
		}
		
		//未使用维的有 权限影响
		for (DimensionRolap o : datacotrolRolap) {
			sqlcon.appendFrom(o.getDim());
			sqlcon.appendWhere(o);
		}
		
		this.headcells=sqlcon.getBaseHeadCells();
		this.cubesql= sqlcon.getAnalyseSql(this.getDataSet());
	}

	public CubeHeadCell[] getBaseHeadCells(){
		return headcells;
	}

	public CubeExec getCube() {
		return this.cube;
	}

	
	public void setCube(CubeExec cube) {
		this.cube=cube;
	}
}
