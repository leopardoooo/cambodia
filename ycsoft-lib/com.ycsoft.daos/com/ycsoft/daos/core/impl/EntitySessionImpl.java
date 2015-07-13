package com.ycsoft.daos.core.impl;

import static com.ycsoft.daos.helper.GenericsHelper.getGenericsClass;
import static com.ycsoft.daos.helper.ListHelper.clearNullElement;
import static com.ycsoft.daos.helper.ListHelper.getElementOfNotNull;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.config.Settings;
import com.ycsoft.daos.config.Table;
import com.ycsoft.daos.core.DataHandler;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.core.PartResultSetExtractor;
import com.ycsoft.daos.core.Query;
import com.ycsoft.daos.helper.BeanHelper;


/**
 * 封装了对实体类常用的操作。使用该类需要指定实体类的类型，
 * 在实体类POJO中必须使用注释的方式配置实体类对应的表信息。
 *
 * @see com.ycsoft.daos.config.POJO
 *
 * @author hh
 * @date Jan 13, 2010 7:22:51 PM
 */
@SuppressWarnings("unchecked")
public class EntitySessionImpl<T> extends AbstractSessionImpl{

	/**
	 *
	 */
	private static final long serialVersionUID = 6993895118802243551L;
	//当前实体类的Class
	private Class entityClass ;

	public EntitySessionImpl(){
		try {
			this.entityClass = getGenericsClass( getClass());
		} catch(Exception e ) {
			try{
				this.entityClass = getGenericsClass( getClass().getSuperclass());
			}catch(Exception _e){
				logger.error( getClass().getName()+"没有使用泛型指定实体类类型!!",_e);
			}
		}
	}

	/**
	 * <p>获得下一个序列的值 , 并且提供了一个可格式化编号的空函数
	 * <code>formatSequence</code></p>
	 * @return
	 * @throws JDBCException
	 */
	public Serializable findSequence()throws JDBCException{
		Table tb = Settings.getTable(getClass());
		String sql = sqlGenerator.getSeqNextVal(tb.getSequenceName());
		return formatSequence(getJdbcTemplate().queryForLong(sql));
	}

	/**
	 * <p>根据序列名称获取序列值
	 * <code>formatSequence</code></p>
	 * @return
	 * @throws JDBCException
	 */
	public Long findSequence(String seqName)throws JDBCException{
		String sql = sqlGenerator.getSeqNextVal(seqName);
		return getJdbcTemplate().queryForLong(sql);
	}

	/**
	 * <p> 将序列值转换成一定格式的主键值</p>
	 * <p> 该函数是一个空函数，可以重写该函数 </p>
	 * @param key 通过序列获得的主键值
	 * @return 格式化后的主键值
	 * @throws Exception
	 */
	protected Serializable formatSequence(Serializable key){
		return key ;
	}

	/**
	 * <p> 获得实体Bean对应表的所有记录 </p>
	 */
	public List<T> findAll() throws JDBCException {
		Table tb = Settings.getTable(getClass());
		String sql = sqlGenerator.getEntityAll(tb.getTableName());
		return createQuery(entityClass , sql).list();
	}

	/**
	 * <p>通过主键获得实体对象</p>
	 * @param keyValue 主键字段值
	 */
	public T findByKey(Serializable keyValue)
			throws JDBCException {
		Table tb = Settings.getTable(getClass());
		if("".equals(tb.getTableName())){
			throw new JDBCException("you need config pk for entity!");
		}
		List<T> lst = createQuery(entityClass ,sqlGenerator.getEntityByKey(tb),keyValue).list();
		if (lst.size() > 0)return lst.get(0);
		return null;
	}

	/**
	 * 按给定的SQL命令及对应的参数。将结果集封装至集合列表中
	 * @throws JDBCException
	 */
	public List<T> findList(String sql, Object... params) throws JDBCException {
		List<T> lst = this.createQuery(entityClass, sql, params).list();
		return lst;
	}


	/**
	 * 按给定的SQL命令及对应的参数，查询一个实体对象， 如果结果集含有多个则只选择第一条记录。
	 * 
	 * @throws JDBCException
	 */
	public T findEntity(String sql , Object ...params) throws JDBCException{
		List<T> lst = findList(sql, params);
		if(lst.size() > 0)
			return lst.get( 0 );
		return null ;
	}

