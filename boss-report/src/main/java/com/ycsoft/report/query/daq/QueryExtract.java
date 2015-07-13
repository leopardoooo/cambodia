package com.ycsoft.report.query.daq;

import com.ycsoft.commons.exception.ReportException;

/**
 * 查询提取数据
 */
public interface QueryExtract {
	/**
	 * 提取缓存，返回缓存文件名
	 * @return
	 * @throws ReportException
	 */
	String extractCache() throws ReportException;
	
	/**
	 * 获得数据
	 * @return
	 * @throws ReportException
	 */
	DataReader getData() throws ReportException;
}
