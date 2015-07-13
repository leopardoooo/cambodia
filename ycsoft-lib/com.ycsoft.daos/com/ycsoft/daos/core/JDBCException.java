package com.ycsoft.daos.core;

import java.sql.SQLException;

/**
 * <p> JDBC 相关异常处理 </p>
 * @see java.sql.SQLException
 * @author hh
 */
public class JDBCException extends Exception{

	/**
	 *
	 */
	private static final long serialVersionUID = -6088606813843987618L;
	private SQLException sqle;
	private String sql;

	public JDBCException(String msg){
		super(msg);
	}
	public JDBCException(String string, Throwable root) {
		super(string, root);
	}

	public JDBCException(String string, SQLException root) {
		super(string, root);
		sqle=root;
	}

	public JDBCException(String string, SQLException root, String sql) {
		this(string, root);
		this.sql = sql;
	}

	/**
	 * @see java.sql.SQLException
	 * @return String
	 */
	public String getSQLState() {
		return sqle.getSQLState();
	}

	/**
	 * ��ȡ <tt>SQLException</tt> �������.
	 * @see java.sql.SQLException
	 * @return int the error code
	 */
	public int getErrorCode() {
		return sqle.getErrorCode();
	}

	/**
	 * ��ȡ <tt> java.sql.SQLException </tt>
	 *  @return SQLException
	 */
	public SQLException getSQLException() {
		return sqle;
	}

	/**
	 * Get the actual SQL statement that caused the exception
	 * (may be null)
	 */
	public String getSQL() {
		return sql;
	}
}