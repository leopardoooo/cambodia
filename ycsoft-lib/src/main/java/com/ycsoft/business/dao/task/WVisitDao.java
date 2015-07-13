/**
 * WVisitDao.java	2013/08/23
 */
 
package com.ycsoft.business.dao.task; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WVisit;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * WVisitDao -> W_VISIT table's operator
 */
@Component
public class WVisitDao extends BaseEntityDao<WVisit> {
	
	/**
	 * default empty constructor
	 */
	public WVisitDao() {}

}
