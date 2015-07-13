package com.ycsoft.report.query.key;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.key.Impl.ConKeyValue;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 查询条件接口
 * 分析查询语句，得到相关联的查询条件
 * @author new
 *
 */
public interface QueryKey extends Serializable {
	
	
	/**
	 * 获得一张报表(sql中含有的及其父key)需要使用的查询条件
	 * @param repdefine
	 * @return
	 * @throws ReportException 
	 */
	public List<RepKeyDto> getQueryKeyList(String sql) throws ReportException;
	/**
	 *  获得一张报表(sql中含有的及其父key)需要使用的查询条件
	 *  并对的父子关系的datefield,textfiedl组件类型的条件组装到同一行
	 * @param sql
	 * @return
	 * @throws ReportException
	 */
	public List<RepKeyDto> getQueryKeySameLineList(String sql) throws ReportException;
	
	/**
	 * 把key-valeu按照一定的顺序转换成字符串
	 * 对象转换成map，然后转换成json,然后ZipUtil压缩字符串
	 * @return
	 * @throws ReportException 
	 */
	public String toString(List<? extends ConKeyValue> keydtolist) throws ReportException;

	/**
	 * ZipUtil解压字符串，然后转换成map(key-valeu)
	 * @param json
	 * @return
	 * @throws ReportException
	 */
	public Map<String,String> toValueMap(String json)throws ReportException;
	/**
	 * 把参数keydtolist列表转化成树型结构顺序列表
	 * 参数key为树根
	 * @param keydtolist
	 * @return
	 * @throws ReportException
	 */
	public List<RepKeyDto> translateToTree(List<RepKeyDto> keydtolist,String key) throws ReportException;
	/**
	 * 获得一个查询条件的取值范围
	 * 数据权限判断
	 * @param key 查询键值
	 * @param fkey_value 查询键值父级取值
	 * @param countyid 分区键取值
	 * @return
	 * @throws ReportException 
	 */
	public List<QueryKeyValue> getKeyValue(String key,String fkey_value,String countyid) throws ReportException;
	/**
	 * 检查页面回穿的查询条件取值
	 * 数据权限判断
	 * @param keyvalues
	 * @throws ReportException 
	 */
	public void checkKeyValueList(List<ConKeyValue> keyvalues) throws ReportException;
}
