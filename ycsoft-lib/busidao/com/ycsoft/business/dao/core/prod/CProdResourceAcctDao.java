/**
 * CProdResourceAcctDao.java	2010/07/02
 */

package com.ycsoft.business.dao.core.prod;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdResourceAcct;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CProdResourceAcctDao -> C_PROD_RESOURCE_ACCT table's operator
 */
@Component
public class CProdResourceAcctDao extends BaseEntityDao<CProdResourceAcct> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1350669701129064263L;

	/**
	 * default empty constructor
	 */
	public CProdResourceAcctDao() {}

	public void removeByProdSn(String prodSn) throws Exception{
		String sql ="delete C_PROD_RESOURCE_ACCT where prod_sn=?";
		executeUpdate(sql, prodSn);
	}


}
