/**
 * JRecordChangeDao.java	2010/11/28
 */
 
package com.ycsoft.business.dao.core.job; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JRecordChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JRecordChangeDao -> J_RECORD_CHANGE table's operator
 */
@Component
public class JRecordChangeDao extends BaseEntityDao<JRecordChange> {
	
	/**
	 * default empty constructor
	 */
	public JRecordChangeDao() {}

}
