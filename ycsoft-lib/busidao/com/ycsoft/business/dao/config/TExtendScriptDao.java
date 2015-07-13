/**
 * TExtendScriptDao.java	2010/03/08
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExtendScript;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TExtendScriptDao -> T_EXTEND_SCRIPT table's operator
 */
@Component
public class TExtendScriptDao extends BaseEntityDao<TExtendScript> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5042694367764928589L;

	/**
	 * default empty constructor
	 */
	public TExtendScriptDao() {}

}
