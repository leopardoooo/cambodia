/**
 * TTaskTypeDao.java	2010/03/18
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.TTaskType;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TTaskTypeDao -> T_TASK_TYPE table's operator
 */
@Component
public class TTaskTypeDao extends BaseEntityDao<TTaskType> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6479755833842054295L;

	/**
	 * default empty constructor
	 */
	public TTaskTypeDao() {}

}
