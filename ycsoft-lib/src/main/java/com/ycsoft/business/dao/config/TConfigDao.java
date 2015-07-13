/**
 * TConfigDao.java	2010/09/05
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TConfig;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TConfigDao -> T_CONFIG table's operator
 */
@Component
public class TConfigDao extends BaseEntityDao<TConfig> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7228211911585616038L;

	/**
	 * default empty constructor
	 */
	public TConfigDao() {}

}
