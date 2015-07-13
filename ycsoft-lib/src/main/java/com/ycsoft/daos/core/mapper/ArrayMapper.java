package com.ycsoft.daos.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p> 处理返回结果集，将结果集封装至<tt>Object Array</tt> </p>
 * @author hh
 */
public class ArrayMapper extends AbstractMapper {

	private Object[] record ;


	public Object mapRow(ResultSet rs, int row) throws SQLException {
		if(columnsIsNull())setCurrentColumns(rs);
		record = new Object[columns.length];
		for(int i=0;i<columns.length;i++)
			record[i] = rs.getObject(columns[i]);
		return record;
	}
}
