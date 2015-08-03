/**
 * CProdPropChangeDao.java	2010/07/13
 */

package com.ycsoft.business.dao.core.prod;


import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdStatusChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CProdPropChangeDao -> C_PROD_PROP_CHANGE table's operator
 */
@Component
public class CProdStatusChangeDao extends BaseEntityDao<CProdStatusChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4710329294236197489L;

	/**
	 * default empty constructor
	 */
	public CProdStatusChangeDao() {}
	
}
