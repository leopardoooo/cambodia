/**
 * TAcctFeeTypeDao.java	2010/07/18
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TAcctFeeTypeDao -> T_ACCT_FEE_TYPE table's operator
 */
@Component
public class TAcctFeeTypeDao extends BaseEntityDao<TAcctFeeType> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5514153319295346981L;

	/**
	 * default empty constructor
	 */
	public TAcctFeeTypeDao() {}

}
