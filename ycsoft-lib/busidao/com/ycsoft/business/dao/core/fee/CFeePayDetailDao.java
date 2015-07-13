/**
 * CFeePayDetailDao.java	2010/04/08
 */

package com.ycsoft.business.dao.core.fee;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeePayDetail;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CFeePayDetailDao -> C_FEE_PAY_DETAIL table's operator
 */
@Component
public class CFeePayDetailDao extends BaseEntityDao<CFeePayDetail> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4358440219044674541L;

	/**
	 * default empty constructor
	 */
	public CFeePayDetailDao() {}

}
