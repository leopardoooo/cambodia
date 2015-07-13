/**
 * TPrintitemDao.java	2010/04/12
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TPrintitem;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TPrintitemDao -> T_PRINTITEM table's operator
 */
@Component
public class TPrintitemDao extends BaseEntityDao<TPrintitem> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4804580847496737856L;

	/**
	 * default empty constructor
	 */
	public TPrintitemDao() {}

}
