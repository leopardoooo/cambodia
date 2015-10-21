package com.ycsoft.commons.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.ycsoft.beans.system.SDataTranslation;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import static com.ycsoft.commons.constants.SystemConstants.LANGUAGE_ZH;
import static com.ycsoft.commons.constants.SystemConstants.LANGUAGE_EN;
import static com.ycsoft.commons.constants.SystemConstants.LANGUAGE_KH;

/**
 * @author <a href="mailto:nbljq99@163.com">Liujiaqi</a>
 *
 */
public class MemoryDict {
	private static ThreadLocal<String> langThreadLocal = new ThreadLocal<String>();
	
	// 字典
	private static Map<String, Map<String, SItemvalue>> dictMap = new HashMap<String,Map<String,SItemvalue>>();

	private static List<SItemvalue> datalist = new ArrayList<SItemvalue>();
	
	private static Map<String, SDataTranslation> transMap = new HashMap<String, SDataTranslation>();

	private static List<SDataTranslation> transList = new ArrayList<SDataTranslation>();

	private static boolean loadingData = false;
	
	public static void setLang(String lang){
		langThreadLocal.set(lang);
	}
	
	public static String getLang(){
		return langThreadLocal.get();
	}
	
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
		
		return getTransData(s.getItem_name());
	}
	
	private static String getLanguageData(SDataTranslation dataTrans){
		if(LANGUAGE_EN.equals( getLang() ) && StringHelper.isNotEmpty(dataTrans.getData_en())){
			return dataTrans.getData_en();
		}else if(LANGUAGE_KH.equals( getLang() ) && StringHelper.isNotEmpty(dataTrans.getData_kh())){
			return dataTrans.getData_kh();
		}
		return dataTrans.getData_cn();
	}
	
	public static String getTransData(String dataCn) {
		if(StringHelper.isEmpty(dataCn)) return "";
		SDataTranslation dataTrans = getTransMap().get(dataCn);
		if(dataTrans == null) return dataCn;
		
		return getLanguageData(dataTrans);
	}
	
	private static SItemvalue createItemvalue(SItemvalue s){
		SItemvalue itemvalue = new SItemvalue();
		itemvalue.setItem_desc(s.getItem_desc());
		itemvalue.setItem_idx(s.getItem_idx());
		itemvalue.setItem_key(s.getItem_key());
		itemvalue.setItem_name(s.getItem_name());
		itemvalue.setItem_value(s.getItem_value());
		itemvalue.setShow_county_id(s.getShow_county_id());
		return itemvalue;
	}
	
	@Override
	public void finalize() {
		langThreadLocal.remove();
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
		
		SItemvalue itemvalue = createItemvalue(s);
		itemvalue.setItem_name( getTransData(s.getItem_name()) );
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
			List<SItemvalue> list = new ArrayList<SItemvalue>();
			for(String key : m.keySet()){
				SItemvalue s = m.get(key);
				
				SItemvalue itemvalue = createItemvalue(s);
				itemvalue.setItem_name( getTransData(s.getItem_name()) );
				list.add(itemvalue);
			}
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
					Map<String, SDataTranslation> transTempMap = new HashMap<String, SDataTranslation>();
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
						
						for (SDataTranslation trans : transList) {
							String key = trans.getData_cn();
							SDataTranslation dataTrans = transTempMap.get(key);
							if (dataTrans == null) {
								dataTrans = new SDataTranslation();
								transTempMap.put(key, dataTrans);
							}
							BeanUtils.copyProperties(dataTrans, trans);
						}
						transMap = transTempMap;
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
	public static void setupData(List<SItemvalue> datalist, List<SDataTranslation> transList) {
		MemoryDict.datalist = datalist;
		MemoryDict.transList = transList;
		reLoadData();
	}
	
	public static void setTransData(List<SDataTranslation> transList){
		MemoryDict.transList = transList;
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
	
	public static void appendTransData(List<SDataTranslation> datalist) {
		try {
			for (SDataTranslation trans : datalist) {
				getTransMap().put(trans.getData_cn(), trans);
			}
		} catch (Exception e) {
			LoggerHelper.error(MemoryDict.class, "国际化数据装载异常");
		} finally{
			if (transMap == null){
				transMap = new HashMap<String, SDataTranslation>();
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
	
	public static Map<String, SDataTranslation> getTransMap() {
		loadData();
		return transMap;
	}
}


