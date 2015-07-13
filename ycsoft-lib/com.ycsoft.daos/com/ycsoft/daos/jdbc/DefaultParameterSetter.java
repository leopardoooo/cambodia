package com.ycsoft.daos.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 默认的参数设置器。支持?的参数设置
 *
 * @author hh
 * @date Feb 3, 2010 4:20:09 PM
 */
public class DefaultParameterSetter implements ParameterSetter {

	public void setParameters(PreparedStatement ps, Object o) throws SQLException {
		Object [] params = (Object[])o;
		for (int i = 0; i < params.length; i++) {
			ps.setObject( (i+1) ,  params[i] );
		}
	}

}
