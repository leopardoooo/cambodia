package com.ycsoft.business.dao.core.cust;

/**
 * CCustAddrDao.java	2010/10/13
 */


import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustAddr;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CCustAddrDao -> C_CUST_ADDR table's operator
 */
@Component
public class CCustAddrDao extends BaseEntityDao<CCustAddr> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7590084152908325875L;

	/**
	 * default empty constructor
	 */
	public CCustAddrDao() {}

}