	/**
	 * <p> 查询表中所有的数据并进行分页 </p>
	 * <p> <code>page</code>需要设置的参数包括：
	 * 	<ul><li> start : 开始位置 </li>
	 * 		<li> limit : 每页显示的记录数 </li></ul></p>
	 * @see com.ycsoft.daos.core.Pager
	 * @param page 分页对象，查询的结果将设置到对象相应属性中
	 * @return 封装了总行数、结果集的page
	 */
	public Pager<T> findAll(Integer start,Integer limit) throws JDBCException {
		Table tb = Settings.getTable(getClass());
		String sql = sqlGenerator.getEntityAll(tb.getTableName());
		return createQuery( entityClass , sql)
				.setStart(start)
				.setLimit(limit)
				.page();
	}

	/**
	 * <p> 通过主键删除对应的记录，支持批量的主键删除 </p>
	 * @param keys 主键值
	 * return 每行执行的结果集
	 */
	public int[] remove(Serializable... keys) throws JDBCException {
		String sql = sqlGenerator.getDelete(Settings.getTable(getClass()));
		return executeBatch(sql, keys);
	}

	/**
	 * <p>将指定的一个或多个实体对象持久化</p>
	 * <p> 主键插入方式：自动查询当前实体类是否已经设置主键值，
	 * 如果没有设置主键值，则通过给定的序列名获取虚列值,虚列值会调用<code>#formatSequence </code>
	 *  函数格式化主键值，so 一般需要重写<code>#formatSequence </code>函数。
	 * </p>
	 * @param entitys 需要保存的一个或多个实体对象
	 * @return 返回所执行的行数 查看JDBC 批量更新的返回值
	 */
	public int[] save(T...entitys) throws JDBCException {
		if (entitys.length == 0)
			return null;
		Table table = Settings.getTable(getClass());
		setColumns(table);
		List<String> lst = table.getColumns();
		List<String> sqls = new ArrayList<String>();
		try {
			for (T element : entitys) {
				Map<String , Object> maps = getElementOfNotNull(lst,element);
				if(isNotBlank(table.getPrimaryKey())
							&& !maps.containsKey(table.getPrimaryKey().toLowerCase())
							&& isNotBlank(table.getSequenceName())){
					maps.put(table.getPrimaryKey(), findSequence());
				}
				sqls.add(sqlGenerator.getSave( table.getTableName(), maps));
			}
		} catch (Exception e) {
			throw new JDBCException("生成Save SQL语句出错!检查"+ table.getTableName() +"表结构column在JavaBean是否有对应的属性!" ,e);
		}
		String [] s = new String[sqls.size()];
		sqls.toArray(s);
		return executeBatch(s);
	}

	/**
	 * <p> 根据实体对应的表中的字段及属性值。如果属性值不能NULL，则将被更新。
	 * if need update all,you must see and use <tt>#update<tt> method</p>
	 * @param entity 实体对象(支持多个)
	 */
	public int[] update(T ...entitys) throws JDBCException{
		Table table = Settings.getTable(getClass());
		if (StringHelper.isEmpty(table.getPrimaryKey()))
			throw new JDBCException("主键未设置，无法根据主键更新");

		setColumns(table);
		List<String> lst = getColumnsNotContainsPK(table);
		List<String> sqls = new ArrayList<String>();
		try {
			for (T element : entitys) {
				Map<String , Object> maps= getElementOfNotNull(lst,element);
				if (maps.size() == 0)
					continue;
				Object key = PropertyUtils.getProperty(element, table.getPrimaryKey().toLowerCase());
				if (key ==null)
					throw new JDBCException("主键 "+table.getPrimaryKey()+" 值为空,无法根据主键更新");

				sqls.add(sqlGenerator.getUpdate( table, maps , key ));
			}
		} catch (Exception e) {
			throw new JDBCException("生成Save SQL语句出错!检查"+ table.getTableName() +"表结构column在JavaBean是否有对应的属性!" ,e);
		}
		if (sqls.size()>0){
			String [] s = new String[sqls.size()];
			sqls.toArray(s);
			return executeBatch(s);
		} else {
			return null;
		}

	}
	/**
	 * <p>查询记录，通过所传递的实体对象，根据实体对象中的参数值作为查询条件(NULL值除外)，
	 * 查询符合条件的记录</p>
	 * @param entity 实体对象
	 * @return
	 * @throws JDBCException
	 */
	public List<T> findByEntity(T entity) throws JDBCException {
		Map map;
		try {
			Table table = Settings.getTable(getClass());
			setColumns(table);
			map = getElementOfNotNull(table.getColumns(),entity);
			clearNullElement(map);
		} catch (Exception e) {
			throw new JDBCException("将实体Bean转换到Map中出错!",e);
		}
		return findByMap(map);
	}

