package com.ycsoft.report.query.redis;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.commons.CacheInput;
import com.ycsoft.report.commons.CacheOutput;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.QueryResult;

/**
 * 查询结果集缓存接口
 */
public interface QRRedisCache {

	/**
	 * 
	 * @param key
	 * @param query_id
	 * @throws ReportException 
	 */
	void cacheQueryId(String query_id) throws ReportException;
	String extractQueryId()throws ReportException;
	
	void cacheRows(Integer rows) throws ReportException;
	Integer extractRows()throws ReportException;
	/**
	 * 缓存查询SQL
	 * @param key
	 * @return
	 */
	String extractQuerySql()throws ReportException;
	void cacheQuerySql(String sql)throws ReportException;
	/**
	 * 提取表头缓存
	 */
	Serializable extractHead()throws ReportException;
	void cacheHead(Serializable head)throws ReportException;
	
	/**
	 * 结果集内容写入缓存
	 * row=null 表示写入结束
	 * @param row
	 */
	void cacheContext(Serializable row)throws ReportException;
	/**
	 * 提取内容对应页码的缓存块
	 */
	Serializable extractContextPage(int page_index)throws ReportException;
	
	CacheOutput getRedisCacheOutput();
	
	CacheInput getRedisCacheInput();
	
}
