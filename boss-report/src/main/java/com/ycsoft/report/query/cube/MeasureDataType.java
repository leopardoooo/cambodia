package com.ycsoft.report.query.cube;

import java.text.DecimalFormat;

/**
 * 
 */
public enum MeasureDataType {
	integer("整数","#0"),
	integerbycomma("整数(,分隔)",",##0"),
	decimals("小数(取两位)","#0.00"),
	decimalsbycomma("小数(取两位;,分隔)",",##0.00")
	;
	private String desc;
	private DecimalFormat df;
	MeasureDataType(String desc,String format){
		this.desc=desc;
		df=new DecimalFormat(format);
	}
	
	public String getDesc() {
		return desc;
	}
	public String fromat(Object id){
		return df.format(id);
	}
}