	/**
	 * <p>查询记录，通过所传递的实体对象，根据实体对象中的参数值作为查询条件(NULL值除外)，
	 * 查询符合条件的记录，并进行分页</p>
	 * @param entity 实体对象
	 * @return
	 * @throws JDBCException
	 */
	public Pager<T> findByEntity(Integer start,Integer limit , T entity) throws JDBCException{
		Map<String, Serializable> map;
		try {
			map = BeanHelper.describe(entity);
			clearNullElement(map);
		} catch (Exception e) {
			throw new JDBCException("将实体Bean转换到Map中出错!",e);
		}
		return findByMap(start,limit,map);
	}

	/**
	 * <p> 通过map中的<tt>key-value</tt>作为查询条件，获取符合条件的记录 </p>
	 * @param params 查询的条件 ，key:列名,value:值
	 * @return 查询的结果
	 * @throws JDBCException
	 */
	public List<T> findByMap(Map<String , Serializable> params) throws JDBCException {
		Table table = Settings.getTable(getClass());
		String sql = sqlGenerator.getFindByMap(table.getTableName() , params);
		return createQuery( entityClass ,sql, params.values().toArray()).list();
	}

	/**
	 * <p> 通过map中的<tt>key-value</tt>作为查询条件，获取符合条件的记录,并进行分页 </p>
	 * @param params 查询的条件 ，key:列名,value:值
	 * @return 查询的结果
	 * @throws JDBCException
	 */
	public Pager<T> findByMap(Integer start,Integer limit , Map<String , Serializable> params)throws JDBCException{
		Table table = Settings.getTable(getClass());
		String sql = sqlGenerator.getFindByMap(table.getTableName() , params);
		return createQuery( entityClass ,sql, params.values().toArray())
				.setStart(start)
				.setLimit(limit)
				.page();
	}

	/**
	 * 实现父类的<code>createQuery</code>函数。
	 * 默认情况下，继承<code>EntitySessionImpl</code>
	 * 应该使用该函数，而不需要在传入实体类的class
	 * @throws Exception
	 */
	public Query<T> createQuery(String sql , Object... params) throws JDBCException {
		return createQuery( entityClass , sql, params ) ;
	}

	/**
	 * 重载父类的createNameQuery，默认情况下，继承<code>EntitySessionImpl</code>
	 * 应该使用该函数，而不需要在传入实体类的class
	 */
	protected  Query<T> createNameQuery( String sql , Map<String , ? > params )throws JDBCException{
		return createNameQuery(entityClass, sql, params);
	}
	
	/**
	 * 使用默认的entityCls
	 * @see #queryForResult(Class, DataHandler, int, String, Object...)
	 */
	public void queryForResult(DataHandler<T> dataHandler,int limit, String sql, Object...params) throws JDBCException {
		this.queryForResult(this.entityClass, dataHandler, limit, sql, params);
	}
	
	/**
	 * 使用默认的entityCls
	 * @see #queryForResult(Class, DataHandler, String, Object...)
	 */
	public void queryForResult(DataHandler<T> dataHandler, String sql, Object...params) throws JDBCException {
		this.queryForResult(this.entityClass, dataHandler, sql, params);
	}

	/**
	 * <p> 获取对应表所有的字段名称并存储至table对象中
	 *  如果已经在此之前已经设置过，将不会在进行设置，
	 *  该函数是在save、update时会被调用，
	 *  需要根据表中的字段去获取实体类的值，而不是由实体类的属性映射成字段名 </p>
	 */
	private void setColumns(Table table)throws JDBCException{
		if(null == table.getColumns()){
			String sql = sqlGenerator.getTableColums(table.getTableName());
			table.setColumns(findUniques(sql));
		}
	}

	/**
	 * <p>根据表返回表中除主键所有的列<p>
	 * @param table
	 * @return 返回表中除主键所有的列的List
	 */
	private List<String> getColumnsNotContainsPK(Table table){
		List<String> lst = new ArrayList<String>();
		lst.addAll(table.getColumns());
		//因为table.columns中包含所有的列，更新不需要更新主键字段
		lst.remove(table.getPrimaryKey().toLowerCase());
		return lst;
	}

	public Class getEntityClass() {
		return entityClass;
	}

}
