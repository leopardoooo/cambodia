package com.ycsoft.report.commons;

import java.io.IOException;

import com.ycsoft.commons.exception.ReportException;
/**
 * 从缓存中读展现数据接口
 * @author new
 *
 */
public interface CacheInput {
	/**
	 * 读表头
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ReportException
	 */
	public Object readHead() throws IOException, ClassNotFoundException, ReportException;
	/**
	 * 读一行数据
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ReportException
	 */
	public Object readObject() throws IOException, ClassNotFoundException, ReportException;
	public void close() throws Exception;
}
