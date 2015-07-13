package com.ycsoft.daos.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 设置参数的接口定义
 *
 * @author hh
 * @date Feb 3, 2010 4:02:10 PM
 */
public interface ParameterSetter {


	void setParameters(PreparedStatement ps , Object o)throws SQLException;

}
