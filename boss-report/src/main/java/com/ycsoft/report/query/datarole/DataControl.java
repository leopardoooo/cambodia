package com.ycsoft.report.query.datarole;

import java.util.List;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 报表数据权限控制接口
 * rep_key_level
 */
public interface DataControl {

	/**
	 * 判断是否存在权限控制
	 * #optrid# 操作员ID判断
	 * @throws ReportException 
	 */
	boolean isControl(String key) throws ReportException;
	/**
	 * 获得权限控制后取值List
	 * @param key
	 * @return
	 * @throws ReportException 
	 */
	List<QueryKeyValue> getControlValues(String key) throws ReportException;
	
	/**
	 * 是否数据权限控制
	 * @param key
	 * @return
	 * @throws ReportException
	 */
	boolean isDataRightTypeControl(String key)throws ReportException;
}
