package com.ycsoft.report.query.cube;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.graph.CubeGraph;
/**
 * cube变换中间数据体
 */
public interface CubeDataSet<T> {
	/**
	 * cube基础数据源
	 * @return
	 */
	T getDataSet();
	/**
	 * 获得cube变换后的数据源
	 * @return
	 */
	T getAssembleDataSet();
	/**
	 * 组装图形数据源
	 * @param graph
	 * @throws ReportException
	 */
	void assembleGraphDataSet(CubeGraph graph) throws ReportException;
	/**
	 * 组装cube数据源
	 * @throws ReportException
	 */
	void assembleCubeDataSet() throws ReportException;
	/**
	 * 获得数据源的基础定义头
	 * @return
	 */
	CubeHeadCell[] getBaseHeadCells();
	/**
	 * 装载cube
	 * @param cube
	 */
	void setCube(CubeExec cube);
	CubeExec getCube();
	/**
	 * 执行cube计算
	 * @throws ReportException
	 */
	void execute() throws ReportException;
	/**
	 * 验证cube正确与否
	 * @return
	 * @throws ReportException
	 */
	String validate() throws ReportException;
	/**
	 * 执行图形计算
	 * @param cg
	 * @return
	 * @throws ReportException
	 */
	CubeGraph graph(CubeGraph cg) throws ReportException;
}
