/**
 * CAcctHisDao.java	2010/07/12
 */

package com.ycsoft.business.dao.core.acct;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CAcctHisDao -> C_ACCT_HIS table's operator
 */
@Component
public class CAcctHisDao extends BaseEntityDao<CAcctHis> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1376946324715952632L;

	/**
	 * default empty constructor
	 */
	public CAcctHisDao() {}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete from C_ACCT_HIS where acct_id = ? ";
		executeUpdate(sql, acctId);
	}

}
