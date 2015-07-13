package com.ycsoft.daos.core.impl;

import static com.ycsoft.commons.helper.LoggerHelper.debug;
import static com.ycsoft.commons.helper.LoggerHelper.isDebugEnabled;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.ycsoft.daos.core.AbstractQuery;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.generator.SQLGenerator;

/**
 * 根据命名参数名称，与实体类的属性，设置参数值
 *
 * @author hh
 * @date Jan 13, 2010 9:31:39 PM
 */
@SuppressWarnings("unchecked")
public class NameQueryImpl<T> extends AbstractQuery<T> {

	private Map params ;
	private NamedParameterJdbcTemplate jdbcTemplate ;
	private Class entityCls ;

	public NameQueryImpl(String sql ,
						 NamedParameterJdbcTemplate nameJT  ,
						 SQLGenerator sqlGenerator ,
					     Map params ,
					     Class entityCls ){
		this.sql = sql ;
		this.jdbcTemplate = nameJT ;
		this.sqlGenerator = sqlGenerator;
		this.params = params ;
		this.entityCls = entityCls ;
		
	}
	
	/**
	 * <p>
	 * 获得当前结果集的条数。如果是分页那么现实的就是总函数
	 * </p>
	 * 
	 * @throws JDBCException
	 */
	public int count()throws JDBCException{
		return jdbcTemplate.queryForInt(sqlGenerator.getPageCount(sql),params);
	}
	
	public void outPrintSql(int resultSize){
		if (isDebugEnabled(getClass())) {
			StringBuffer p = new StringBuffer();
			p.append("query result ").append(resultSize).append(" ");
			p.append(sql).append("{");
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				p.append("(").append(key).append(":").append(params.get(key))
						.append(")");
			}
			p.append("}");
			debug(getClass(), p.toString());
		}
	}

	public List<T> list() throws JDBCException {
		List result = jdbcTemplate.query(sql, params,
				new BeanPropertyRowMapper(entityCls));
		outPrintSql(result.size());
		return result;
	}

	public T first() throws JDBCException {
		List<T> result = jdbcTemplate.query(sql, params,
				new BeanPropertyRowMapper(entityCls));
		outPrintSql(result.size());
		return result.size() == 0 ? null : result.get(0);
	}
}
