package com.ycsoft.commons.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;

/**
 * @author <a href="mailto:nbljq99@163.com">Liujiaqi</a>
 *
 */
public class MemoryDict {
	// 字典
	private static Map<String, Map<String, SItemvalue>> dictMap = new HashMap<String,Map<String,SItemvalue>>();

	private static List<SItemvalue> datalist = new ArrayList<SItemvalue>();

	private static boolean loadingData = false;

	/**
	 * 获取字典表name项
	 *
	 * @param keyname
	 * @param value
	 * @return
	 */
	public static String getDictName(Object keyname, String value) {
		if(keyname == null || StringHelper.isEmpty(value)) return "";
		
		Map<String, SItemvalue> m = getDictMap().get(keyname.toString());
		if (m == null)
			return "";
		SItemvalue s = m.get(value);
		if (s == null)
			return "";
		return s.getItem_name();
	}

	/**
	 * 获取字典表name项
	 *
	 * @param keyname
	 * @param value
	 * @return
	 */
	public static SItemvalue getDictItem(Object keyname, String value) {
		if(StringHelper.isEmpty(value)) return null;
		Map<String, SItemvalue> m = getDictMap().get(keyname.toString());
		if (m == null)
			return null;
		SItemvalue s = m.get(value);
		if (s == null)
			return null;
		return s;
	}

	/**
	 * 获取指定key字典集合
	 *
	 * @param keyname
	 * @return
	 */
	public static List<SItemvalue> getDicts(Object keyname) {
		Map<String, SItemvalue> m = getDictMap().get(keyname.toString());

		if (m != null){
			List<SItemvalue> list = new ArrayList<SItemvalue>(m.values());
			Collections.sort(list, new Comparator<SItemvalue>() {
				public int compare(final SItemvalue c1, final SItemvalue c2) {
					if (c1.getItem_idx() != null && c2.getItem_idx() != null){
						if (c1.getItem_idx() > c2.getItem_idx()) {
							return 1;
						} else {
							if (c1.getItem_idx().compareTo( c2.getItem_idx()) ==0) {
								return 0;
							} else {
								return -1;
							}
						}
					}
					return 0;
				}
			});
			return list;
		}
		return new ArrayList<SItemvalue>();
	}

	private static void loadData() {
		if (loadingData)
			synchronized (MemoryDict.class) {
				if (loadingData) {
					Map<String, Map<String, SItemvalue>> tempmap = new HashMap<String, Map<String, SItemvalue>>();
					try {
						for (SItemvalue itemvalue : datalist) {
							String key = itemvalue.getItem_key();
							Map<String, SItemvalue> dicts = tempmap.get(key);
							if (dicts == null) {
								dicts = new Hashtable<String, SItemvalue>();
								tempmap.put(key, dicts);
							}
							dicts.put(itemvalue.getItem_value(), itemvalue);
						}
						dictMap = tempmap;
					} catch (Exception e) {
						LoggerHelper.error(MemoryDict.class, "字典装载异常");
					} finally{
						loadingData = false;
					}
				}
			}

	}

	private static void reLoadData() {
		loadingData = true;
		loadData();
	}

	/**
	 * 在原数据中添加数据,并从新装载
	 *
	 * @param adddatalist
	 *            新添加的数据
	 */
	public static void setupData(List<SItemvalue> datalist) {
		MemoryDict.datalist = datalist;
		reLoadData();
	}

	/**
	 * 添加字典数据
	 * 重复关键字的数据，删除原有数据，以新添加的为准
	 * [会删除]传入数据键值相关的所有数据
	 * 
	 * @param datalist
	 */
	public static void appendData(List<SItemvalue> datalist) {
		try {
			Map<String, Map<String, SItemvalue>> tempmap = new HashMap<String, Map<String, SItemvalue>>();
			for (SItemvalue itemvalue : datalist) {
				String key = itemvalue.getItem_key();
				Map<String, SItemvalue> dicts = tempmap.get(key);
				if (dicts == null) {
					dicts = new Hashtable<String, SItemvalue>();
					tempmap.put(key, dicts);
				}
				dicts.put(itemvalue.getItem_value(), itemvalue);
			}
			dictMap.putAll(tempmap);
		} catch (Exception e) {
			LoggerHelper.error(MemoryDict.class, "字典装载异常");
		} finally{
			if (dictMap == null){
				dictMap = new HashMap<String, Map<String, SItemvalue>>();
			}
		}
	}
	
	/**
	 * 添加字典数据
	 * 有相同的覆盖，没有就新添加
	 * [不会删除]传入数据键值相关的所有数据
	 * @param datalist
	 */
	public static void addData(List<SItemvalue> datalist) {
		try {
			for (SItemvalue itemvalue : datalist) {
				String key = itemvalue.getItem_key();
				Map<String, SItemvalue> dicts = dictMap.get(key);
				if (dicts == null) {
					dicts = new Hashtable<String, SItemvalue>();
					dictMap.put(key, dicts);
				}
				dicts.put(itemvalue.getItem_value(), itemvalue);
			}
		} catch (Exception e) {
			LoggerHelper.error(MemoryDict.class, "字典装载异常");
		} finally{
			if (dictMap == null){
				dictMap = new HashMap<String, Map<String, SItemvalue>>();
			}
		}
	}
	
	public static Map<String, Map<String, SItemvalue>> getDictMap() {
		loadData();
		return dictMap;
	}
}


