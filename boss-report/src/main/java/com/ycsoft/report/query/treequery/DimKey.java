package com.ycsoft.report.query.treequery;

import java.io.Serializable;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.key.BaseKey;

/**
 * 统计报表的维度key
 * 用于支持类似olap报表钻取效果
 * @author new
 *
 */
public interface DimKey extends Serializable {
	
	/**
	 * 获得ID对应的上一级ID
	 * @param value
	 * @return
	 */
	public String getPid(String id) throws ReportException;;
	/**
	 * 获得ID对应的描述
	 * @param value
	 * @return
	 * @throws ReportException 
	 */
	public String getName(String id) throws ReportException;
	
	/**
	 * 获取关键字key
	 * @return key
	 */
	public BaseKey getBaseKey();
    
	/**
	 * #key#
	 * @return
	 */
	public String getKey();
	/**
	 * #key#的描述
	 * @return
	 */
	public String getDesc() ;
	/**
	 * 父级#key#
	 * @return
	 */
	public String getPkey() ;
	
	/**
	 * 子级#key#
	 * @return
	 */
	public String getSkey();


}
