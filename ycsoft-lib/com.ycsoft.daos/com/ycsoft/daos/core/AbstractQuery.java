package com.ycsoft.daos.core;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ycsoft.daos.core.generator.SQLGenerator;


/**
 * <p> 抽象的查询接口类 </p>
 * @author hh
 * @param <T> 实体对象
 */
public abstract class AbstractQuery<T> implements Query<T> {
	protected String sql ;
	protected Object[] params ;
	protected SQLGenerator sqlGenerator ;
	protected JdbcTemplate jdbcTemplate ;
	//开始位置，
	private Integer start ;
	//显示的条数
	private Integer limit ;

	public AbstractQuery(){
	}
	/**
	 * <p> 构造函数初始化SQL,及相关参数 </p>
	 * @param sql
	 * @param params
	 */
	public AbstractQuery(
				JdbcTemplate jdbcTemplate,
				SQLGenerator sqlGenerator,
				String sql,
				Object...params){
		this.jdbcTemplate = jdbcTemplate;
		this.sqlGenerator = sqlGenerator;
		this.sql = sql ;
		this.params = params;
	}

	public String getSql() {
		return sql;
	}

	public Query<T> setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public Object[] getParams() {
		return params;
	}

	public Query<T> setParams(Object[] params) {
		this.params = params;
		return this;
	}

	public Integer getStart() {
		return start;
	}

	public Query<T> setStart(Integer start) {
		this.start = start;
		return this;
	}

	public Integer getLimit() {
		return limit;
	}

	public Query<T> setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * 根据当前实例对象的参数值，如<code>start、limit</code>值，
	 * 判断是否需要分页查询，如果两个值均不为NULL，则返回格式化后(分页信息)的SQL命令。
	 * 否则返回当前的SQL
	 * @return
	 */
	protected String getPageSQL(){
		if(isPage())
			return sqlGenerator.getPage(sql, start, limit);
		return sql ;
	}

	/**
	 * <p> 获得当前结果集的条数。如果是分页那么现实的就是总函数 </p>
	 * @throws JDBCException
	 */
	public int count()throws JDBCException{
		return jdbcTemplate.queryForInt(sqlGenerator.getPageCount(sql),params);
	}

	/**
	 * <p>根据<code>start、limit</code>判断是否需要进行分页!
	 * 	<tt>true</tt> is need ,<tt>false</tt> isn't need.
	 * </p>
	 */
	protected boolean isPage(){
		return null!=start && null!=limit;
	}

	/**
	 * <p> 分页查询，通过设置<code>start、limit</code>两个属性的值。进行分页查询，
	 * 	如果没有设置相关属性的参数值，将同<code>list</code>函数的效果是一直的。
	 * </p>
	 */
	public Pager<T> page() throws JDBCException {
		if(false == isPage())
			return new Pager<T>(list(),null);
		int total = count() ;
		sql = this.getPageSQL();
		return new Pager<T>(list(),total);
	}
}
