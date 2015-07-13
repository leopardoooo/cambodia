package com.ycsoft.report.query.daq;

import com.ycsoft.commons.exception.ReportException;
/**
 * cube计算结果数据 访问接口
 */
public interface DataReader {

	void open() throws ReportException;
	
	boolean next() throws ReportException;
	
	void close() throws ReportException;
	
	String getString(int i) throws ReportException;
	
	Object getObject(int i) throws ReportException;
}
