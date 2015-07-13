package com.ycsoft.report.query.datarole;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 一个操作员的数据权限包含
 * 1 标准报表数据权限
 * 2 自定义数据权限，即该操作员角色中存在的数据权限
 */
public interface DataRole extends Serializable,FuncRole{
	/**
	 * 报表标准权限
	 * @return
	 */
	public Integer getReprole();
	/**
	 * 标准权限取值
	 * @param reprole
	 * @return
	 */
	public QueryKeyValue getReproleValue(int reprole);

	/**
	 * 自定义对象ID数组
	 * @param s_data_right_type
	 * @return
	 */
	public List<QueryKeyValue> getDataRightValues(String s_data_right_type);
	/**
	 * key权限取值缓存
	 * @param key
	 * @return
	 */
	public List<QueryKeyValue> getKeyValueByCache(String key);
	/**
	 * 装入key权限取值缓存
	 * @param key
	 * @param valuelist
	 */
	public void setKeyValueCache(String key,List<QueryKeyValue> valuelist);
}
