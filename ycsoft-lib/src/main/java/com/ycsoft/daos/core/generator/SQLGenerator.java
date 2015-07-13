package com.ycsoft.daos.core.generator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ycsoft.daos.config.Table;


/**
 *  <p>SQL生成器接口，提供了所有对数据库基本操作的sql</P>
 */
public interface SQLGenerator {

	/**
	 * <p> 通过实体对象生成相应的SQL语句</p>
	 * @param maps 字段及值
	 * @param tableName 表名称
	 */
	String getSave(String tableName,Map<String , Object > maps);

	/**
	 *  <p> 删除的sql，将根据在JavaBean配置的主键字段作为删除的条件</p>
	 */
	String getDelete(Table tb);

	/**
	 *  <p> 更新的sql，将根据在JavaBean配置的主键字段作为删除的条件 </p>
	 *  @param tb 表信息对象
	 *  @param columns 需要更新的列
	 */
	String getUpdate(Table tb , List<String> columns) ;

	/**
	 * 获取更新SQL命令，包含参数的值，
	 * @param tb 表信息
	 * @param maps 转载字段及对应的Value的集合
	 * @param key 主键字段
	 */
	public String getUpdate(Table tb,Map<String, Object> maps, Object key);


	/**
	 * <p> 生成产生所有数据的SQL </P>
	 * @param tname 表名
	 */
	String getEntityAll(String tname) ;

	/**
	 * <p> 查询通过主键 </p>
	 * @param tb 存储了表相关的信息对象
	 * @return
	 */
	String getEntityByKey(Table tb) ;

	/**
	 * <p> 获得总记录数的sql </p>
	 * @param tname 表名
	 */
	String getRows(String tname );

	/**
	 * <p> 获取序列的下一个值 </p>
	 * @param sequenceName 序列名
	 * @return
	 */
	String getSeqNextVal(String sequenceName);

	/**
	 * <p> 获取表名的Sql </p>
	 * @param tname 表名
	 */
	String getTableColums(String tname);

	/**
	 * <p> 将指定的sql转换成分页查询命令 </p>
	 * @param sql 原命令
	 * @return 添加了分页命令的sql
	 */
	String getPage(String sql,int start,int limit);

	/**
	 * <p> 将给定的sql命令，获得结果集行数的命令. </p>
	 * @param sql 原命令
	 * @return
	 */
	String getPageCount(String sql);

	/**
	 * <p> 通过给定的Map，将Map作为查询条件，封装SQL </p>
	 * @param tableName  表名称
	 * @param params 对应的查询条件
	 * @return
	 */
	String getFindByMap(String tableName , Map<String, Serializable> params);

	/**
	 * 将<code>appendCondition</code>对象中不为NULL的属性，生成条件语句串
	 * @param appendCondition
	 * @return
	 */
	String and(Map params) ;

	/**
	 * 通过给定的Map,将Map中的参数值以"or"的形式作为查询条件封装SQL,
	 * 忽略NULL元素值
	 * @return
	 */
	String or(Map<String , Object> params);

	/**
	 * 通过给定的List 返回一个符合SQL中in关键字的参数值
	 * @param value
	 * @return
	 */
	String in(Object[] value);

	String setWhereInArray(String name,Object[] value);
}
