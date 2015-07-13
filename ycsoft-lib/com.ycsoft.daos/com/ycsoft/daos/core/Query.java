package com.ycsoft.daos.core;

import java.util.List;

/**
 * <p> 执行查询的接口定义 </p>
 * @author hh
 */
public interface Query<T> {

	/**
	 * 设置需要执行的SQL命令
	 * @return 当前对象
	 */
	public Query<T> setSql(String sql);

	/**
	 * 设置需要执行的SQL命令的所需参数
	 * @return 当前对象
	 */
	public Query<T> setParams(Object[] params);

	/**
	 * 设置分页的开始位置
	 * @return 当前对象
	 */
	public Query<T> setStart(Integer start);

	/**
	 * 设置分页的条数
	 * @return 当前对象
	 */
	public Query<T> setLimit(Integer limit) ;


	/**
	 * <p> 根据设置的参数信息，执行SQL命令，并自动将结果集添加至对应的<tt>POJO</tt>对象中,
	 * 	每一个实体对象对应一个ResultSet row,将所有的实体对象装载至集合并返回。<p>
	 */
	public List<T> list()throws JDBCException ;

	/**
	 * 返回查询结果的第一行
	 */
	public T first()throws JDBCException ;

	/**
	 * <p> 分页查询 </p>
	 * @return
	 * @throws JDBCException
	 */
	public Pager<T> page()throws JDBCException ;

}
