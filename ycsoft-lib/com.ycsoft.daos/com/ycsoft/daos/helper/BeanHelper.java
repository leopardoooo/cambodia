package com.ycsoft.daos.helper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.util.Assert;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;

import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.store.MemoryDict;

/**
 * <p> 利用Java 1.5特性，提供对Apache BeanUtils 不足的封装 </p>
 *
 */
@SuppressWarnings("unchecked")
public class BeanHelper extends org.apache.commons.beanutils.BeanUtils{

	/**
	 * 将Bean转换为Map ,Bean的属性名称作为Map的key,属性值作为Map的value
	 * @return
	 */
	public static Map describe(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(null == bean){
			return null ;
		}
		Map map =BeanUtils.describe( bean ) ;
		map.remove("class");
		return map;
	}

	/**
	 * <p>
	 * 通过属性名称，获得该属性对应的Field类型, 循环向上转型,获取对象的DeclaredField, 如果没有找到，则会向上找到父类的模板类
	 * 依次向上，直到找到该属性为止，如果最终没有找到则返回NUll</p>
	 * @param clazz 属性对应类的模板
	 * @param propertyName 属性名称
	 * @return  属性对应的Field
	 * @throws NoSuchFieldException
	 */
	public static Field getDeclaredField(Class clazz, String propertyName)
			throws NoSuchFieldException {
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * <p>强行设置对象的属性值，忽略private、public修饰符</p>
	 *   没有调用setter方法，进行的设置。
	 * </p>
	 */
	public static void forceSetProperty(Object object, String propertyName,
			Object newValue) throws Exception {
		Field field = getDeclaredField(object.getClass(), propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		field.set(object, newValue);
		field.setAccessible(accessible);
	}

	/**
	 * 强行获取对象变量值,忽略private,protected修饰符的限制.
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(Object object, String propertyName)
			throws NoSuchFieldException {
		Field field = getDeclaredField(object.getClass(), propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
		}
		field.setAccessible(accessible);
		return result;
	}
	/**
	 * <p> 根据给定的参数名，按照顺序将参数值添加至数组 </p>
	 * @param propertys 原属性的名称
	 * @param bean 目标对象
	 */
	public static List<Object> parseObjectValueToArray(List<String> propertyNames ,Object bean)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Object>lst = new ArrayList<Object>();
		for (String _s : propertyNames) {
			lst.add(PropertyUtils.getProperty(bean, _s));
		}
		return lst;
	}
	
	/**
	 * 比较新旧两个Bean的属性变化.如果两个对象的Class不一样,且不是继承关系,则抛出异常.如果两个是继承关系,去基类的属性比较.
	 * @param theOldOne 
	 * @param theNewOne
	 * @param properties 需要比较的属性.如果为空,则比较所有的属性.提供的属性bean里找不到,报错.
	 * @return
	 * @throws Exception
	 */ 
	public static String beanchange(Object theOldOne,Object theNewOne, String... properties ) throws Exception {
		
		//两个都为空不能比,一个为空可以
		boolean theOldIsNull = theOldOne==null;
		boolean theNewIsNull = theNewOne==null;
		
		if(theOldIsNull&&theNewIsNull){
			return null;
		}
		
		Class<? extends Object> oldClass = theOldIsNull?null:theOldOne.getClass();
		Class<? extends Object> newClass = theNewIsNull?null:theNewOne.getClass();
		
		PropertyDescriptor [] fieldsDescriptors= null;
				
		PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
		
		if(!theOldIsNull && !theNewIsNull){//两个都不为空
			try{
				Assert.isAssignable(oldClass, newClass, "比较的两个对象不同类且不是继承关系");
				fieldsDescriptors = propertyUtils.getPropertyDescriptors(oldClass);
			}catch (Exception e) {
				Assert.isAssignable(newClass, oldClass, "比较的两个对象不同类且不是继承关系");
				fieldsDescriptors = propertyUtils.getPropertyDescriptors(newClass);
			}
		}else if(theOldIsNull){//老的为空
			fieldsDescriptors = propertyUtils.getPropertyDescriptors(newClass);
		}else if(theNewIsNull){//新的为空
			fieldsDescriptors = propertyUtils.getPropertyDescriptors(oldClass);
		}
		//安全的初始化两组对比的值
		
		//开始比较,结果装入Map,外层,key是fieldName,内存以 "old" "new"为key
		Map<String, Map<String, Object>> dataChanged = new HashMap<String, Map<String, Object>>();
		
		if(properties ==null || properties.length == 0){
			properties = new String [fieldsDescriptors.length -1];
			List<String> fieldNameList = new ArrayList<String>();
			
			for(int index =0;index<fieldsDescriptors.length;index++){
				String name = fieldsDescriptors[index].getName();
				if(!name.equals("class")){
					fieldNameList.add(name);
				}
			}
			properties = fieldNameList.toArray(new String[fieldNameList.size()]);
		}
		//比较值
		for(String fieldName:properties){
			Map<String, Object> changed = new HashMap<String, Object>();
			Object oldValue = null;
			Object newValue = null;
				
			try{
				oldValue = theOldIsNull?null:forceGetProperty(theOldOne, fieldName);//使用这个方法,如果属性不存在,抛错
				newValue = theNewIsNull?null:forceGetProperty(theNewOne, fieldName);//使用这个方法,如果属性不存在,抛错
			}catch (Exception e) {
				throw new IllegalArgumentException("Bean的属性参数有错,bean里没有属性 '" + fieldName +"' ");
			}
			
			changed.put("old", oldValue);
			changed.put("new", newValue);
			dataChanged.put(fieldName, changed);
		}
		
		StringBuilder resultBuffer = new StringBuilder();
		String format = "%s:%s=>%s";
		
		for(Entry<String, Map<String, Object>> entry:dataChanged.entrySet()){
			Map<String, Object> field = entry.getValue();
			Object newVal = field.get("new");
			Object oldVal = field.get("old");
			newVal = null == newVal ? "":newVal;
			oldVal = null == oldVal ? "":oldVal;
			
			
			if (com.ycsoft.commons.helper.StringHelper.isEmpty(newVal.toString())
					&& com.ycsoft.commons.helper.StringHelper.isEmpty(oldVal.toString())) {// 两个都为空,continue,null作为空字符串一样对待
				continue;
			}
			
			String oldValStr = "null";
			String newValStr = "null";
			
			if(com.ycsoft.commons.helper.StringHelper.isEmpty(newVal.toString()) ){
				if(oldVal.getClass().isAssignableFrom(Date.class)){
					oldValStr = DateHelper.format((Date)oldVal);
				}else{
					oldValStr = oldVal.toString();
				}
				resultBuffer.append(String.format(format, entry.getKey(),oldValStr,newValStr)).append(",");
			}else if(com.ycsoft.commons.helper.StringHelper.isEmpty(oldVal.toString()) ){
				if(newVal.getClass().isAssignableFrom(Date.class)){
					newValStr = DateHelper.format((Date)newVal);
				}else{
					newValStr = newVal.toString();
				}
				resultBuffer.append(String.format(format, entry.getKey(),oldValStr,newValStr)).append(",");
			}else{
				if(oldVal.getClass().isAssignableFrom(Date.class)){//两个类型必须一样
					//TODO 空值判断。。。
					oldValStr = DateHelper.format((Date)oldVal);
					newValStr = DateHelper.format((Date)newVal);
				}else{
					oldValStr = oldVal.toString();
					newValStr = newVal.toString();
				}
				boolean notChanged = !oldValStr.equals(newValStr) ;
				if(notChanged){
					resultBuffer.append(String.format(format, entry.getKey(),oldValStr,newValStr)).append(",");
				}
			}
			
			
		}
		
		String result = resultBuffer.toString();
		if(result.length() != 0 ){
			result = StringHelper.delEndChar(result, 1);
		}
		return result;
	}
	
	/**
	 * 比较两个集合类的变化.两个集合可以为空集合,不能为null.两个List的反省参数必须一致.</br>
	 * 如果fieldName为空,则认为传入的List的泛型参数类型是String.如果不是则抛出IllegalArgumentException.</br>
	 * 如果传入的两个List的泛型参数类型 <strong><font color="red">不是 String</font></strong>,则必须指定fieldName.否则抛出 IllegalArgumentException.</br>
	 * 如果传入的两个List的泛型参数类型  <strong><font color="red">是 String</font></strong>,则忽略fieldName.</br>
	 * 另外,不考虑list里面有重复的数据(如果不是Stirng的list,是指bean的fieldName属性没有相同的).
	 * @param oldList 老的集合.
	 * @param newList	新的集合.
	 * @param fields	要比较的字段名称,可以为空,为空的时候说明两个集合都是字符串.否则,取集合里的这个值.
	 * @param dictKey	字典键.
	 * @return
	 */
	public static <T> String listchange(List<T> oldList,List<T> newList,String ... fields) throws Exception{
		
		if(fields!=null&&fields.length>0){
			List<String> temp=new ArrayList<String>();
			for(String o:fields){
				if(o!=null&&!o.trim().equals("")){
					temp.add(o);
				}
			}
			fields=temp.toArray(new String[temp.size()]);
		}
		if ((oldList == null && newList == null)
				|| (oldList == null && newList != null && newList.size() == 0)
				|| (newList == null && oldList != null && oldList.size() == 0)) {//保证不同时为null 或者一个为null 一个为空
			throw new IllegalArgumentException("两个集合不能都为空");
		}
		
		boolean dataChanged = false;
		
		Map<String, String> oldValMap = new HashMap<String, String>();
		Map<String, String> newValMap = new HashMap<String, String>();
		
		Class oldParamType = null;
		Class newParamType = null;
		
		if(oldList ==null){
			oldList = new ArrayList<T>();
			dataChanged = true;
		}else if(newList ==null){
			newList = new ArrayList<T>();
			dataChanged = true;
		}
		
		if( oldList.size() ==0 && newList.size() ==0 ){//空组,返回空字符串
			return "";
		}
		
		if(oldList.size() == 0){
			newParamType = newList.get(0).getClass();
		}else if(newList.size() == 0){
			oldParamType = oldList.get(0).getClass();
		}else{
			newParamType = newList.get(0).getClass();
			oldParamType = oldList.get(0).getClass();
		}
		
		boolean isString = oldParamType == null ? newParamType.equals(String.class) :oldParamType.equals(String.class) ;
		
		if( (fields ==null || fields.length ==0) && !isString){//传入List为字符串类型,fieldName 为空
			throw new IllegalArgumentException("属性名为空的时候传入的List的参数类型应为 String");
		}
		
		for(T obj:oldList){
			String valStr = "";
			if(isString){
				valStr = obj.toString();
				oldValMap.put(valStr, valStr);
				continue;
			}
			
			Object fieldValue = null;
			try{
//				fieldValue = forceGetProperty(obj, fields);
				for(String field:fields){
					fieldValue = forceGetProperty(obj, field);
					if(fieldValue ==null){
						fieldValue = "null";
					}
					
					if(fieldValue.getClass().isAssignableFrom(Date.class)){
						valStr += DateHelper.format((Date)fieldValue) + "_";
					}else{
						valStr += fieldValue.toString() + "_";
					}
				}
			}catch (Exception e) {
				throw new IllegalArgumentException("List里的对象没有 '" + fields + "' 属性!");
			}
			/*
			if(fieldValue == null){
				throw new IllegalArgumentException("传入的数据错误!需要的属性 " + fields +" 的值为空 !");
			}
			*/
			valStr = valStr.substring(0, valStr.length() -1);
			oldValMap.put(valStr, valStr);
			
		}
		
		for(Object obj:newList){
			String valStr = "";
			if(isString){
				valStr = obj.toString();
				newValMap.put(valStr, valStr);
				continue;
			}
			Object fieldValue = null;
			try{
//				fieldValue = forceGetProperty(obj, fields);
				for(String field:fields){
					fieldValue = forceGetProperty(obj, field);
					if(fieldValue ==null){
						fieldValue = "null";
					}
					
					if(fieldValue.getClass().isAssignableFrom(Date.class)){
						valStr += DateHelper.format((Date)fieldValue) + "_";
					}else{
						valStr += fieldValue.toString() + "_";
					}
				}
			}catch (Exception e) {
				throw new IllegalArgumentException("List里的对象没有 '" + fields + "' 属性!");
			}
			/*
			if(fieldValue == null){
				throw new IllegalArgumentException("传入的数据错误!需要的属性 " + fields +" 的值为空 !");
			}
			*/
			valStr = valStr.substring(0, valStr.length() -1);
			newValMap.put(valStr, valStr);
		}
		
		if(!dataChanged){//都不为空的时候再比较下
			if(newValMap.size() !=oldValMap.size()){
				dataChanged = true;
			}else{
				for(String key : newValMap.keySet()){
					if(oldValMap.get(key) == null){
						dataChanged = true;
						break;
					}
				}
			}
		}
		
		String format = "%s %s=>%s";//属性名,旧值,新值  
		String fieldName = "";
		if(dataChanged){
			if(fields !=null && fields.length ==1){
				fieldName = StringHelper.isEmpty(fields[0])? "":fields[0]+":";//如果是字符串,省略null
			}
//			String oldValStr = JsonHelper.fromObject(oldValMap.values());
			String oldValStr =  "[";
			for(String str :oldValMap.values()){
				oldValStr += str + ",{splitFlag}";
			}
			oldValStr = oldValStr.substring(0, oldValStr.length());
			oldValStr += "]";
//			String newValStr = JsonHelper.fromObject(newValMap.values());
			
			String newValStr =  "[";
			for(String str :newValMap.values()){
				newValStr += str + ",{splitFlag}";
			}
			newValStr = newValStr.substring(0, newValStr.length());
			newValStr += "]";
			
			return String.format(format, fieldName,oldValStr,newValStr);
		}
		return null;
	}
	
}

