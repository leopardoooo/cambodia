/**
 * RepDatabaseDao.java	2010/06/21
 */
 
package com.ycsoft.report.dao.config; 

import org.springframework.stereotype.Component;

import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDatabase;


/**
 * RepDatabaseDao -> REP_DATABASE table's operator
 */
@Component
public class RepDatabaseDao extends BaseEntityDao<RepDatabase> {
	
	/**
	 * default empty constructor
	 */
	public RepDatabaseDao() {}
	
	public RepDatabase getRepSortDB() throws JDBCException{
		String sql="select database,name,driverclass,url,maxidletime" +
				",maxpoolsize,minpoolsize,testquerysql,testoeriod,username,password,encrypt " +
				" from busi.rep_sort_db ";
		return this.createQuery(sql).first();
	}

}
