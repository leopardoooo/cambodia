package com.ycsoft.report.query.cube.showclass.cellwarn;

import com.ycsoft.commons.exception.ReportException;

/**
 * 多列头的处理操作符
 * 无列头选择或单个列头是，对应操作位置 为空 不能使用
 */
public enum WarnValueType {
	SUM("求和"),AVG("平均值");
	private String desc;
	WarnValueType(String desc){
		this.desc=desc;
	}
	public String getDesc(){
		return desc;
	}
	
	public double operator(double... values) throws ReportException{
		if(this.equals(SUM)){
			double r=0;
			for(double o:values){
				r=r+o;
			}
			return r;
		}else if(this.equals(AVG)){
			double r=0;
			for(double o:values){
				r=r+o;
			}
			return r/values.length;
		}else{
			throw new ReportException(this.desc+" is undefind operator");
		}
	}
}
