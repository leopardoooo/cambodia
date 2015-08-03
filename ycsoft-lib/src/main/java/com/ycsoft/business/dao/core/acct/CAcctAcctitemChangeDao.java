/**
 * CAcctItemChangeDao.java	2010/07/12
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.business.dto.core.acct.AcctAcctitemChangeDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.core.Query;


/**
 * CAcctItemChangeDao -> C_ACCT_ITEM_CHANGE table's operator
 */
@Component
public class CAcctAcctitemChangeDao extends BaseEntityDao<CAcctAcctitemChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3901452397774128427L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemChangeDao() {}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete c_acct_acctitem_change where acct_id=?";
		executeUpdate(sql, acctId);
	}

	/**
	 * 查询账目下异动明细
	 * @param acctitemId 账目id
	 * @param limit 
	 * @param start 
	 * @return
	 * @throws JDBCException
	 */
	public Pager<CAcctAcctitemChange> queryByAcctitemId(String acctId,String acctitemId, Integer start, Integer limit) throws JDBCException {
		String sql = "select t.*, (select t2.acctitem_name from vew_acctitem  t2,B_BILL_WRITEOFF t3,B_BILL t4 "
				+ "  where t.acct_id=t3.acct_id and t.acctitem_id=t3.acctitem_id and t.fee_type=t3.fee_type"
				+ "  and t.done_code=t3.writeoff_sn and t3.bill_sn=t4.bill_sn and t4.acctitem_id = t2.acctitem_id and t.change_type='XZ') acctitem_name, "
				+ " case when t.busi_code = ? AND T.CHANGE_FEE >0  "
				+ "  then (select t6.acctitem_name from c_acct_acctitem_change t5, vew_acctitem t6 where t.busi_code = ? "
				+ "  and t5.done_code = t.done_code and t5.acctitem_id <> t.acctitem_id and t6.acctitem_id = t5.acctitem_id "
				+ " and t.cust_id=t5.cust_id and t.fee_type=t5.fee_type and t.change_type=t5.change_type) "
				+ "  WHEN  t.busi_code = ? THEN t7.acctitem_name END start_acctitem,"
				+ " case when t.busi_code = ? AND T.CHANGE_FEE<=0  "
				+ "  then (select t6.acctitem_name from c_acct_acctitem_change t5, vew_acctitem t6 where t.busi_code = ? "
				+ "  and t5.done_code = t.done_code  and t5.acctitem_id <> t.acctitem_id and t6.acctitem_id = t5.acctitem_id "
				+ " and t.cust_id=t5.cust_id and t.fee_type=t5.fee_type and t.change_type=t5.change_type) "
				+ "  WHEN  t.busi_code = ? THEN t7.acctitem_name end end_acctitem"
				+ "  from c_acct_acctitem_change t,vew_acctitem t7 where t.acctitem_id = t7.acctitem_id and t.acct_id = ?  and t.acctitem_id = ? "
				+ " order by t.done_date desc";
		Query<CAcctAcctitemChange> query = createQuery(sql,BusiCodeConstants.ACCT_TRANS,BusiCodeConstants.ACCT_TRANS,BusiCodeConstants.ACCT_TRANS,
				BusiCodeConstants.ACCT_TRANS,BusiCodeConstants.ACCT_TRANS,BusiCodeConstants.ACCT_TRANS, acctId,acctitemId);
		return query.setStart(start).setLimit(limit).page();
	}

	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete C_ACCT_ACCTITEM_CHANGE where done_code=?";
		executeUpdate(sql, doneCode);

	}
	
	/**
	 * @param doneCode
	 */
	public void removeSpecChangeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete C_ACCT_ACCTITEM_CHANGE where done_code=? and busi_code=? and acctitem_id <> ?";
		executeUpdate(sql, doneCode, BusiCodeConstants.ACCT_PAY, SystemConstants.ACCTITEM_PUBLIC_ID);
	}

	public int querySumWriteOff(String acctId, String acctitemId, String feeType)  throws Exception{
		String sql = "select nvl(sum(change_fee),0)*-1 from c_acct_acctitem_change " +
				" where acct_id=? " +
				" and acctitem_id=? " +
				" and fee_type =? " +
				" and busi_code=? ";
		return Integer.parseInt(findUnique(sql, acctId,acctitemId,feeType,BusiCodeConstants.ACCT_WRITEOFF));
	}

	public void removeByAcctItemId(String acctId, String acctItemId)throws JDBCException {
		String sql = "delete C_ACCT_ACCTITEM_CHANGE where acct_id=? and acctitem_id=? and change_type <> ?";
		executeUpdate(sql, acctId,acctItemId, SystemConstants.ACCT_CHANGE_INVALID);
		
	}
	
	/**
	 * 查找模转数转到公用账目,且没在订购产品时转回数字产品账目的记录
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<AcctAcctitemChangeDto> queryAtvToDtvAcctitemChange(String custId,String countyId) throws JDBCException {
		String sql = StringHelper.append(
				"select a.*,d.user_id from c_acct_acctitem_change a,c_done_code c,c_done_code_detail d",
				" where a.done_code=c.done_code and c.done_code=d.done_code",
				" and a.change_fee>0 and a.county_id=? and c.county_id=? and d.county_id=?",
				" and change_type=? and c.busi_code=? and d.cust_id=?"
			);
		return this.createQuery(AcctAcctitemChangeDto.class, sql, countyId,
				countyId, countyId, SystemConstants.ACCT_CHANGE_TRANS,
				BusiCodeConstants.USER_ATOD, custId).list();
	}
	/**
	 * 查询订购回退的作废记录
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public AcctAcctitemChangeDto queryOrderZFAcctitemChange(String userId, String custId,
			String countyId) throws JDBCException {
		String sql = "select a.* from c_acct_acctitem_change a,c_done_code c,c_done_code_detail d"+
				" where a.done_code=c.done_code and c.done_code=d.done_code"+
				" and a.change_type=? and a.change_fee<0"+
				" and a.county_id=? and c.county_id=? and d.county_id=?"+
				" and d.user_id=? and d.cust_id=? and c.busi_code=? order by a.done_date desc";
		return this.createQuery(AcctAcctitemChangeDto.class, sql, SystemConstants.ACCT_CHANGE_INVALID,
				countyId, countyId, countyId, userId, custId, BusiCodeConstants.PROD_PACKAGE_ORDER).first();
	}
	
	/**
	 * 查询账目除作废以外的账目
	 * @param acctId
	 * @param acctitemId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctAcctitemChange queryActiveAcctitemChange(String acctId,String acctitemId) throws JDBCException {
		String sql = StringHelper.append(
				"select * from c_acct_acctitem_change a",
				" where a.acct_id=? and a.acctitem_id=? and a.change_type <> ? order by a.done_date desc"
			);
		return this.createQuery(CAcctAcctitemChange.class, sql, acctId,
				acctitemId, SystemConstants.ACCT_CHANGE_INVALID).first();
	}

	//非公用账目的账目异动
	public List<CAcctAcctitemChange> querybyDoneCode(Integer doneCode) throws JDBCException {
		String sql = " select * from c_acct_acctitem_change c where c.done_code=? and c.busi_code=? and c.acctitem_id <> ?";
		return createQuery(sql,doneCode, BusiCodeConstants.ACCT_PAY, SystemConstants.ACCTITEM_PUBLIC_ID).list();
	}
	
	public List<Object[]> queryUnRefundByOptr(String optrId,String countyId) throws JDBCException {
		String sql = "select  cd.cust_id , to_char(max(cd.done_code)) done_code "
				+ " from c_acct_acctitem_adjust ad,c_acct_acctitem_active ac,c_done_code c,c_done_code_detail cd"
				+ " where ad.acct_id=ac.acct_id and ad.acctitem_id=ac.acctitem_id and ad.done_code=c.done_code and c.done_code=cd.done_code"
				+ " and ad.fee_type=? and ad.county_id=? and ac.fee_type=? and ac.county_id=?"
				+ " and c.optr_id=? and c.county_id=? and cd.county_id=? and c.status=? "
				+ " and ac.balance>0 group by cust_id";
		return this.createSQLQuery(sql,  SystemConstants.ACCT_FEETYPE_ADJUST_KT,
				countyId, SystemConstants.ACCT_FEETYPE_ADJUST_KT, countyId,
				optrId, countyId, countyId,StatusConstants.ACTIVE).list();
	}
	 
	public String queryUnRefundMaxDoneCode(String custId,Integer doneCode) throws JDBCException {
		String sql = "select  decode(max(cdc.done_code),null,-1,max(cdc.done_code))  done_code from busi.c_done_code cdc  ,busi.c_done_code_detail cdcd , busi.c_acct_acctitem_adjust caaa "
			    +" where  cdc.done_code = cdcd.done_code   and cdc.done_code = caaa.done_code AND CDCD.DONE_CODE=CAAA.DONE_CODE  "
			    +" and cdc.county_id = caaa.county_id and cdc.county_id=cdcd.county_id  "
			    +" and cdcd.COUNTY_ID = caaa.COUNTY_ID  and caaa.fee_type=? and cdc.status=?  "
			    +" and cdcd.cust_id=?  and cdc.done_code>? " ;
		 
		return this.findUnique(sql,  SystemConstants.ACCT_FEETYPE_ADJUST_KT,StatusConstants.ACTIVE,custId,doneCode);
	}
	
	
	
}
