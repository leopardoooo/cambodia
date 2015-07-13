package com.ycsoft.report.query.cube.showclass.cellwarn;

import com.ycsoft.commons.exception.ReportException;

/**
 * 逻辑操作符号
 */
public enum Operator {
	greaterthan(">"),greater_equal(">="),equal("="),
	lessthan("<"),lessthan_equal("<=");
	
	private String desc;
	Operator(String desc){
		this.desc=desc;
	}
	public String getDesc() {
		return desc;
	}
	
	public boolean operator(double key,double value) throws ReportException{
		switch(this){
			case greaterthan:
				return  value>key;
			case greater_equal:
				return value>=key;
			case equal:
				return value==key;
			case lessthan:
				return value<key;
			case lessthan_equal:
			 	return value<=key;
			default:
				throw new ReportException(this.desc+ " is undefid in operator");
		}
	}
}
