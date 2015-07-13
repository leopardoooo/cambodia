package com.ycsoft.report.query.sql.cubesql;

import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.Measure;

/**
 * cube变换内部sql组装工厂
 */
public interface AnalyseCubeSql {
	/**
	 * 获取基础表头、原始表头
	 * 后续数据计算的依据
	 * @return
	 */
	public CubeHeadCell[] getBaseHeadCells();
	/**
	 * 获得cube变换组装后的取值sql
	 * @param sql
	 * @return
	 */
	public String getAnalyseSql(String sql) ;


	public void appendSelect(DimensionRolap dimcon);

	/**
	 * 无纵向维装载度量
	 * @param measures
	 */
	public void appendSelect(List<Measure> measures) ;

	/**
	 * 设置纵向维和度量
	 * 
	 * @throws ReportException
	 */
	public void appendSelect(DimensionRolap dimcon,
			List<Measure> defalutMeasures, String pid, int level,boolean isgraph)
			throws ReportException ;

	/**
	 * 维度表
	 * @param crossDim
	 */
	public void appendFrom(Dimension dim) ;
	/**
	 * 装载where 条件
	 * 
	 * @param crossDim
	 * @throws ReportException 
	 */
	public void appendWhere(DimensionRolap dimcon) throws ReportException ;
	/**
	 * 多级装载横向维
	 * @param dimcon
	 */
	public void appendGroup(DimensionRolap dimcon);

	public void appendOrderBy(DimensionRolap dimcon) ;
}
