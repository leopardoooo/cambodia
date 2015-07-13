/**
 * RepHeadDao.java	2010/06/21
 */
 
package com.ycsoft.report.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepHead;


/**
 * RepHeadDao -> REP_HEAD table's operator
 */
@Component
public class RepHeadDao extends BaseEntityDao<RepHead> {
	
	/**
	 * default empty constructor
	 */
	public RepHeadDao() {}
	
	public List<RepHead> getRepHeadList(String rep_id) throws JDBCException{
		String sql="select * from rep_head where rep_id=? order by row_seq,col_seq ";
		return this.createQuery(sql,rep_id).list();
	}

}
