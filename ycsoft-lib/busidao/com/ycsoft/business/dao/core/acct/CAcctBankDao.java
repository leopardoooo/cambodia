/**
 * CAcctBankDao.java	2010/07/12
 */

package com.ycsoft.business.dao.core.acct;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CAcctBankDao -> C_ACCT_BANK table's operator
 */
@Component
public class CAcctBankDao extends BaseEntityDao<CAcctBank> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7677632930660669012L;

	/**
	 * default empty constructor
	 */
	public CAcctBankDao() {}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete c_acct_bank where acct_id=? and bank_pay_type<>'9'";
		executeUpdate(sql, acctId);
	}
	

	public void removeByAcctIdPayType(String acctId, String bankPayType)
			throws Exception {
		String sql = "delete c_acct_bank where acct_id=? and bank_pay_type=?";
		executeUpdate(sql, acctId, bankPayType);
	}
	
	public CAcctBank querySignBankByAcctId(String acctId,String bankPayType) throws JDBCException{
		String sql="select * from c_acct_bank cab where cab.acct_id=? and cab.bank_pay_type=? order by cab.sign_time desc";
		return createQuery(sql,acctId,bankPayType).first();
	}
	public void updateByAcctId(String acctId,String bankPayType,String optionType,Date time) throws JDBCException{
		String rtime = DateHelper.format(time, DateHelper.FORMAT_YMD_STR);
		if ("1".equals(optionType)) {
			String sql = "update c_acct_bank  set unsign_time=null,sign_time=to_date(?,'yyyymmdd') , status='ACTIVE' where acct_id=? and bank_pay_type=?";
			executeUpdate(sql, rtime, acctId, bankPayType);
		} else if ("2".equals(optionType)) {
			String sql = "update c_acct_bank  set unsign_time=to_date(?,'yyyymmdd'),status='INVALID' where acct_id=? and bank_pay_type=?";
			executeUpdate(sql, rtime, acctId, bankPayType);
		}
		
	}

	/**
	 * 根据客户编号查找签约信息
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctBank findByCustId(String custId) throws JDBCException {
		String sql = "select a.* from c_acct_bank a,c_acct b where " +
			" a.acct_id=b.acct_id and b.cust_id=? and b.acct_type=? ";
		return createQuery(CAcctBank.class, sql, custId,SystemConstants.ACCT_TYPE_PUBLIC).first();
	}

	/**
	 * 更新签约状态
	 * @param custId
	 * @param status
	 * @throws JDBCException
	 */
	public void updateBank(String custId, String status) throws JDBCException{
		String sql = "UPDATE c_acct_bank t SET t.status=? WHERE t.cust_id = ? ";
		executeUpdate(sql, status, custId);
	}


}
