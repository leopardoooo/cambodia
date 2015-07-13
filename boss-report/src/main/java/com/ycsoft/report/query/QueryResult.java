package com.ycsoft.report.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;

/**
 * 查询结果集接口
 * @author new
 *
 */
public interface QueryResult extends Serializable {
	
	/**
	 * 查询条件的sha1哈希值
	 * @return
	 */
	public String getQRSHA();
	public void setQRSHA(String qrkeys) throws ReportException;
	/**
	 * 查询唯一编号
	 * @return
	 */
	public String getQueryId();
	
	public String getRepId();
	/**
	 * 数据库类型
	 * @return
	 */
	public String getDatabase();
	/**
	 * 结果集长度
	 * @return
	 */
	public int getRowSize();
	
	/**
	 * 获得指定分页内存
	 * @param start
	 * @param limit
	 * @return
	 * @throws ReportException
	 */
	public List<?> getPage(Integer start , Integer limit) throws ReportException;
 
	/**
	 * 查询头部内容
	 * 原始格式 List<RepHead>
	 * @return
	 * @throws ReportException 
	 * @throws ReportException 
	 */
	public List<?> getHead() throws ReportException;
	
	/**
	 * 返回导出文档的全文件名
	 * col_indexs为列索引数组,值为空表示导出快逸初始化数据源
	 * @return
	 * @throws ReportException 
	 */
	public String export(Integer...col_indexs) throws ReportException;
	
	/**
	 * 清空结果集
	 */
	public void clear();
	
	/**
	 * 更新访问日期
	 */
	public void updateVisitDate();
	
	/**
	 * 获得访问日期
	 * @return
	 */
	public Date getVisitDate();

}
