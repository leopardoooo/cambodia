/**
 * CAcctAcctitemActiveDao.java	2010/07/12
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CAcctAcctitemActiveDao -> C_ACCT_ACCTITEM_ACTIVE table's operator
 */
@Component
public class CAcctAcctitemActiveDao extends BaseEntityDao<CAcctAcctitemActive> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2003063980133372828L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemActiveDao() {
	}
	
	public int queryXJBalanceByCustId(String custId,String countyId) throws JDBCException {
		String sql = "select nvl(sum(a.balance),0) from c_acct_acctitem_active a"+
			" where a.acct_id in (select t.acct_id from c_acct t where t.cust_id=? and t.county_id=?)"+
			" and a.fee_type=? and a.county_id=?";
		int cashbalance = Integer.parseInt(findUnique(sql, custId, countyId,
				SystemConstants.ACCT_FEETYPE_CASH, countyId));
		
		sql = "select nvl(sum(a.balance),0) from c_acct_acctitem_active a"+
		" where a.acct_id in (select t.acct_id from c_acct t where t.cust_id=? and t.county_id=?)"+
		" and a.fee_type !=? and a.county_id=?";
		
		int otherBalance = Integer.parseInt(findUnique(sql, custId, countyId,
				SystemConstants.ACCT_FEETYPE_CASH, countyId));
		
		sql = "select nvl(sum(t.owe_fee+t.real_bill),0) from c_acct_acctitem t"+
			" where t.acct_id in (select c.acct_id from c_acct c where c.cust_id = ?)";
		int amount = Integer.parseInt(findUnique(sql, custId));
		
		
		int result = 0;
		
		if(otherBalance-amount >= 0){//非现金余额大于费用
			result = cashbalance;
		}else if(cashbalance+otherBalance- amount>=0){//总余额大于等于费用
			result = cashbalance+otherBalance- amount;
		}
		return result;
	}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete c_acct_acctitem_active where acct_id=?";
		executeUpdate(sql, acctId);
	}
	/**
	 * 查询账目项资金明细
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemActive queryAcctItemActive(String acctId,
			String acctItemId, String feeType, String countyId)
			throws Exception {
		String sql = "select * from c_acct_acctitem_active "
				+ " where acct_id=? and acctitem_id=? "
				+ " and fee_type=? and county_id=? ";
		return  createQuery(sql, acctId,
				acctItemId, feeType, countyId).first();
	}

	/**
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @param fee
	 * @param countyId
	 * @throws Exception
	 */
	public void updateBanlance(String acctId, String acctItemId,
			String feeType, int fee, String countyId) throws Exception {
		String sql = "update c_acct_acctitem_active set balance = balance  + ? "
				+ " where acct_id=? "
				+ " and acctitem_id=? "
				+ " and fee_type=? " + " and county_id=? ";

		executeUpdate(sql, fee, acctId, acctItemId, feeType, countyId);
	}

	/**
	 * 查询账目下余额明细
	 * @param acctitemId 账目id
	 * @return
	 * @throws JDBCException
	 */
	public List<AcctAcctitemActiveDto> queryByAcctitemId(String acctId,String acctitemId,String countyId)
			throws JDBCException {
		String sql = "select c.*,t.can_trans,t.can_refund,t.is_cash,t.priority from c_acct_acctitem_active c,t_acct_fee_type t" +
				" where c.fee_type=t.fee_type" +
				" and c.acct_id=? and c.acctitem_id=? " +
				" and c.county_id=? order by t.priority";
		return createQuery(AcctAcctitemActiveDto.class,sql, acctId,acctitemId,countyId).list();
	}

	/**
	 *
	 * @param acctId
	 * @param acctItemId
	 * @param feeType
	 * @return
	 * @throws Exception
	 */
	public CAcctAcctitemActive queryActiveByFeetype(String acctId,String acctItemId,String feeType,String countyId) throws Exception{
		String sql ="select * from c_acct_acctitem_active " +
				" where acct_id=?  and acctitem_id =?  and fee_type =? and county_id=?";
		return createQuery(sql,acctId,acctItemId,feeType,countyId).first();
	}

	public int querySumFeetype(String acctId,String acctItemId,String feeType,String countyId) throws Exception{
		String sql ="select nvl(sum(balance),0) from c_acct_acctitem_active " +
				" where acct_id=?  and acctitem_id =?  and fee_type =? and county_id=?";
		return Integer.parseInt(findUnique(sql,acctId,acctItemId,feeType,countyId));
	}


	/**
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public List<CAcctAcctitemActive> queryMinusByCustId(String custId,String countyId) throws JDBCException {
		String sql ="select * from c_acct_acctitem_active " +
				" where acct_id in (select acct_id from c_acct where cust_id=?) " +
				" and county_id=? and balance<0";
		return createQuery(sql,custId,countyId).list();
	}

	public void removeByAcctItemId(String acctId, String acctItemId)throws JDBCException {
		String sql = "delete c_acct_acctitem_active where acct_id=? and acctitem_id=?";
		executeUpdate(sql, acctId,acctItemId);
		
	}


}
