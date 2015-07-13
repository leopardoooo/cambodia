/**
 * JExecuteRecordDao.java	2011/07/01
 */
 
package com.ycsoft.business.dao.core.job; 

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JExecuteRecord;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JExecuteRecordDao -> J_EXECUTE_RECORD table's operator
 */
@Component
public class JExecuteRecordDao extends BaseEntityDao<JExecuteRecord> {
	
	/**
	 * default empty constructor
	 */
	public JExecuteRecordDao() {}

}
