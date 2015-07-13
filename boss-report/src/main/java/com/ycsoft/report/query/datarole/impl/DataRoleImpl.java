package com.ycsoft.report.query.datarole.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.report.query.datarole.DataRole;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
/**
 * 报表数据权限
 */
public class DataRoleImpl extends FuncRoleImpl implements DataRole{
	/**
	 * 报表标准数据权限
	 */
	private Integer reprole;
	/**
	 * 自定义数据权限——key_值数组MAP
	 */
	private Map<String,List<QueryKeyValue>> dataright_map;
	/**
	 * 默认数据权限1,2,3,4权限对应默认操作员取值
	 */
	private Map<Integer,QueryKeyValue> reprolemap;
	/**
	 * key-取值
	 */
	private Map<String,List<QueryKeyValue>> keyvaluecache=new HashMap<String,List<QueryKeyValue>>();	
	
	
	
	public Map<Integer, QueryKeyValue> getReprolemap() {
		return reprolemap;
	}

	public void setReprolemap(Map<Integer, QueryKeyValue> reprolemap) {
		this.reprolemap = reprolemap;
	}

	public Integer getReprole(){
		return this.reprole;
	}

	public Map<String, List<QueryKeyValue>> getDataright_map() {
		return dataright_map;
	}

	public void setDataright_map(Map<String, List<QueryKeyValue>> dataright_map) {
		this.dataright_map = dataright_map;
	}

	public void setReprole(Integer reprole) {
		this.reprole = reprole;
	}

	public List<QueryKeyValue> getDataRightValues(String s_data_right_type) {
		return this.dataright_map.get(s_data_right_type);
	}

	public QueryKeyValue getReproleValue(int reprole) {
		return reprolemap.get(reprole);
	}

	public List<QueryKeyValue> getKeyValueByCache(String key) {
		return this.keyvaluecache.get(key);
	}

	public void setKeyValueCache(String key, List<QueryKeyValue> valuelist) {
		if(valuelist==null||valuelist.size()==0)
			return;
		this.keyvaluecache.put(key, valuelist);
	}

}
