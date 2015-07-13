/**
 * JExecResultDao.java	2010/06/08
 */

package com.ycsoft.business.dao.core.job;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JExecResult;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JExecResultDao -> J_EXEC_RESULT table's operator
 */
@Component
public class JExecResultDao extends BaseEntityDao<JExecResult> {

	/**
	 *
	 */
	private static final long serialVersionUID = 8734381058371382480L;

	/**
	 * default empty constructor
	 */
	public JExecResultDao() {}

}
