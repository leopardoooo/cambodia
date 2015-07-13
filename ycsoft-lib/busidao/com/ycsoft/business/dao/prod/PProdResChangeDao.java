/**
 * PProdResChangeDao.java	2010/10/10
 */

package com.ycsoft.business.dao.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdResChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;

/**
 * PProdResChangeDao -> P_PROD_RES_CHANGE table's operator
 */
@Component
public class PProdResChangeDao extends BaseEntityDao<PProdResChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5071683867695798336L;

	/**
	 * default empty constructor
	 */
	public PProdResChangeDao() {
	}

}
