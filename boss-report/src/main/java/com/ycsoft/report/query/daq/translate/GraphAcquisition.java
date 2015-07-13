package com.ycsoft.report.query.daq.translate;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.graph.CubeGraph;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;

public class GraphAcquisition extends CacheAcquisition {
	
	private CubeGraph graph=null;
	public GraphAcquisition(CubeGraph graph){
		this.graph=graph;
	}
	/**
	 * 创建虚拟表头
	 * 增加未被图形使用的维度但cube使用的维度的切片和权限影响
	 * @throws ReportException 
	 */
	@Override
	protected List<CacheHeadCell> createDataCotrolHead(AbstractDataSet dataset) throws ReportException{
		List<CacheHeadCell> headlist=super.createDataCotrolHead(dataset);
		
		//cube使用的，但是图形未使用的维度 且有切片或权限影响的
		List<DimensionRolap> slice_control_Rolaps=new ArrayList<DimensionRolap>();
		for(DimensionRolap rolap:  dataset.getCube().getDimensionRolaps()){
			if(rolap.isUsesign()&&(rolap.getSlices_level()!=null||rolap.getDim().getDimLevelControlMap().size()>0)
					&&rolap.getDim()!=graph.getCrossDimension()&&rolap.getDim()!=graph.getVertDimension()){
				slice_control_Rolaps.add(rolap);
			}
		}
		//组装虚拟头
		for(DimensionRolap rolap:slice_control_Rolaps){
			CacheHeadCell o=CacheHeadCell.createDummyHead(rolap);
			headlist.add(o);
		}
		return headlist;
	}
	/**
	 * 图形计算不考虑内存限制
	 */
	@Override
	protected boolean isMenoryMax(){
		return false;
	}

}
