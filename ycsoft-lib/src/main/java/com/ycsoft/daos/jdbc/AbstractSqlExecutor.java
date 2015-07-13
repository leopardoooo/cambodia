package com.ycsoft.daos.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * SQL命令执行器, 为了完成没有使用Spring的情况下去完成一些查询，
 * 如系统参数的初始化，等功能。
 *
 * 可以使用改类提供的函数。这里封装了大量的CRUD函数
 *
 * @author hh
 * @date Feb 3, 2010 3:49:17 PM
 */
public class AbstractSqlExecutor {

	/**
	 * 默认构造函数
	 */
	public AbstractSqlExecutor() {}


	/**
	 * 执行更新的核心函数 ,采用默认的参数设置(? == 代替的参数)
	 * @param conn
	 * @param sql 命令
	 * @return
	 * @throws Exception
	 */
	public int executeUpdate(Connection conn, String sql, Object ...params)throws Exception{
		return executeUpdate(conn, new DefaultParameterSetter(), sql, params);
	}

	/**
	 * 执行更新的核心函数
	 * @param conn 连接对象
	 * @param setter 参数设置对象
	 * @param sql 命令
	 * @param params 参数
	 * @return 返回所影响的行数
	 * @throws Exception
	 */
	public int executeUpdate(Connection conn, ParameterSetter setter, String sql, Object params)throws Exception{
		PreparedStatement ps = conn.prepareStatement( sql );
		setter.setParameters(ps, params);
		return ps.executeUpdate();
	}

}
