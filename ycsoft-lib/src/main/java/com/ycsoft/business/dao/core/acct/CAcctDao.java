/**
 * CAcctDao.java	2010/06/18
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;



/**
 * CAcctDao -> C_ACCT table's operator
 */
@Component
public class CAcctDao extends BaseEntityDao<CAcct> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5335532665551661638L;

	/**
	 * default empty constructor
	 */
	public CAcctDao() {}

	/**
	 * 查询客户的所有账户信息
	 * @param custId
	 * @param countyId
	 * @return
	 */
	public List<CAcct> queryAcctByCustId(String custId, String countyId)
			throws JDBCException {
		String sql = "select * from c_acct where cust_id=? and county_id=? " +
				" and acct_id not in (select acct_id from j_cust_writeoff_acct where acctitem_id is null) order by acct_type,acct_id";
		return createQuery(sql, custId, countyId).list();
	}

	/**
	 * 查询客户的所有公用账户信息
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public CAcct findCustAcctByCustId(String custId, String countyId) throws JDBCException {
		String sql = "select * from c_acct where user_id is null and acct_type=? and cust_id=? and county_id=?";
		return createQuery(sql,SystemConstants.ACCT_TYPE_PUBLIC, custId, countyId).first();
	}
	
	public CAcct queryUserAcct(String custId, String userId, String countyId) throws Exception {
		String sql = "select * from c_acct where cust_id=? and user_id=? and county_id=?";
		return this.createQuery(sql, custId, userId, countyId).first();
	}

	public void updateByAcctItemId(Integer doneCode, String acctId,
			String acctItemId, String newAcctId, String newCustId) throws Exception {
		String sql =  "  update c_user u set u.acct_id=(select a.acct_id from c_acct a "+
		         " where u.cust_id=a.cust_id and u.user_id=a.user_id and a.acct_id=?) "+
		         " where exists(select 1 from  c_acct a "+
		         "       where u.cust_id=a.cust_id and u.user_id=a.user_id and a.acct_id=?)";
		this.executeUpdate(sql, newAcctId,newAcctId);
		
		sql = "insert into c_acct_acctitem_active_his" +
				" select ?,acct_id,acctitem_id,fee_type,balance,area_id,county_id" +
				" from c_acct_acctitem_active where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, doneCode, acctId, acctItemId);
		
		//新账户(公用)是否存在
		sql = "select * from c_acct_acctitem_active t where t.acct_id=? and t.acctitem_id=?";
		List<CAcctAcctitemActive> activeList = this.createQuery(CAcctAcctitemActive.class, sql, newAcctId, acctItemId).list();
		if(activeList.size()==0){
			sql = "update c_acct_acctitem_active set acct_id=?" +
				" where acct_id=? and acctitem_id=? and balance>0";
			this.executeUpdate(sql, newAcctId, acctId, acctItemId);
		}else{
//			String activeUpdateSql = "update  ("
//				+ " select /*+ BYPASS_UJVC */ a1.balance,a2.balance balance2" +
//						" from c_acct_acctitem_active a1,c_acct_acctitem_active a2"
//	            + " where a1.acctitem_id=a2.acctitem_id and a1.fee_type=a2.fee_type"
//	            + " and a1.county_id=a2.county_id"
//	            + " and a1.acct_id=? and a1.acctitem_id=?"
//	            + " and a1.fee_type=? and a1.county_id=? and a2.acct_id=?"
//	            + " ) t set t.balance2=t.balance2+t.balance";
		
			String activeUpdateSql="update  c_acct_acctitem_active a2 set a2.balance= "+
										"(select a1.balance+a2.balance from   c_acct_acctitem_active a1 "+
										" where  a1.acctitem_id=a2.acctitem_id and a1.fee_type=a2.fee_type "+
										"  and a1.county_id=a2.county_id and a1.acct_id=? and a1.acctitem_id=? and a1.fee_type=? ) "+
								   " where exists( select 1 from   c_acct_acctitem_active a1 " +
								   				"  where  a1.acctitem_id=a2.acctitem_id and a1.fee_type=a2.fee_type "+
								   						" and a1.county_id=a2.county_id and a1.acct_id=? and a1.acctitem_id=?  and a1.fee_type=? ) "+
								   " and  a2.county_id=?  and a2.acct_id=? ";
			
			String insertSql = "insert into c_acct_acctitem_active" +
					" select ?,acctitem_id,?,balance,area_id,county_id" +
					" from c_acct_acctitem_active where acct_id=? and acctitem_id=? and fee_type=? and county_id=?";
			
			sql = "select * from c_acct_acctitem_active t where t.acct_id=? and t.acctitem_id=?";
			List<CAcctAcctitemActive> oldActiveList = this.createQuery(CAcctAcctitemActive.class, sql, acctId, acctItemId).list();
			for(CAcctAcctitemActive oldActive : oldActiveList){
				boolean flag = true;
				for(CAcctAcctitemActive newActive : activeList){
					//相同资金类型，累加余额
					if(newActive.getAcctitem_id().equals(oldActive.getAcctitem_id())
							&& newActive.getFee_type().equals(oldActive.getFee_type())){
						flag = false;
						break;
					}
				}
				if(flag){
					//资金类型不同，新增活动余额记录
					if(oldActive.getBalance() > 0)
						this.executeUpdate(insertSql, newAcctId, oldActive.getFee_type(),
								oldActive.getAcct_id(), oldActive.getAcctitem_id(),
								oldActive.getFee_type(), oldActive.getCounty_id());
				}else{
					if(oldActive.getBalance() > 0)
						this.executeUpdate(activeUpdateSql, oldActive.getAcct_id(), oldActive.getAcctitem_id(), 
								oldActive.getFee_type(),oldActive.getAcct_id(), oldActive.getAcctitem_id(), 
								oldActive.getFee_type(), oldActive.getCounty_id(), newAcctId);
				}
			}
			
			
		}
		
		//新旧账目，将旧账目钱累加到新账目上
		sql = "select * from c_acct_acctitem where acct_id=? and acctitem_id=?";
		CAcctAcctitem oldAcctItem = this.createQuery(CAcctAcctitem.class, sql, acctId, acctItemId).first();
		CAcctAcctitem newAcctItem = this.createQuery(CAcctAcctitem.class, sql, newAcctId, acctItemId).first();
		if(oldAcctItem != null && newAcctItem != null){
			sql = "update c_acct_acctitem set ACTIVE_BALANCE=?,OWE_FEE=?,REAL_FEE=?,REAL_BILL=?," +
					" ORDER_BALANCE=?,REAL_BALANCE=?,CAN_TRANS_BALANCE=?,CAN_REFUND_BALANCE=?,INACTIVE_BALANCE=?" +
					" where acct_id=? and acctitem_id=?";
			this.executeUpdate(sql, 
					newAcctItem.getActive_balance()+oldAcctItem.getActive_balance(),
					newAcctItem.getOwe_fee() + oldAcctItem.getOwe_fee(),
					newAcctItem.getReal_fee() + oldAcctItem.getReal_fee(),
					newAcctItem.getReal_bill() + oldAcctItem.getReal_bill(),
					newAcctItem.getOrder_balance() + oldAcctItem.getOrder_balance(),
					newAcctItem.getReal_balance() + oldAcctItem.getReal_balance(),
					newAcctItem.getCan_trans_balance() + oldAcctItem.getCan_trans_balance(),
					newAcctItem.getCan_refund_balance() + oldAcctItem.getCan_refund_balance(),
					newAcctItem.getInactive_balance() + oldAcctItem.getInactive_balance(),
					newAcctItem.getAcct_id(), newAcctItem.getAcctitem_id()
			);
		}
		
		//删除居民活动余额
		sql = "delete from c_acct_acctitem_active where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, acctId, acctItemId);
		
		sql = "update c_acct_acctitem_threshold set acct_id=?" +
			" where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, newAcctId, acctId, acctItemId);
		
		sql = "update c_acct_acctitem_inactive set acct_id=?,cust_id=?" +
			" where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, newAcctId, newCustId, acctId, acctItemId);
		
		sql = "update c_acct_acctitem_threshold_prop set acct_id=?" +
			" where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, newAcctId, acctId, acctItemId);
		
		sql = "update c_acct_acctitem_adjust set acct_id=?" +
			" where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, newAcctId, acctId, acctItemId);
		
		sql = "update c_acct_acctitem_invalid set acct_id=?" +
			" where acct_id=? and acctitem_id=?";
		this.executeUpdate(sql, newAcctId, acctId, acctItemId);
		
		sql = "update c_acct_acctitem_order set acct_id=?" +
			" where acct_id=? or src_acct_id=?";
		this.executeUpdate(sql, newAcctId, acctId, acctId);
		
		sql = "update c_acct_acctitem_trans set in_acct_id=?" +
			" where in_acct_id=? and in_acctitem_id=?";
		this.executeUpdate(sql, newAcctId, acctId, acctItemId);
		
	}

	public String queryWhetherCustOwnfee(String custId) throws Exception{
		String sql = "select count(1) flag from c_acct ca,c_acct_acctitem caa " +
				" where ca.cust_id= ?  " + // and ca.county_id='0101'
				" and ca.acct_id=caa.acct_id and ca.county_id=caa.county_id " +
				"and (caa.active_balance+caa.order_balance-caa.owe_fee-caa.real_bill)<0";
		return findUnique(sql, custId);
	}
	
	public String queryWhetherUserOwnfee(String userId) throws Exception{
		String sql = "select count(1) flag from c_acct ca,c_acct_acctitem caa " +
				" where ca.user_id= ?  " + 
				" and ca.acct_id=caa.acct_id and ca.county_id=caa.county_id " +
				"and (caa.active_balance+caa.order_balance-caa.owe_fee-caa.real_bill)<0";
		return findUnique(sql, userId);
	}

}
