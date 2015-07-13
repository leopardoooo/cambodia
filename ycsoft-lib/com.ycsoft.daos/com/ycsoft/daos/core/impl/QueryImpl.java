package com.ycsoft.daos.core.impl;

import static com.ycsoft.commons.helper.LoggerHelper.debug;
import static com.ycsoft.commons.helper.LoggerHelper.isDebugEnabled;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;

import com.ycsoft.daos.core.AbstractQuery;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.generator.SQLGenerator;



/**
 * <p> 执行查询的核心类，主要将查询的结果封装至实体Bean中，
 * 支持分页查询。</p>
 * @see com.ycsoft.daos.core.mapper.EntityMapper
 * @see com.ycsoft.daos.core.impl.SQLQueryImpl
 * @author hh
 */
public class QueryImpl<T> extends AbstractQuery<T> {

	private RowMapper<T> mapper;

	/**
	 * <p> 构造函数初始化SQL,及相关参数 </p>
	 * @param sql
	 * @param params
	 */
	public QueryImpl(
				Class<T> entity ,
				JdbcTemplate jdbcTemplate,
				SQLGenerator sqlGenerator,
				String sql,
				Object...params){
		super(jdbcTemplate,sqlGenerator,sql,params);
		this.mapper = ParameterizedBeanPropertyRowMapper.newInstance( entity );
	}

	private void outPrintSql(int resultSize) {
		if (isDebugEnabled(getClass())) {
			StringBuffer p = new StringBuffer();
			p.append("query result ").append(resultSize).append(" ");
			p.append(sql);
			if (params.length > 0) {
				p.append("{");
				for (Object s : params) {
					p.append(s).append(",");
				}
				p.append("}");
			}
			debug(getClass(), p.toString());
		}
	}

	/**
	 * 重载一个构造参数可传入自定义Mapper的查询器
	 * @param mapper
	 * @param jdbcTemplate
	 * @param sqlGenerator
	 * @param sql
	 * @param params
	 */
	public QueryImpl(
			RowMapper<T> mapper,
			JdbcTemplate jdbcTemplate,
			SQLGenerator sqlGenerator,
			String sql,
			Object...params){
		super(jdbcTemplate,sqlGenerator,sql,params);
		this.mapper = mapper;
	}

	/**
	 * <p> 根据所传递的SQL命令，查询结果，结果集将被封装至实体对象中。 </p>
	 */
	public List<T> list() throws JDBCException {
		List<T> result = jdbcTemplate.query(sql, params, this.mapper);
		outPrintSql(result.size());
		return result;
	}

	public T first() throws JDBCException {
		List<T> result = jdbcTemplate.query(sql, params, this.mapper);
		outPrintSql(result.size());
		return result.size() == 0 ? null : result.get(0);
	}
}
