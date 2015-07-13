package com.ycsoft.report.query;

import java.io.Serializable;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.dto.InitQueryDto;

/**
 * 查询容器管理
 * @author new
 *
 */
public interface QueryManage extends Serializable {
	
	/**
	 * 初始化查询，
	 * @param initQueryDto
	 * @throws ReportException 
	 */
	public RepQueryLog initQuery(InitQueryDto initQueryDto,RepQueryLog repQueryLog,String optr_id) throws ReportException;

	public QueryResult get(String query_id) throws ReportException;
	
	public void delete(String query_id);
	
}
