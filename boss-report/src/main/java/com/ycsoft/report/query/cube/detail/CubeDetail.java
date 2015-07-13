package com.ycsoft.report.query.cube.detail;

import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepDetailData;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.cube.CubeDataSet;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.key.Impl.ConKeyValue;
/**
 * cube明细报表计算维度键值转换接口
 */
public interface CubeDetail {
	/**
	 * 创建cube明细报表的dim键值替换语句
	 * @param cube
	 * @param headdatacells
	 * @return
	 * @throws ReportException
	 */
	public List<ConKeyValue> createCubeDetailKeys(CubeExec cube, List<CubeHeadCell> headdatacells)throws ReportException;
	
	/**
	 * 创建cube明细报表的测试用dim替换键值
	 * @param dimlist
	 * @return
	 */
	public List<ConKeyValue> createTestKeys(CubeExec cube);
	
	/**
	 * 生成 手工可编辑明细报表的查询控制sql,headdatacells 页面坐标，detaildimmap 可编辑明细报表的维度位置信息
	 * @return
	 * @throws ReportException 
	 */
	public String customQuerySql(List<CubeHeadCell> headdatacells,Map<Dimension,Integer> detaildimmap) throws ReportException;
	
	/**
	 * 生成 手工可编辑明细报表一条记录的 维度坐标取值bean
	 * @param headdatacells
	 * @param detaildimmap
	 * @return
	 * @throws ReportException
	 */
	public RepDetailData customDimData(List<CubeHeadCell> headdatacells,Map<Dimension,Integer> detaildimmap) throws ReportException;
}
