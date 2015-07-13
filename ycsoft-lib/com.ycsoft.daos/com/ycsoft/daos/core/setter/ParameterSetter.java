package com.ycsoft.daos.core.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p> 设置预编译的参数 </p>
 * @author hh
 */
public abstract class ParameterSetter {

	/**
	 * <p>设置sql中的参数值</p>
	 * @param ps  预编译对象
	 * @param params  参数值
	 */
	protected void setParameters(PreparedStatement ps, Object[] params)
			throws SQLException {
		for (int i = 0; i < params.length; i++) {
			ps.setObject((i+1), params[i]);
		}
	}
}
