package com.ycsoft.daos.helper;

import java.lang.reflect.ParameterizedType;

/**
 * <p> 泛型帮助类 </p>
 * @author hh
 */
@SuppressWarnings("unchecked")
public class GenericsHelper {

	/**
	 * <p> 通过一个类的Class,获取该类的泛型的模板 </p>
	 * @param cls 类的模板
	 * @return 泛型的模板
	 * @throws Exception
	 */
	public static Class getGenericsClass(Class cls)throws Exception{
		ParameterizedType superClass = (ParameterizedType) cls.getGenericSuperclass();
		Class target =(Class)superClass.getActualTypeArguments()[0];
		return target ;
	}

	/**
	 *  获取一个指定父类名称的父类Class，
	 * @param cls
	 * @param superClass
	 * @return
	 */
	public static Class getSuperClass(Class cls  , String clsName){
		Class temp = cls ;
		while(temp != null && temp.getSuperclass() !=null){

			if(clsName.equals(temp.getSuperclass().getName())){
				return temp ;
			}
			temp = temp.getSuperclass();
		}
		return null ;
	}
}
