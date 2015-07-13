package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDimensionLevel;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

@Component
public class RepDimensionLevelDao extends BaseEntityDao<RepDimensionLevel>{

	/**
	 * 查询一个维的层
	 * @param dim
	 * @return
	 * @throws ReportException
	 */
	public List<RepDimensionLevel> queryLevelsByDimension(String dim) throws ReportException{
		try {
			return this.createQuery("select * from rep_dimension_level where id=?", dim).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	public List<QueryKeyValue> queryLevelsIdNameByDim(String dim_id) throws ReportException{
		try {
			return this.createQuery(QueryKeyValue.class, "select dim_level id,dim_level_name name from rep_dimension_level where id=?", dim_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
}
