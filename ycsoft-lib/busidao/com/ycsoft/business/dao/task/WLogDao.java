/**
 * WLogDao.java	2013/09/03
 */
 
package com.ycsoft.business.dao.task; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WLog;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * WLogDao -> W_LOG table's operator
 */
@Component
public class WLogDao extends BaseEntityDao<WLog> {
	
	/**
	 * default empty constructor
	 */
	public WLogDao() {}

}
