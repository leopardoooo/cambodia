/**
 * CAcctAcctitemThresholdPropDao.java	2011/08/29
 */
 
package com.ycsoft.business.dao.core.acct; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemThresholdProp;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CAcctAcctitemThresholdPropDao -> C_ACCT_ACCTITEM_THRESHOLD_PROP table's operator
 */
@Component
public class CAcctAcctitemThresholdPropDao extends BaseEntityDao<CAcctAcctitemThresholdProp> {
	
	/**
	 * default empty constructor
	 */
	public CAcctAcctitemThresholdPropDao() {}

	public List<CAcctAcctitemThresholdProp> queryAcctitemThresholdProp(
			String acctId, String acctItemId) throws Exception {
		String sql = "select * from c_acct_acctitem_threshold_prop t where t.acct_id=? and t.acctitem_id=?" +
				" order by t.change_time desc";
		return this.createQuery(sql, acctId, acctItemId).list();
	}
}
