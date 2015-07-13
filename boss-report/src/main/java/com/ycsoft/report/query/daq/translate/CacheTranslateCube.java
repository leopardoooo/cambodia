package com.ycsoft.report.query.daq.translate;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.daq.DataReader;

/**
 * 通过缓存计算cube接口
 */
public interface CacheTranslateCube {
	/**
	 * 计算是否成功
	 * 内容条数超过限额，则计算失败
	 * @return
	 */
	boolean isTranslateSucess();
	
	/**
	 * 计算
	 * @param full_cache_id
	 * @return
	 * @throws ReportException 
	 */
	DataReader translate(String query_cache_id,AbstractDataSet dataset) throws ReportException;
	
}
