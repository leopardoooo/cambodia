package com.ycsoft.report.query.cube;

/***********************************************************************
 * Module:  CubeExec.java
 * Author:  new
 * Purpose: Defines the Interface CubeExec
 ***********************************************************************/

import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.graph.CubeGraph;

/**
 * cube计算接口 T cube基础数据集
 * 
 * @pdOid 4636492c-d859-4949-9168-a3b8fcdf6701
 */
public interface CubeExec {
	
	/**
	 * 计算图形数据结构
	 * 参数 mea 计算用指标
	 * 参数 rolaps 计算用维度清单
	 * @param t
	 * @param graph
	 * @return
	 * @throws ReportException 
	 */
	CubeGraph executeGraph(CubeDataSet dataset,CubeGraph graph) throws ReportException;

	/**
	 * 使用的度量清单
	 */
	List<Measure> getMeasures();
	/**
	 * 默认配置的度量清单
	 * @return
	 */
	List<Measure> getDefaultMeasures();

	/**
	 * 维设置清单 按有效横向维，有效纵向维，无效维排序
	 */
	List<DimensionRolap> getDimensionRolaps();

	DimensionRolap getDimensionRolap(Dimension dim);

	/**
	 * 展开一个维
	 * 
	 * @param dim
	 */
	void expandDimension(Dimension dim);

	/**
	 * 收缩一个维度
	 * 
	 * @param dim
	 */
	void shrinkDimension(Dimension dim);

	/**
	 * 维度切片过滤 切片值： Map<level,value> level表示哪一层切片 value表示该层切片值
	 * 
	 * @param dim
	 * @param levelvalueMap
	 */
	void slicesDimension(Dimension dim, Integer level, String... values)
			throws ReportException;
	/**
	 * 维度某一个层级的自定义排序
	 * @param dim
	 * @param level
	 * @param values
	 * @throws ReportException
	 */
	void sortDimension(Dimension dim, Map<Integer,String[]> sortmap)throws ReportException;
	
	/**
	 * 维选择 vertdim 纵向维 dims 维清单
	 * 
	 * @param vertdim
	 * @param dims
	 * @throws ReportException
	 */
	void selectDimension(Dimension vertdim, Dimension... dims)
			throws ReportException;

	/**
	 * 指标选择
	 * 
	 * @param meas
	 * @throws ReportException
	 */
	void selectMeasure(String... meas) throws ReportException;

	/**
	 * 配置维度分组统计
	 * 
	 * @param dim
	 * @param levels
	 * @throws ReportException
	 */
	void configDimensionTotal(Dimension dim, Integer... levels)
			throws ReportException;

	/**
	 * 执行计算
	 */
	CubeDataSet execute(CubeDataSet dataset) throws ReportException;

	/**
	 * 预览，cube转换的样子
	 */
	List<CubeHeadCell[]> preview(List<DimensionRolap> dimrolaps,List<Measure> mealist)
			throws ReportException;

	/**
	 * 验证cube配置是否正确。 返回null表示配置正确，其他值表示错误内容
	 * 
	 * @throws ReportException
	 * 
	 */
	String validate(CubeDataSet dataset) throws ReportException;

	/**
	 * 获得表头
	 * 
	 * @return
	 * @throws ReportException
	 */
	List<CubeHeadCell[]> getCubeHead() throws ReportException;

}