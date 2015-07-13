/**
 * RepColumnsDao.java	2010/07/16
 */
 
package com.ycsoft.report.dao.config; 

import java.util.List;
import java.util.Vector;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepColumn;


/**
 * RepColumnsDao -> REP_COLUMNS table's operator
 * @deprecated
 */
@Component
public class RepColumnDao extends BaseEntityDao<RepColumn> {
	
	/**
	 * default empty constructor
	 */
	public RepColumnDao() {}
	
	/**
	 *  
	 * @param rep_id
	 * @return
	 * @throws JDBCException
	 */
	public List<RepColumn> getRepColumnsList(String rep_id) throws JDBCException{
		this.createQuery(RepColumn.class, "");
		//this.queryForResult(entityCls, dataHandler, limit, sql, params)
		return this.findList("select * from rep_column where rep_id=? order by col_index",rep_id);
	}
	
}
