/**
 * CAcctAcctitemAdjustDao.java	2010/07/19
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemAdjust;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CAcctAcctitemAdjustDao -> C_ACCT_ACCTITEM_ADJUST table's operator
 */
@Component
public class CAcctAcctitemAdjustDao extends BaseEntityDao<CAcctAcctitemAdjust> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6720733862755376147L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemAdjustDao() {}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<CAcctAcctitemAdjust> queryByDoneCode(Integer doneCode) throws Exception{
		String sql = "select * from C_ACCT_ACCTITEM_ADJUST where done_code=?";
		return this.createQuery(sql, doneCode).list();
	}

	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete C_ACCT_ACCTITEM_ADJUST where done_code=?";
		executeUpdate(sql, doneCode);

	}
	
	public void updateReason(Integer doneCode, String reason) throws Exception {
		String sql = "update C_ACCT_ACCTITEM_ADJUST set reason=? where done_code=?";
		this.executeUpdate(sql, reason, doneCode);
	}

	/**
	 * 返回账目调账信息
	 * @param acctId
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitemAdjust> queryByAcctitemId(String acctId,
			String acctitemId) throws JDBCException {
		String sql = "select * from c_acct_acctitem_adjust c where c.acct_id=? and c.acctitem_id=?";
		return createQuery(sql, acctId,acctitemId).list();
	}
	
	public List<CAcctAcctitemAdjust> queryAdjustFee(String acctId, String acctItemId,String feeType) throws Exception{
		String sql = "select * from c_acct_acctitem_adjust t " +
				"where t.acct_id = ? and t.acctitem_id = ?  and t.create_time >=TRUNC(SYSDATE, 'MM') " +
				"and t.create_time<=last_day(SYSDATE) and t.fee_type = ?  and t.ajust_fee<0 ";
		return createQuery(sql, acctId,acctItemId,feeType).list();
	}

}
