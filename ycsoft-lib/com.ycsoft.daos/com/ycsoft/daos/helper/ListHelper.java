package com.ycsoft.daos.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p> 对集合的操作的工具类 扩展了 Apache <code> ListUtils </code></p>
 */
public class ListHelper extends org.apache.commons.collections.ListUtils{

	/**
	 * <p> 转换一个字符串集合,根据split在list item前和后添加
	 * 转换后的格式如：'item1','item2','item3'</p>
	 * @param startSplit
	 * @param endSplit 字符将添加在list每一项的前后
	 * @return
	 */
	public static String parseListToStr(String startSplit,String endSplit ,List src){
		StringBuffer sb = new StringBuffer();
		for (Object _o : src)
			sb.append( startSplit+_o+endSplit + "," );
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	/**
	 * <p> 转换一个字符串集合,如果是字符类型，则在该字符串的前后添加符号如："'"</p>
	 * @param split 该字符将添加在list每一项的前后
	 * @return
	 */
	public static String parseListToStr(List src){
		StringBuffer sb = new StringBuffer();
		for (Object _o : src) {
			if(_o instanceof String || _o instanceof Date
					|| _o instanceof  Character)sb.append("'"+_o+"'");
			else sb.append(_o);
			sb.append(",");
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	public static String formListToStr(List src){
		StringBuffer sb = new StringBuffer();
		for (Object _o : src) {
			if(_o instanceof String || _o instanceof Date
					|| _o instanceof  Character)sb.append(_o);
			else sb.append(_o);
			sb.append(",");
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * <p> 清空Map中元素值为NULL的元素 </p>
	 * @param src
	 * @return 清理之后的Map对象
	 */
	public static void clearNullElement(Map map){
		if(map == null) return ;
		Object [] vs = map.keySet().toArray();
		for (Object o : vs) {
			if(null == map.get(o))
				map.remove(o);
		}
	}

	/**
	 * <p> 清空columns 与 Bean 中名称相同的属性且值为NULL的属性，并添加至Map中 </p>
	 * @return maps
	 */
	public static Map<String, Object> getElementOfNotNull(List<String> columns , Object bean )throws Exception{
		if(null == bean){
			throw new java.lang.NullPointerException("传入的实体Bean为null!");
		}
		Map<String ,Object> maps = new HashMap<String ,Object> ();
		Object _o = null ;
		for (String _s : columns) {
			_o = PropertyUtils.getProperty(bean, _s);
			if(null !=  _o){
				maps.put(_s, _o);
			}
		}
		return maps ;
	}
	/**
	 * 根据给定的map key 生成一个新的map ,
	 * 并在new map key前面添加给定的字符
	 */
	@SuppressWarnings("unchecked")
	public static Map<String , Object> parseMapKey(Map map , String start)throws Exception{
		if(map == null ) return map ;
		Iterator<String> ite= map.keySet().iterator();
		String key = null;
		Map newMap = new HashMap();
		while(ite.hasNext()){
			key = ite.next();
			newMap.put(start + key , map.get(key) );
		}
		return newMap ;
	}
}
