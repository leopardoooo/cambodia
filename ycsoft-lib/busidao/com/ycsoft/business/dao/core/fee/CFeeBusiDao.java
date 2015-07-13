/**
 * CFeeBusiDao.java	2010/04/08
 */

package com.ycsoft.business.dao.core.fee;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeeBusi;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CFeeBusiDao -> C_FEE_BUSI table's operator
 */
@Component
public class CFeeBusiDao extends BaseEntityDao<CFeeBusi> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4712258675702390565L;

	/**
	 * default empty constructor
	 */
	public CFeeBusiDao() {}
}
