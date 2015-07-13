/**
 * CAcctAcctitemHisDao.java	2011/03/17
 */
 
package com.ycsoft.business.dao.core.acct; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemHis;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CAcctAcctitemHisDao -> C_ACCT_ACCTITEM_HIS table's operator
 */
@Component
public class CAcctAcctitemHisDao extends BaseEntityDao<CAcctAcctitemHis> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3892423576369153466L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemHisDao() {}

	public List<CAcctAcctitemHis> queryAcctItemHis(String acctId) throws Exception{
		String sql = "select t.* from busi.C_ACCT_ACCTITEM_HIS t where t.acct_id = ? ";
		return createQuery(sql, acctId).list();
	}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete from C_ACCT_ACCTITEM_HIS where acct_id = ?";
		executeUpdate(sql, acctId);
	}

}
