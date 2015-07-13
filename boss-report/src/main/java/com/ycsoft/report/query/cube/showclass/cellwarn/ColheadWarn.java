package com.ycsoft.report.query.cube.showclass.cellwarn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;

public class ColheadWarn implements MeaWarnCheck {
	
	
	private List<Integer> indexs=new ArrayList<Integer>();
	/**
	 * 逻辑判断
	 */
	private Operator[] operatorKey=null;
	private Double[]   operatorValue=null;
	/**
	 * 
	 */
	private WarnValueType type=null;
	
	private String show_class="";
	
	/**
	 * 确定点位，那几个
	 * @throws ReportException 
	 */
	public ColheadWarn(MeaWarnRow row,String mea,CubeHeadCell[] headcells) throws ReportException{
		if(!WarnRowType.colhead.equals(row.getWarnrowtype()))
			throw new ReportException("WarnRowType is not colhead");
	
		this.show_class=row.getColour();
		
		Map<String,String> valuelevelMap=new HashMap<String,String>();
		
		if(row.getWarnvaluelist()!=null){
			for(String o:row.getWarnvaluelist()){
				valuelevelMap.put(o, null);
			}
		}
		//确定要计算的列
		for(int i=0;i<headcells.length;i++){
			CubeHeadCell head=headcells[i];
			if(!head.getDim_type().equals(DimensionType.crosswise)
					&&mea.equals(head.getMea_code())){
				if(valuelevelMap.size()==0||valuelevelMap.containsKey(head.getId().toString())){
					indexs.add(i);
				}
			}
		}
		//逻辑符号
		if(row.getWarnvaluetype()!=null)
			this.type=row.getWarnvaluetype();
		
		//逻辑判断
		int index=0;
		if(row.getOptr1()!=null&&row.getValue1()!=null)
			index=index+1;
		if(row.getOptr2()!=null&&row.getValue2()!=null)
			index=index+1;
		if(index==0)
			throw new ReportException(" Operator is null");
		operatorKey=new Operator[index];
		operatorValue=new Double[index];
		
		if(row.getOptr1()!=null&&row.getValue1()!=null){
			index=index-1;
			operatorKey[index]=row.getOptr1();
			operatorValue[index]=row.getValue1();
		}
		if(row.getOptr2()!=null&&row.getValue2()!=null){
			index=index-1;
			operatorKey[index]=row.getOptr2();
			operatorValue[index]=row.getValue2();
		}
	}

	
	public boolean check(CubeCell[] cells) throws ReportException{
		if(this.type==null){
			for(int index:this.indexs)
				if(this.operator(((Number)cells[index].getId()).doubleValue()))
					cells[index].setShow_class(cells[index].getShow_class()+show_class);
		}else{
			double[] values=new double[this.indexs.size()];
			for(int i=0;i<this.indexs.size();i++){
				values[i]=((Number)cells[this.indexs.get(i)].getId()).doubleValue();
			}
			if(this.operator(this.type.operator(values))){
				for(int index:this.indexs)
					cells[index].setShow_class(cells[index].getShow_class()+show_class);
			}
		}
		return true;
	}
	
	/**
	 * 逻辑判断
	 * @param i
	 * @return
	 * @throws ReportException 
	 */
	public boolean operator(double value) throws ReportException{
		boolean opersign=true;
		for(int i=0;i<this.operatorKey.length;i++){
			opersign=opersign&&operatorKey[i].operator(this.operatorValue[i], value);
		}
		return opersign;
	}
}
