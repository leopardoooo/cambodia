/**
 * TBusiCodeDao.java	2010/02/25
 */

package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiConfirm;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TBusiCodeDao -> T_BUSI_CODE table's operator
 */
@Component
public class TBusiConfirmDao extends BaseEntityDao<TBusiConfirm> {

	/**
	 *
	 */
	private static final long serialVersionUID = 9206442426616499351L;

	/**
	 * default empty constructor
	 */
	public TBusiConfirmDao() {}
}
