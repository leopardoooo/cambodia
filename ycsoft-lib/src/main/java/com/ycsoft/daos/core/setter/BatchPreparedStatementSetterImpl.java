package com.ycsoft.daos.core.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/**
 * <p> 实现<tt>Spring</tt>的参数接口设置，<p>
 * @see org.springframework.jdbc.core.BatchPreparedStatementSetter
 * @author hh
 */
public class BatchPreparedStatementSetterImpl extends ParameterSetter
			implements BatchPreparedStatementSetter {
	private  int size;
	private  List<Object[]> values ;

	/**
	 * <p> SQL结构中有多个参数时，将对应的参数位置封装置数组中</p>
	 * @param values 所传递的参数行、列的格式
	 */
	public BatchPreparedStatementSetterImpl(List<Object[]> values){
		size = values.size() ;
		this.values = values ;
	}

	/**
	 * <p>  如果批量处理中只有一个参数，使用该函数</p>
	 * @param values
	 */
	public BatchPreparedStatementSetterImpl(Object[] values){
		List<Object[]> params = new ArrayList<Object[]>();
		for (Object o : values) {
			params.add(new Object[]{ o });
		}
		size = params.size() ;
		this.values = params ;
	}

	/**
	 *  获取批量执行的命令数
	 */
	public int getBatchSize() {
		return size;
	}

	/**
	 * 将对应的行数的参数封装进去
	 */
	public void setValues(PreparedStatement ps, int row) throws SQLException {
		setParameters(ps, values.get( row ));
	}
}
