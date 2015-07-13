package com.ycsoft.commons.helper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;


/**
 * 对JavaBean提供的辅助类，
 * 提供对Apache BeanUtils及Spring BeanUtils没有的封装 </p>
 *
 * @author hh
 * @data Mar 25, 2010 9:46:35 PM
 */
public class BeanHelper {

	private static BeanUtilsBean beanUtils = null;
	static {
		ConvertUtilsBean convertUtils = new ConvertUtilsBean();
		Converter dateConverter = new DateConvert();
		convertUtils.register(dateConverter, Date.class);
		beanUtils = new BeanUtilsBean(convertUtils, new PropertyUtilsBean());
	}


	/**
	 * bean转换器
	 * 当遇到date类型时使用该类转换
	 * @author liujiaqi
	 *
	 */
	static class DateConvert implements Converter {
		public Object convert(Class arg0, Object arg1) {
			String p = (String) arg1;
			if (p == null || p.trim().length() == 0) {
				return null;
			}
			try {
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				return df.parse(p.trim());
			} catch (Exception e) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					return df.parse(p.trim());
				} catch (ParseException ex) {
					return null;
				}
			}

		}
	}

	/**
	 * 判断类的指定属性是否存在
	 * @param entity 实例类的class
	 * @param propName 属性名称
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasProperty( Class entity ,String propName )
			throws SecurityException, NoSuchFieldException{
		try{
			entity.getDeclaredField( propName );
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 获取实体类中复杂的对象
	 * 规则请参考org.springframework.beans.BeanUtils.isSimpleProperty
	 * @param entityCls entity 实体Class
	 * @param isSimple 是为简单对象，否则为复杂对象
	 * @return
	 */
	public static Map<String, Class> getSimpleOrComplexProperty( Class entityCls , boolean isSimple){
		Field [] fields = entityCls.getDeclaredFields();
		Map<String , Class> p = new HashMap<String ,Class>();
		for (Field _f : fields) {
			if(isSimple == org.springframework.beans.BeanUtils.isSimpleProperty(_f.getType())){
				p.put( _f.getName() , _f.getType() );
			}
		}
		return p;
	}

	/**
	 * 动态的执行某个函数
	 * @param bean 目标对象
	 * @param methodName 方法名称
	 * @param paramTypes 方法对应的参数类型
	 * @param paramValues 参数值
	 * @throws Exception
	 */
	public static Object invoke(Object bean,String methodName,
								Class [] paramTypes ,Object[] paramValues)
				throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(bean == null){
			LoggerHelper.warn(BeanHelper.class,"传入的实体类为NULL，无法执行函数!");
			return null;
		}
		Method md = bean.getClass().getMethod(methodName, paramTypes);
		return md.invoke(bean, paramValues);
	}

	@SuppressWarnings("unchecked")
	public static String getPropertyString(Object bean, String propertyName)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (bean == null || propertyName == null || propertyName.equals(""))
			return null;
		Class ownerClass = bean.getClass();

		propertyName = propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + propertyName);
			Object obj = method.invoke(bean);
			if (obj != null)
				return method.invoke(bean).toString();
			else
				return null;
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + propertyName + "' method";
		}
	}



	/**
	 * 动态设置bean属性
	 * 支持了Date类型的设置
	 * @param bean
	 * @param propertyName
	 * @param value
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void setPropertyString(Object bean, String propertyName,
			Object value) throws IllegalAccessException,
			InvocationTargetException {
		beanUtils.setProperty(bean, propertyName, value);
	}

	public static<T> void setNullPropertyEmptyString(T bean) {
		if (bean == null)
			return;
		Field[] fields = bean.getClass().getDeclaredFields();
		String fdname = null;
		for (Field field : fields) {
			fdname = field.getName();
			try {
				if (getPropertyString(bean, fdname) == null)
					setPropertyString(bean, fdname, "");
			} catch (Exception e) {
			}
		}
	}
	
	public static<T> String getFieldType(Class<T> clazz , String propertyName) throws IntrospectionException{
		Field[] fields = clazz.getDeclaredFields();
		String fieldName = null,type = null;
		for(Field field : fields){
			fieldName = field.getName();
			if(fieldName.equals(propertyName)){
				type = field.getGenericType().toString();
				if(type.indexOf("class")==0 && type.substring(0,5).equals("class")){
					type = type.substring(6);
				}
				return type;
			}
		}
		
		Class superClass = clazz.getSuperclass();
		if(null != superClass){
			return getFieldType(superClass, propertyName);
		}
		
		return null;
	}

}
