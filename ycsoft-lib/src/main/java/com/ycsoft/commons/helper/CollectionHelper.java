package com.ycsoft.commons.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.ycsoft.beans.prod.PPromFeeProd;

/**
 * 集合辅助类，包含Java三大集合的处理
 *
 * @author hh
 * @date Dec 3, 2009 1:25:11 PM
 */
public class CollectionHelper {


	private CollectionHelper() {}

	/**
	 * 将给定的属性名称propertyName，在数据集中查询对应的JavaBean属性值，
	 * 将属性值相等的放入新的List ,并添加至Map中。以属性值为map的key值
	 * @param <T>
	 * @param data 数据源
	 * @param keyName 作为主键的属性名称
	 * @return
	 */
	public static <T> Map<String, List<T>> converToMap(List<T> data,
			String... propertyName) throws Exception {

		Map<String, List<T>> map = new HashMap<String, List<T>>();
		if (data == null){
			return map ;
		}
		for (int i = 0; i < data.size(); i++) {
			T obj = data.get(i);
			String key = "";
			for (String p : propertyName) {
				String value =BeanUtils.getProperty(obj, p);
				value=value==null?"null":value;
				key += value+"_";
			}
			key = key.substring(0, key.length()-1);
			List<T> list = map.get(key);
			if (list == null){
				list = new ArrayList<T>();
				map.put(key, list);
			}
			list.add(obj);
		}
		return map;
	}

	/**
	 * 将给定的属性名称propertyName，在数据集中查询对应的JavaBean属性值，
	 * 将属性propertyName的值作为key,并添加至Map中。propertyName应唯一，否则会出现数据覆盖
	 * @param <T>
	 * @param data 数据源
	 * @param keyName 作为主键的属性名称
	 * @return
	 */
	public static <T> Map<String, T> converToMapSingle(List<T> data,
			String... propertyName) throws Exception {
		Map<String, T> map = new HashMap<String, T>();
		if (data == null) {
			return map;
		}
		for (int i = 0; i < data.size(); i++) {
			T obj = data.get(i);
			String key = "";
			for (String p : propertyName) {
				String value =BeanUtils.getProperty(obj, p);
				value=value==null?"null":value;
				key += value+"_";
			}
			key = key.substring(0, key.length()-1);
			map.put(key, obj);
		}
		return map;
	}

	/**
	 * 在给定的数据集中，将每个Map的key与实体类属性名称一致的进行拷贝，
	 * 支持更深度的拷贝，如实体类中包含另一个JavaBean属性也会进行同样的拷贝。
	 *
	 * @param <T> 实体类
	 * @param data 数据集
	 * @param entityCls 实体类的class
	 * @return 转化之后的List
	 */
	public static <T> List<T> converToEntity(List<Map<String,Object>> data ,Class<T> entityCls)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> target = new ArrayList<T>();
		//检查是否有需要转化的数据
		if( null == data || data.size() == 0){
			return target;
		}
		Map<String, Class> complex = BeanHelper.getSimpleOrComplexProperty( entityCls , false);
		T temp = null; Object sb = null; String sbpn = null;
		for (Map<String, Object> map : data) {
			clearNullElement( map );
			temp = entityCls.newInstance();
			//将Map对应的实体类简单属性值进行拷贝
			BeanUtils.populate( temp , map);
			Iterator<String> ite = complex.keySet().iterator();
			while(ite.hasNext()){
				sbpn = ite.next();
				sb = complex.get( sbpn ).newInstance();
				BeanUtils.populate( sb , map);
				BeanUtils.setProperty(temp, sbpn, sb);
			}
			target.add( temp );
		}
		return target;
	}

	/**
	 * <p> 清空Map中元素值为NULL的元素 </p>
	 */
	public static void clearNullElement(Map map){
		if(map == null) return ;
		Object [] vs = map.keySet().toArray();
		for (Object o : vs) {
			if(null == map.get(o))
				map.remove(o);
		}
	}

	public static List<String> converValueToList(List<?> datas, String propertyName)
			throws Exception {
		List<String> result = new ArrayList<String>();
		if (datas == null||datas.size()==0) return result;
		for (Object o : datas) {
			String v = BeanUtils.getProperty(o, propertyName);
			if (StringHelper.isNotEmpty(v))
				result.add(v);
		}
		return result;
	}
	
	public static String[] converValueToArray(List<?> datas, String propertyName)
			throws Exception {
		List<String> result = new ArrayList<String>();
		for (Object o : datas) {
			String v = BeanUtils.getProperty(o, propertyName);
			if (StringHelper.isNotEmpty(v))
				result.add(v);
		}
		return result.toArray(new String[result.size()]);
	}

	public static<T> void setValues(List<T> datas, String propertyName,
			String value) throws IllegalAccessException, InvocationTargetException {
		for (T o : datas) {
			BeanUtils.setProperty(o, propertyName, value);
		}
	}
	/**
	 *
	 * @return 1 a包含b
	 *         0 互不包含
	 *         -1 b包含a
	 */
	public static int compare(List<String> a,List<String> b){
		if(a.size() == 0 || b.size() == 0){
			return 0;
		}
		
		if (a.size()<=b.size()){
			for (String x:a){
				boolean z=false;
				for (String y:b){
					if (x.equals(y)){
						z=true;
						break;
					}
				}
				if (!z)
					return 0;
			}
			return -1;
		} else {
			for (String x:b){
				boolean z=false;
				for (String y:a){
					if (x.equals(y)){
						z=true;
						break;
					}
				}
				if (!z)
					return 0;
			}
			return 1;
		}
	}

	/**
	 * 判断集合是否为空(等于null 或者 isEpty).
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(Collection<? extends Object> list) {
		return list == null || list.isEmpty();
	}
	
	/**
	 * 判断集合是否<font color ="red">不为空</font>(等于null 或者 isEpty).
	 * @param list
	 * @return
	 */
	public static boolean isNotEmpty(Collection<? extends Object> list) {
		return !isEmpty(list);
	}
}