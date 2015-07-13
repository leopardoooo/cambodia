/**
 * CAcctAcctitemInactiveDao.java	2010/07/12
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * CAcctAcctitemInactiveDao -> C_ACCT_ACCTITEM_INACTIVE table's operator
 */
@Component
public class CAcctAcctitemInactiveDao extends
		BaseEntityDao<CAcctAcctitemInactive> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2635187230743739226L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemInactiveDao() {
	}



	public void removeByAcctId(String acctId) throws Exception {
		String sql = "delete c_acct_acctitem_inactive where acct_id=?";
		executeUpdate(sql, acctId);
	}

	public CAcctAcctitemInactive queryInactiveAcctitem(String acctId,
			String acctItemId, String counyId) throws Exception {
		String sql = "select * from c_acct_acctitem_inactive "
				+ " where acct_id=? and acctitem_id=? and county_id= ?";
		return createQuery(sql, acctId, acctItemId, counyId).first();
	}

	/**
	 * 查询账目下冻结资金记录
	 *
	 * @param acctitemId
	 *            账目id
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitemInactive> queryByAcctitemId(String acctId,
			String acctitemId) throws JDBCException {
		String sql = "select * from c_acct_acctitem_inactive t where t.acct_id=? and  t.acctitem_id=?";
		return createQuery(sql, acctId, acctitemId).list();
	}

	/**
	 * 根据feesn获取赠送记录
	 *
	 * @param feeSn
	 * @return
	 * @throws JDBCException
	 */
	public CAcctAcctitemInactive queryByFeeSn(String feeSn)
			throws JDBCException {
		String sql = "select * from c_acct_acctitem_inactive t where t.fee_sn=?";
		return createQuery(sql, feeSn).first();
	}
	
	public CAcctAcctitemInactive queryByPromotionSn(String promotionSn,String acctId,String acctitemId)
			throws JDBCException {
		String sql = "select 0 done_code,t.* from c_acct_acctitem_inactive t where t.promotion_sn=? and t.acct_id=? and  t.acctitem_id=?"
			+ " union all "
			+ " select t.* from c_acct_acctitem_inactive_his t where t.promotion_sn=? and t.acct_id=? and  t.acctitem_id=?";
		return createQuery(sql, promotionSn, acctId, acctitemId, promotionSn, acctId, acctitemId).first();
	}
	
	/**
	 * 根据feesn获取赠送记录
	 *
	 * @param feeSn
	 * @return
	 * @throws JDBCException
	 */
	public CAcctAcctitemInactive queryPromByFeeSn(String feeSn,String acctId, String acctItemId)
			throws JDBCException {
		String sql = "select * from c_acct_acctitem_inactive t where t.fee_sn=?  and acct_id=? and acctitem_id=? ";
		return createQuery(sql, feeSn,acctId,acctItemId).first();
	}

	/**
	 * @param fee_sn
	 * @param promotion_sn
	 */
	public void removeBySn(String feeSn, String promotionSn,String acctId, String acctItemId)
			throws JDBCException {
		feeSn = feeSn == null ? "" : feeSn;
		promotionSn = promotionSn == null ? "" : promotionSn;
		String sql = "delete c_acct_acctitem_inactive where (fee_sn =? or promotion_sn=?) and acct_id=? and acctitem_id=? ";
		executeUpdate(sql, feeSn, promotionSn,acctId,acctItemId);
	}

	/**
	 * @param promotionSn
	 * @return
	 */
	public List<CAcctAcctitemInactive> queryByPromSn(String promotionSn)
			throws JDBCException {
		String sql = "select * from c_acct_acctitem_inactive t where t.promotion_sn=?";
		return createQuery(sql, promotionSn).list();
	}
	
	public List<CAcctAcctitemInactive> queryByPromDoneCode(Integer promDonecode,String custId, boolean fromHistory)
			throws JDBCException {
		String sql = "select * from " + ( fromHistory? " c_acct_acctitem_inactive_his " : " c_acct_acctitem_inactive " ) 
				+ " t where t.cust_id = ? and " +
				( fromHistory? " t.create_done_code " : " t.done_code " ) + " = ? " ;
		return createQuery(sql, custId,promDonecode).list();
	}

	/**
	 * 更新冻结资金的余额
	 *
	 * @param sn
	 * @param fee
	 * @throws Exception
	 */
	public void updateBanlance(String sn, String acctId, String acctItemId,int fee) throws Exception {
		
		if(StringHelper.isEmpty(sn)){
			String sql = "update c_acct_acctitem_inactive set BALANCE = BALANCE - ? ,use_amount=use_amount + ? ," +
			" last_active_time=sysdate"
			+ " where (fee_sn is null  or promotion_sn is null ) and acct_id=? and acctitem_id=? ";
			executeUpdate(sql, fee, fee, acctId,acctItemId);
		}else{
			String sql = "update c_acct_acctitem_inactive set BALANCE = BALANCE - ? ,use_amount=use_amount + ? ," +
			" last_active_time=sysdate"
			+ " where (fee_sn=?  or promotion_sn=? ) and acct_id=? and acctitem_id=? ";
			executeUpdate(sql, fee, fee, sn, sn,acctId,acctItemId);
		}
	}

	/**
	 * 更新冻结资金的下次解冻日期
	 *
	 * @param sn
	 * @param unfreezeDate
	 * @throws Exception
	 */
	public void updateUnfeezeDate(String sn,String acctId,String acctItemId, int cycle) throws Exception {
		String sql = "update c_acct_acctitem_inactive "
				+ " set NEXT_ACTIVE_TIME=add_months(to_date(to_char(sysdate,'yyyy-mm-')||'01','yyyy-mm-dd'),?) ," 
				+ " last_active_time=sysdate "
				+ " where (fee_sn=?  or promotion_sn=? ) " 
				+ " and acct_id=? " 
				+ " and acctitem_id=?";
		executeUpdate(sql, cycle, sn, sn,acctId,acctItemId);
	}

	/**
	 *
	 */

	public CAcctAcctitemInactive queryNextUnfreezeRecord(String acctId,
			String acctItemId) throws Exception {
		String sql = "select * from c_acct_acctitem_inactive "
				+ " where use_amount=0 " + " and acct_id =? "
				+ " and acctitem_id=? " + " order by create_time";

		List<CAcctAcctitemInactive> l = this.createQuery(sql, acctId,
				acctItemId).list();
		if (l.size() > 0)
			return l.get(0);
		else
			return null;
	}



	/**
	 * 删除冻结资金记录，并记录历史
	 * @param acctId
	 * @param acctItemId
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void removeByAcctItemIdWithHis(String acctId, String acctItemId,
			Integer doneCode) throws JDBCException {
		String sql1 = "insert into c_acct_acctitem_inactive_his (" +
				" select ? donecode, c.PROMOTION_SN,c.FEE_SN,c.ACCT_ID,c.ACCTITEM_ID,c.INIT_AMOUNT," +
				" c.USE_AMOUNT,c.BALANCE,c.CYCLE,c.CREATE_TIME,c.ACTIVE_AMOUNT,c.LAST_ACTIVE_TIME," +
				" c.NEXT_ACTIVE_TIME,c.AREA_ID,c.COUNTY_ID,c.CUST_ID,c.DONE_CODE" +
				" from c_acct_acctitem_inactive c where c.acct_id=? and c.acctitem_id=?)";
		executeUpdate(sql1, doneCode, acctId, acctItemId);

		String sql = "delete c_acct_acctitem_inactive where acct_id=? and acctitem_id=?";
		executeUpdate(sql, acctId, acctItemId);
	}
	
	/**
	 * 删除冻结资金记录，并记录历史
	 * @param acctId
	 * @param acctItemId
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void removeByPromDoneCodeWithHis(Integer createDoneCode,
			Integer doneCode) throws JDBCException {
		String sql1 = "insert into c_acct_acctitem_inactive_his (" +
				" select ? donecode, c.PROMOTION_SN,c.FEE_SN,c.ACCT_ID,c.ACCTITEM_ID,c.INIT_AMOUNT," +
				" c.USE_AMOUNT,c.BALANCE,c.CYCLE,c.CREATE_TIME,c.ACTIVE_AMOUNT,c.LAST_ACTIVE_TIME," +
				" c.NEXT_ACTIVE_TIME,c.AREA_ID,c.COUNTY_ID,c.CUST_ID,c.DONE_CODE" +
				" from c_acct_acctitem_inactive c where c.done_code=? )";
		executeUpdate(sql1, doneCode, createDoneCode);

		String sql = "delete c_acct_acctitem_inactive where done_code=? ";
		executeUpdate(sql, createDoneCode);
	}
}
