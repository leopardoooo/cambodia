package com.ycsoft.report.query;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.compute.CellCompute;
import com.ycsoft.report.query.cube.compute.DataCompute;
import com.ycsoft.report.query.cube.graph.CubeGraph;
import com.ycsoft.report.query.cube.graph.Serie;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.daq.DBAcquisition;
import com.ycsoft.report.query.daq.DataReader;
import com.ycsoft.report.query.daq.translate.CacheTranslateCube;
import com.ycsoft.report.query.daq.translate.GraphAcquisition;

/**
 * 图形数据提取
 * 数据库提取
 */
public class QueryGraph {

	private CubeGraph graph;
	private CubeHeadCell[] headcells;
	private List<CubeCell[]> databody;
	private String cache_query_id;
	private AbstractDataSet dataset;
	public QueryGraph(AbstractDataSet t,CubeGraph graph,String cache_query_id) throws ReportException{
		this.dataset=t;
		this.graph=graph;
		this.headcells=t.getBaseHeadCells();
		databody=new ArrayList<CubeCell[]>();
		this.cache_query_id=cache_query_id;
	}
	public void execute() throws ReportException {
		DataReader rs=null;
		try {	
			//缓存提取
			CacheTranslateCube cache=new GraphAcquisition(this.graph);
			rs=cache.translate(cache_query_id, dataset);
			if(!cache.isTranslateSucess()){
				//缓存提取失败，直接数据提取，这是不可能的
				rs=new DBAcquisition(this.dataset.getAssembleDataSet(),graph.getDatabase());
			}
			rs.open();
			CubeCell[] datacells=null;
			//数据行计算器
			CellCompute<CubeCell[]> datacompute=new DataCompute(headcells,rs);
			while (rs.next()) {
				// 一行查询结果
				datacells = new CubeCell[headcells.length];
				//生成数据行
				datacompute.compute(datacells);
				datacells=datacompute.getResult();
				databody.add(datacells);
			}
		}catch (ReportException e) {
			throw e;
		}catch (Exception e) {
			throw new ReportException(e);
		} finally {
			try {
				if (rs != null){
					rs.close();
					rs=null;
				}
			} catch (Exception e) {	}
		}
	}
	
	public CubeGraph getGraph() throws ReportException{
		
		if(graph.getGraphType().getDimnum()==2&&
				graph.getGraphType().getDimtype().equals(DimensionType.crosswise)){
			//饼图
			return this.getCrossGraph_2();
		}else if(graph.getGraphType().getDimnum()==2&&
				graph.getGraphType().getDimtype().equals(DimensionType.vertical)){
			//二维折线图或柱状图
			return this.getVertGraph_2();
		}else if(graph.getGraphType().getDimnum()==3
				&&graph.getGraphType().getDimtype().equals(DimensionType.measure)){
			//三维折线图或柱状图
			return this.getGraph_3();
		}else{
			throw new ReportException(graph.getGraphType().getDesc()+" is undefined query.");
		}
	}
	
	/**
	 * 二维折线图 柱状图计算
	 * @return
	 * @throws ReportException
	 */
	private CubeGraph getVertGraph_2() throws ReportException{
		if(this.databody.size()==0) return this.graph;
	
		List<String> categories=new ArrayList<String>();
		for(int i=0;i<this.headcells.length;i++){
			categories.add(this.headcells[i].getName());
		}
		this.graph.getGraphData().setCategories(categories);
		
		List<Serie> series=new ArrayList<Serie>();
		for(CubeCell[] cellrow:this.databody){
			Serie vo=new Serie();
			vo.setName(this.graph.getGraphData().getXtitle());
			List<Number> values=new ArrayList<Number>();
			for(int i=0;i<cellrow.length;i++)
			values.add((Number) cellrow[i].getId());
			vo.setData(values);
			series.add(vo);
		}
		this.graph.getGraphData().setSeries(series);
		return this.graph;
	}
	
	/**
	 * 三维折线图柱状图计算
	 */
	private CubeGraph getGraph_3() throws ReportException{
		if(this.databody.size()==0) return this.graph;
		int data_start=0;//数据行索引
		for(int i=0;i<this.headcells.length;i++){
			if(!headcells[i].getDim_type().equals(DimensionType.crosswise)){
				data_start=i;
				break;
			}
		}
		
		List<String> categories=new ArrayList<String>();
		for(int i=data_start;i<this.headcells.length;i++){
			categories.add(this.headcells[i].getName());
		}
		this.graph.getGraphData().setCategories(categories);
		
		List<Serie> series=new ArrayList<Serie>();
		for(CubeCell[] cellrow:this.databody){
			Serie vo=new Serie();
			vo.setName(cellrow[data_start-1].getName());
			List<Number> values=new ArrayList<Number>();
			for(int i=data_start;i<cellrow.length;i++)
			values.add((Number) cellrow[i].getId());
			vo.setData(values);
			series.add(vo);
		}
		this.graph.getGraphData().setSeries(series);
		return this.graph;
	}
	
	private CubeGraph getCrossGraph_2() throws ReportException{
		if(this.databody.size()==0) return this.graph;
		int data_index=0;
		for(int i=0;i<this.headcells.length;i++){
			if(!headcells[i].getDim_type().equals(DimensionType.crosswise)){
				data_index=i;
				break;
			}
		}
		List<Serie> list=new ArrayList<Serie>();
		for(CubeCell[] cellrow:this.databody){
			Serie vo=new Serie();
			vo.setName(cellrow[data_index-1].getName());
			List<Number> values=new ArrayList<Number>();
			values.add((Number) cellrow[data_index].getId());
			vo.setData(values);
			list.add(vo);
		}
		this.graph.getGraphData().setSeries(list);
		return this.graph;
	}
}
