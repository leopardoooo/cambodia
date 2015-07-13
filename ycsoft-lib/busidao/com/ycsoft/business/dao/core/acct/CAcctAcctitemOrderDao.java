/**
 * CAcctAcctitemOrderDao.java	2010/07/14
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemOrder;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CAcctAcctitemOrderDao -> C_ACCT_ACCTITEM_ORDER table's operator
 */
@Component
public class CAcctAcctitemOrderDao extends BaseEntityDao<CAcctAcctitemOrder> {

	/**
	 *
	 */
	private static final long serialVersionUID = -531002445985932560L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemOrderDao() {}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete c_acct_acctitem_order where acct_id=?";
		executeUpdate(sql, acctId);
	}

	/**
	 * 查询账目下冻结明细
	 * @param acctitemId 账目id
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitemOrder> queryByAcctitemId(String acctId,String acctitemId) throws JDBCException {
		String sql = "select * from c_acct_acctitem_order t where t.acct_id=? and t.acctitem_id=?";
		return createQuery(sql, acctId,acctitemId).list();
	}
	public List<CAcctAcctitemOrder> queryAllByAcctitemId(String acctId,String acctItemId) throws JDBCException {
		String sql = " select caao.* ,"+
					" (select acctitem_name from busi.vew_acctitem where acctitem_id=caao.acctitem_id)  acctitem_name ,"+
					" (select acctitem_name from busi.vew_acctitem where acctitem_id=caao.src_acctitem_id)  src_acctitem_name "+
					" from busi.c_acct_acctitem_order caao  "+
					" where (caao.src_acct_id=? or  caao.acct_id =?) and src_acctitem_id<>acctitem_id "+
					" and (caao.src_acctitem_id=? or caao.acctitem_id=?)";
		return createQuery(sql, acctId,acctId,acctItemId,acctItemId).list();
	}
}
