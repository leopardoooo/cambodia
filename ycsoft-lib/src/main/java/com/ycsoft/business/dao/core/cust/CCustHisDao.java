/**
 * CCustHisDao.java	2010/07/21
 */

package com.ycsoft.business.dao.core.cust;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CCustHisDao -> C_CUST_HIS table's operator
 */
@Component
public class CCustHisDao extends BaseEntityDao<CCustHis> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4206099763334377641L;

	/**
	 * default empty constructor
	 */
	public CCustHisDao() {}

}
