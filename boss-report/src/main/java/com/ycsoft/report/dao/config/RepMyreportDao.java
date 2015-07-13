/**
 * RepMyreportDao.java	2010/08/13
 */

package com.ycsoft.report.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepMyreport;
/**
 * RepMyreportDao -> REP_MYREPORT table's operator
 */
@Component
public class RepMyreportDao extends BaseEntityDao<RepMyreport> {

	/**
	 * default empty constructor
	 */
	public RepMyreportDao() {
	}
	
	public void delete(String optr_id,String rep_id) throws JDBCException{
		this.executeUpdate("delete from rep_myreport where optr_id=? and rep_id=?", optr_id,rep_id);
	}
		
}
