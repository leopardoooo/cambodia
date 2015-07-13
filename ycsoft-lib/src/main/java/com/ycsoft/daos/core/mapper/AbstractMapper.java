package com.ycsoft.daos.core.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


/**
 * <p> 提供一些常用的属性。具体结果集的转换，由子类完成! </p>
 * @author hh
 */
public abstract class AbstractMapper<T> implements RowMapper<T> {
	//当前的结果集中的列
	protected String[] columns ;

	/**
	 *<p> 获得当前游标所有的列数 <p>
	 * @throws Exception
	 */
	protected void setCurrentColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		columns = new String[rsmd.getColumnCount()];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = rsmd.getColumnName(i + 1);
		}
	}

	/**
	 * columns is null ，value is null ，the return value is <tt>true</tt> ,
	 * otherwise the return value is <tt>false</tt>.
	 */
	protected boolean columnsIsNull(){
		return columns==null?true:false;
	}
}
