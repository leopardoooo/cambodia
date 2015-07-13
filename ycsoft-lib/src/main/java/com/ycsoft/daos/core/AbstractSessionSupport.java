package com.ycsoft.daos.core;

import static com.ycsoft.commons.helper.LoggerHelper.*;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.daos.core.generator.SQLGenerator;
import com.ycsoft.daos.core.impl.NameQueryImpl;
import com.ycsoft.daos.core.impl.QueryImpl;
import com.ycsoft.daos.core.mapper.ArrayMapper;
import com.ycsoft.daos.core.setter.BatchPreparedStatementSetterImpl;
import com.ycsoft.daos.helper.BeanHelper;


/**
 * <p> 抽象的<tt>Session</tt>定义，主要提供了底层与<tt>Spring JDBCTemplate</tt>的封装 ,
 *  其它如实体类的保存、更新、删除、查询等操作。需要由其子类完成!</p>
 * @see org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport
 * @author hh
 */
public abstract class AbstractSessionSupport extends
					NamedParameterJdbcDaoSupport implements Session {
	//SQL生成器
	protected SQLGenerator sqlGenerator ;

	/**
	 * <p>执行更新的操作</p>
	 * @param sql 命令
	 * @param params 对应占位符?参数的值
	 * @return 影响的行数
	 */
	public int executeUpdate( String sql, Object ...params)throws JDBCException{
		int result =  getJdbcTemplate().update(sql, params );
		if (isDebugEnabled(getClass())) {
			StringBuffer p = new StringBuffer();
			p.append("update result ").append(result);
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
		return result;
	}

	/**
	 * <p> 执行sql语句结构一致的批量更新操作。当sql命令结构一致时采用该函数</p>
	 * @param sql 命令
	 * @param params  参数值(多个)每一项均为数组格式分别对应命令中的占位符
	 * @return 查看 JDBC 批量更新的返回值
	 */
	public int[] executeBatch(String sql , List<Object[]> params)throws JDBCException{
		return getJdbcTemplate().batchUpdate( sql ,
				new BatchPreparedStatementSetterImpl( params ));
	}

	/**
	 * <p> 执行sql语句结构一致的批量更新操作。当sql命令结构一致时采用该函数
	 * ,当sql命令中只包含一个参数的时候，使用该函数</p>
	 */
	public int[] executeBatch(String sql , Object[] params)throws JDBCException{
		return getJdbcTemplate().batchUpdate( sql ,
				new BatchPreparedStatementSetterImpl( params ));
	}

	/**
	 * <p> 批量更新已经封装好的SQL命令 </p>
	 * @param sql
	 * @throws JDBCException
	 */
	public int[] executeBatch(String [] sql ) throws JDBCException{
		int [] result = getJdbcTemplate().batchUpdate(sql);
		if (isDebugEnabled(getClass())) {
			StringBuffer p = new StringBuffer();
			for (int i=0 ;i<sql.length ;i++){
				String s = sql[i];
				p.append("update result ").append(result[i]).append(" ").append(s).append("\n");
			}
			debug(getClass(), p.toString());
		}
		return result;
	}

	/**
	 * 按给定的SQL命名参数 ，将对应的Map中的参数值替换
	 * @param sql
	 * @param params
	 * @return
	 * @throws JDBCException
	 */
	public int execNameParamUpdate( String sql, Map<String,?> params)throws JDBCException{
		return getNamedParameterJdbcTemplate().update(sql, params);
	}

	/**
	 * <p> SQL查询函数,会将查询的结果全部封装至对应的实体对象中<p>
	 * @see com.ycsoft.daos.core.Query & cn.ycsoft.core.QueryImpl
	 * @param sql SQL命令
	 * @param params 对应的参数值
	 * @return 查询接口
	 * @throws Exception
	 */
	public <T> Query<T> createQuery(Class<T> cls , String sql,Object... params) throws JDBCException {
		return new QueryImpl<T>(
				cls,
				getJdbcTemplate(),
				sqlGenerator,
				sql,
				params
		);
	}

	/**
	 * 创建一个自定义<code>RowMapper</code>的查询器，
	 * @param mapper 处理结果的查询器
	 * @param sql 要执行的SQL命令
	 * @param param 可替换的参数值
	 * @return
	 * @throws JDBCException
	 */
	public <T> Query<T> createQuery( RowMapper<T> mapper , String sql ,Object...params)throws JDBCException{
		return new QueryImpl<T>(
				mapper,
				getJdbcTemplate(),
				sqlGenerator,
				sql,
				params
		);
	}

	/**
	 * 创建查询对象，SQL参数采用命名参数。
	 * @param paramEntity 参数实体类
	 * @return List集合，实体类类型为传入的T 实体类型
	 */
	public <T> Query<T> createNameQuery( String sql , T paramEntity)throws JDBCException{
		try {
			return new NameQueryImpl<T>(
							sql ,
							getNamedParameterJdbcTemplate() ,
							sqlGenerator ,
							BeanHelper.describe(paramEntity),
							paramEntity.getClass()
			);
		} catch (Exception e) {
			throw new JDBCException("创建一个查询对象失败!" , e );
		}
	}

	/**
	 *  创建查询对象，SQL参数采用命名参数。
	 * @param params 设置SQL命令中对应的参数名称的值到Map中传入
	 * @param cls 返回类型的Class
	 */
	public <T> Query<T> createNameQuery(Class<T> cls, String sql,
			Map<String, ?> params) throws JDBCException {
		return new NameQueryImpl<T>(
						sql ,
						getNamedParameterJdbcTemplate() ,
						sqlGenerator ,
						params,
						cls
		);
	}

	/**
	 * <p> SQL查询函数,将查询的结果封装至集合数组中<p>
	 * @see com.ycsoft.daos.core.Query & cn.ycsoft.core.SQLQueryImpl
	 * @param sql SQL命令
	 * @param params 对应的参数值
	 * @return 查询接口
	 * @throws Exception
	 */
	public Query<Object[]> createSQLQuery(String sql,Object... params) throws JDBCException {
		return new QueryImpl<Object[]>(
				new ArrayMapper(),
				getJdbcTemplate(),
				sqlGenerator,
				sql,
				params
		);
	}
	
	/**
	 * 默认读取 {@link PartResultSetExtractor#DEFAULT_LIMIT}条数
	 * @see #queryForResult(Class, DataHandler, String, Object...)
	 */
	public <E> void queryForResult(Class<E> entityCls,DataHandler<E> dataHandler, String sql, Object...params) throws JDBCException {
		JdbcTemplate jt = getJdbcTemplate();
		jt.setFetchSize(PartResultSetExtractor.DEFAULT_FETCH_SIZE);
		jt.query(sql, params, newResultSetExtractor(entityCls, dataHandler, PartResultSetExtractor.DEFAULT_LIMIT));
		//this.queryForResult(entityCls, dataHandler, PartResultSetExtractor.DEFAULT_LIMIT, sql, params);
	}
	
	/**
	 * <p>读取结果集，分批读取游标中的数据，读取的条数按给定limit参数，交给dataHandler回调函数处理</p>
	 * 
	 * @param entityCls 实体类
	 * @param dataHandler 数据处理器
	 * @param limit 每次处理的条数
	 * @param sql 命令
	 * @param params sql 参数
	 * @throws JDBCException
	 */
	public <T> void queryForResult(Class<T> entityCls,DataHandler<T> dataHandler,int limit, String sql, Object...params) throws JDBCException {
		JdbcTemplate jt = getJdbcTemplate();
		jt.setFetchSize(PartResultSetExtractor.DEFAULT_FETCH_SIZE);
		jt.query(sql, params, newResultSetExtractor(entityCls, dataHandler, limit));
	}
	
	//create a PartResultSetExtractor
	protected <T> PartResultSetExtractor<T> newResultSetExtractor(Class<T> entityCls, 
			DataHandler<T> dataHandler, int limit){
		return new PartResultSetExtractor<T>(entityCls, dataHandler, limit);
	}

	public SQLGenerator getSqlGenerator() {
		return sqlGenerator;
	}

	public void setSqlGenerator(SQLGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
	}
}
