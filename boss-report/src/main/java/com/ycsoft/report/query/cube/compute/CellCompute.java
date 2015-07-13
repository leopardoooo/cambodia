package com.ycsoft.report.query.cube.compute;

import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarnCheck;

/**
 * 单元格值计算
 */
public interface CellCompute<T> {
	 
	/**
	 * 执行计算
	 * 返回true：计算完毕
	 * @return
	 * @throws ReportException 
	 */
	boolean compute(T t) throws ReportException;
	/**
	 * 返回计算结果
	 * @throws ReportException 
	 */
	T getResult() throws ReportException;
	
	void setWarnCheck(List<MeaWarnCheck> checks);
	
}
