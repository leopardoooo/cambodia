package com.ycsoft.daos.config;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.GenericsHelper;


/**
 * <p> 加载配置相关信息,包括表名等信息 </p>
 * @see com.ycsoft.daos.config.POJO
 * @see com.ycsoft.daos.config.Table
 * @author hh
 */
@SuppressWarnings("unchecked")
public final class Settings {
	private static Map<String , Table> tables = new HashMap<String,Table>() ;
	private Settings(){}

	/**
	 * <p>通过<tt>POJO</tt>类的模板获取一个对应的<tt>TableBean</tt>的注释信息</p>
	 * @param cls 类的模板
	 */
	public static Table getTable(Class cls)throws JDBCException{
		Class entityClass = null ;
		try {
			entityClass =GenericsHelper.getGenericsClass(cls);
		} catch (Exception e) {
			throw new JDBCException("未指明的泛型，获取失败!",e);
		}
		if(!tables.containsKey(entityClass.getName()))
			addTable(entityClass);
		return tables.get(entityClass.getName());
	}

	/**
	 * <p> 通过一个新的类的Class。获取相应的TableBean注释。 </p>
	 * @param entityClass
	 */
	private static void addTable(Class entityClass){
		try {
			Object entity = Class.forName(entityClass.getName()).newInstance();
			Annotation[] ann = entity.getClass().getAnnotations();
			Table table = null;
			 boolean b = true ;
			for (Annotation annotation : ann) {
				if(annotation instanceof POJO){
					table = new Table((POJO)annotation);
					b = false ;
				}
			}
			//没有配置注释，则采用默认的值
			if(b){
				table = table == null ? new Table(): table ;
				table.setTableName(entityClass.getSimpleName());
			}
			tables.put(entityClass.getName(), table);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
