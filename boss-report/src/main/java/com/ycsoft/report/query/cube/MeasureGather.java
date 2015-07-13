package com.ycsoft.report.query.cube;

/**
 * 度量聚集方法
 */
public enum MeasureGather {
	SUM /**,COUNT_DISTRINT,AVG**/;
	
	/**
	 * 聚集方法
	 * sum已实现
	 * 其他未实现
	 * @param o
	 * @return
	 */
	public  double gather(double... o){
		if(this.equals(SUM))
			return execSum(o);
		return 0;
	}
	
	/**
	 * SUM计算方法
	 * @param o
	 * @return
	 */
	private double execSum(double... o){
		if(o==null) return 0;
		double r=0;
		for(double a:o)
			r=r+a;
		return r;
	}
	
}
