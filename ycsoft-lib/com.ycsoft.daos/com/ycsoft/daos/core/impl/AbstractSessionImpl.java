package com.ycsoft.daos.core.impl;

import static com.ycsoft.commons.helper.LoggerHelper.debug;
import static com.ycsoft.commons.helper.LoggerHelper.isDebugEnabled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ycsoft.daos.core.AbstractSessionSupport;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.core.Query;
import com.ycsoft.daos.core.mapper.HashMaper;
import com.ycsoft.daos.helper.BeanHelper;


/**
 * <p> 在<tt>Spring</tt>提供的<tt>NamedParameterJdbcDaoSupport</tt>
 * 进行更深度的封装。</p>
 * @see com.ycsoft.daos.core.Session
 * @see org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport
 * @author hh
 * @param <T> 对应的<tt>POJO</tt>实体对象
 */
public  class AbstractSessionImpl extends AbstractSessionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 8581731834598555307L;

	/**
	 * 取第一列数据
	 * <p> 执行 <b>一行一列或多行一列 </b> 的SQL命令，将结果集封装至List中</p>
	 * @return 封装了所有数据的list集合
	 */
	public List<String> findUniques(String sql,Object...params)throws JDBCException{
		List<Object[]> lst = createSQLQuery(sql, params).list();
		List<String> target = new ArrayList<String>();
		for (Object[] os : lst)
			if (os[0]!=null)
			target.add(os[0].toString());
		return target;
	}

	/**
	 * <p> 执行返回结果为<b>一行一列</b>的sql命令</p>
	 * @param sql SQL命令
	 * @param params 对应的参数值
	 */
	public String findUnique(String sql,Object ...params)throws JDBCException{
		List<String> lst = findUniques(sql, params);
		if(lst.size() > 0) return lst.get(0);
		return null ;
	}

	/**
	 * <p> 执行返回结果集行数的sql命令</p>
	 * @param sql SQL命令
	 * @param params 对应的参数值
	 */
	public int count(String sql,Object ...params)throws JDBCException{
		if (isDebugEnabled(getClass())) {
			StringBuffer p = new StringBuffer();
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
		return getJdbcTemplate().queryForInt(sql, params) ;
	}

	/**
	 * @see #find(String, Map, Class)
	 * @param appendCondition 条件对象
	 * @throws JDBCException
	 */
	public <T> Query<T> find(String sql , Object appendCondition , Class<T> resultType)throws JDBCException{
		Map cond = null ;
		try {
			cond = BeanHelper.describe(appendCondition);
			cond.remove("class");
		} catch (Exception e) {
			throw new JDBCException("将Bean转换为Map时出错!",e);
		}
		return find(sql , cond , resultType );
	}

	/**
	 * 根据给定的<code>appendCondition</code>Map对象，
	 * 将参数值不为NULL的字段，使用And追加在给定的SQL语句结尾部分
	 * @param <T>
	 * @param sql
	 * @param appendCondition
	 * @param resultType
	 * @return
	 * @throws JDBCException
	 */
	public <T> Query<T> find(String sql , Map appendCondition ,Class<T> resultType) throws JDBCException{
		String ac;
		try {
			ac = sqlGenerator.and( appendCondition );
		} catch (Exception e) {
			throw new JDBCException("生成条件语句出错!",e);
		}
		if( ac.length() > 0 ){
			sql += " and " + ac ;
		}
		return this.createQuery( resultType , sql);
	}

	/**
	 * 按给定的SQL命令及对应的参数，查询一个实体对象， 如果结果集含有多个则只选择第一条记录。
	 *
	 * @throws JDBCException
	 */
	public Map<String, Object> findToMap(String sql, Object... params)
			throws JDBCException {
		List<Map<String, Object>> lst = findToList(sql, params);
		return lst.size() > 0 ? lst.get(0): null;
	}

	/**
	 * 按给定的sql命令及对应的参数查询，
	 * 将结果集封装至Map中(字段名作为KEY,字段值作为value)，并添加至List返回
	 * @param sql 参数使用 ?  代替
	 * @throws JDBCException
	 */
	public List<Map<String ,Object>> findToList(String sql , Object ...params)
			throws JDBCException{
		return this.createQuery( new HashMaper(), sql, params).list();
	}

	/**
	 * 按给定的sql命令及对应的参数查询，并进行分页
	 * @see #findToList(String, Object...)
	 */
	public Pager<Map<String ,Object>> findToListPage(Integer start,Integer limit,String sql , Object ...params)
			throws JDBCException{
		return this.createQuery( new HashMaper(), sql, params)
				   .setLimit(limit)
				   .setStart(start)
				   .page();
	}

}
