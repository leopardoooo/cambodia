package com.ycsoft.report.query.cube.graph;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.impl.DimensionManage;

public class CubeGraphImpl implements CubeGraph {
	
	private String measure;
	private Dimension crossDimension;
	private Dimension vertDimension;
	private String database;
	private CubeGraphType graphType;
	private GraphData graphData;
	
	public CubeGraphImpl(CubeGraphType type,String title,String database,String mea,String...dims) throws ReportException{
		this.graphType=type;
		this.database=database;
		this.measure=mea;
		
		if(dims==null||dims.length==0)
			throw new ReportException("dims is null");
		if(type.getDimtype().equals(DimensionType.crosswise)){
			this.crossDimension=DimensionManage.getDimension(dims[0]);
			if(this.crossDimension==null)
				throw new ReportException(dims[0]+":dims is undefined");
		}else if(type.getDimtype().equals(DimensionType.vertical)){
			this.vertDimension=DimensionManage.getDimension(dims[0]);
			if(this.vertDimension==null)
				throw new ReportException(dims[0]+":dims is undefined");
		}else{
			if(dims.length!=2)
				throw new ReportException(type.getDesc()+":dims.size !=2");
			this.crossDimension=DimensionManage.getDimension(dims[0]);
			this.vertDimension=DimensionManage.getDimension(dims[1]);
		}
		graphData=new GraphData();
		graphData.setTitle(title);
		graphData.setType(type.getGraphtype());
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public Dimension getCrossDimension() {
		return crossDimension;
	}
	public void setCrossDimension(Dimension crossDimension) {
		this.crossDimension = crossDimension;
	}
	public Dimension getVertDimension() {
		return vertDimension;
	}
	public void setVertDimension(Dimension vertDimension) {
		this.vertDimension = vertDimension;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public CubeGraphType getGraphType() {
		return graphType;
	}
	public void setGraphType(CubeGraphType graphType) {
		this.graphType = graphType;
	}
	public GraphData getGraphData() {
		return graphData;
	}
	public void setGraphData(GraphData graphData) {
		this.graphData = graphData;
	}
	
	

}
