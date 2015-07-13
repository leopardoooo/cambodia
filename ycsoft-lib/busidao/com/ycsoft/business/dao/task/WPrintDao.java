/**
 * WPrintDao.java	2013/08/23
 */
 
package com.ycsoft.business.dao.task; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.WPrint;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * WPrintDao -> W_PRINT table's operator
 */
@Component
public class WPrintDao extends BaseEntityDao<WPrint> {
	
	/**
	 * default empty constructor
	 */
	public WPrintDao() {}

}
