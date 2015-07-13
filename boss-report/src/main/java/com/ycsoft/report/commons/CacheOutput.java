package com.ycsoft.report.commons;

import java.io.IOException;

import com.ycsoft.commons.exception.ReportException;
/**
 * 把展现结果写入结果集
 * @author new
 *
 */
public interface CacheOutput {
	/**
	 * 写入头
	 * @param obj
	 * @throws IOException
	 * @throws ReportException
	 */
	void writeHead(Object obj) throws IOException,ReportException;
	/**
	 * 写入一个内容
	 * @param obj
	 * @throws IOException
	 * @throws ReportException
	 */
	void writeObject(Object obj) throws IOException,ReportException;
	/**
	 * 关闭
	 * @throws IOException
	 * @throws ReportException
	 */
	void close() throws IOException,ReportException;;
}
