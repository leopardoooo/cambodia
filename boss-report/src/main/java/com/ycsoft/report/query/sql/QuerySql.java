package com.ycsoft.report.query.sql;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.ResultSetExtractor;
/**
 * 查询使用的sql接口
 */
public interface QuerySql extends Serializable {
	
	/**
	 * 返回值为合计项字符串
	 * 测试sql组件是否正确
	 * @return
	 * @throws ReportException 
	 */
	public Map<String,String> testSQL(String sql,String database) throws ReportException;
	/**
	 * 获取列名列表
	 * @param sql
	 * @param database
	 * @return
	 * @throws ReportException
	 */
	public List<String> getColumnNameList(String sql,String database) throws ReportException;
	
	
	/**
	 * 结果集提取器，负责查询数据，及关闭资源，提出数据由<code>ResultSetExtractor</code>来完成
	 * 
	 * @param sql
	 * @param database
	 * @param rse 读取数据
	 * @return
	 * @throws ReportException
	 */
	public <T> T getColumnRSMD(String sql, String database, ResultSetExtractor<T> rse)throws ReportException;
	/**
	 * 列模板转换
	 * @param sql
	 * @return
	 */
	public String translateTemplateKey(String sql);
}
