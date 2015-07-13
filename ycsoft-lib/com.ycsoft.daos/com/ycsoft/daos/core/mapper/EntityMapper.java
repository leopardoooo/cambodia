package com.ycsoft.daos.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.beanutils.BeanUtils;

import com.ycsoft.commons.helper.LoggerHelper;


/**
 * <p> 处理SQL的返回结果集，将结果集封装至对应的实体对象中。 </p>
 * @author hh
 */
@SuppressWarnings("unchecked")
public class EntityMapper extends AbstractMapper {
	//实体的模板
	private Class entityClass ;

	public EntityMapper(Class entity){
		entityClass = entity ;
	}

	/**
	 * <p> 实现row result 的转换。</p>
	 */
	public Object mapRow(ResultSet rs, int row) throws SQLException {
		if(columnsIsNull())setCurrentColumns(rs);
		Object item = null , value = null ;
		String c = "" ;
		try {
			item = entityClass.newInstance();
			for (String element : columns) {
				c = element;
				value = rs.getObject( c );
				if( value == null ){
					continue ;
				}
				//logger.debug( c + " -> " + o.getClass().getName());
				BeanUtils.setProperty(item, element.toLowerCase(), value );
			}
		} catch (InstantiationException e) {
			LoggerHelper.error(EntityMapper.class, e.getMessage());
			throw new SQLException("转换结果集时，实例化" + entityClass.getName() + "实体对象出错!");
		} catch (Exception e) {
			LoggerHelper.error(EntityMapper.class, e.getMessage());
			throw new SQLException("设置实体属性值出错!" + c );
		}
		return item;
	}
}
