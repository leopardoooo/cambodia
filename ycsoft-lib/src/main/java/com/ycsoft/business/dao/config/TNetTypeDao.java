/**
 * TBandNetTypeDao.java	2010/06/30
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TNetType;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TNetTypeDao -> T_NET_TYPE table's operator
 */
@Component
public class TNetTypeDao extends BaseEntityDao<TNetType> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8135474209867541793L;

	/**
	 * default empty constructor
	 */
	public TNetTypeDao() {}

}
