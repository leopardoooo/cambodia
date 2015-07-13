/**
 * CAcctAcctitemDao.java	2010/06/18
 */

package com.ycsoft.business.dao.core.acct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.UnitPayDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CAcctAcctitemDao -> C_ACCT_ACCTITEM table's operator
 */
@Component
public class CAcctAcctitemDao extends BaseEntityDao<CAcctAcctitem> {
	
	private String acctItemTbale="(select a.acct_id,"+
		    "   a.acctitem_id,"+
		    "   a.active_balance,"+
		    "   a.owe_fee,"+
		    "   a.real_fee,"+
		    "   a.real_bill,"+
		    "   a.order_balance,"+
		    "   a.real_balance,"+
		    "   sum(decode(b.can_trans,'T',b.balance,0)) can_trans_balance,"+
		    "   sum(decode(b.can_refund,'T',b.balance,0)) can_refund_balance," +
		    "   (select sum(cin.balance) from c_acct_acctitem_inactive cin where cin.acct_id=a.acct_id " +
		    "	and cin.acctitem_id=a.acctitem_id and cin.county_id=a.county_id)inactive_balance," +
		    "   a.open_time,"+
		    "   a.area_id,"+
		    "   a.county_id"+
		 " from c_acct_acctitem a "+
		 " , (select t1.*, t2.can_trans, t2.can_refund "+
		 "  from c_acct_acctitem_active t1, t_acct_fee_type t2 "+
		 " where t1.fee_type = t2.fee_type) b " +
		 " where a.acct_id=b.acct_id(+)"+
		 " and a.acctitem_id=b.acctitem_id(+)"+
		 " and a.county_id=b.county_id(+)"+
//		 " and (a.acct_id,a.acctitem_id) not in (select acct_id,acctitem_id from j_cust_writeoff_acct) " +
//		 " and a.acct_id not in (select acct_id from j_cust_writeoff_acct where acctitem_id is null)"+
		 " ? "+
		 "  group by a.acct_id,"+
		 "     a.acctitem_id,"+
		 "      a.active_balance,"+
		 "      a.owe_fee,"+
		 "      a.real_fee,"+
		 "      a.real_bill,"+
		 "      a.order_balance,"+
		 "      a.real_balance,"+
		 "      a.inactive_balance,"+
		 "      a.open_time,"+
		 "      a.area_id,"+
		 "      a.county_id)";
	
	private String getAcctItemTableByCust (String custId,String countyId){
		
		return this.acctItemTbale.replace("?", " and a.acct_id in (select acct_id from c_acct where cust_id='"+custId+"' and county_id='"+countyId+"')");
	}
	
	private String getAcctItemTableByAcct (String acctId){
		return this.acctItemTbale.replace("?", " and a.acct_id ='"+acctId+"'");
	}
	
	private String getAcctItemTableByUser (String userId){
		return this.acctItemTbale.replace("?", " and a.acct_id =(select acct_id from c_user where user_id='"+userId+"' )");
	}
	
	private String getAcctItemTableByAcctItem (String acctId,String acctItemId){
		return this.acctItemTbale.replace("?", " and a.acct_id = '"+acctId+"' and a.acctitem_id='"+acctItemId+"' ");
	}
	/**
	 *
	 */
	private static final long serialVersionUID = -8385930701069444921L;

	/**
	 * default empty constructor
	 */
	public CAcctAcctitemDao() {}

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete c_acct_acctitem where acct_id=?";
		executeUpdate(sql, acctId);
	}

	/**
	 * @param acctId
	 * @param acctItemId
	 */
	public void removeByAcctItemId(String acctId, String acctItemId) throws Exception{
		String sql = "delete c_acct_acctitem where acct_id=? and ACCTITEM_ID=?";
		executeUpdate(sql, acctId,acctItemId);

	}
	/**
	 * 保存账目余额变化
	 * @param acctId
	 * @param acctItemId
	 * @param fee
	 * @param refundFee
	 * @param transFee
	 * @param countyId
	 * @throws Exception
	 */
	public void updateActiveBanlance(String acctId,String acctItemId,int fee,int refundFee,int transFee,String countyId) throws Exception{
		String sql = "update c_acct_acctitem set REAL_BALANCE=REAL_BALANCE+?,ACTIVE_BALANCE = ACTIVE_BALANCE + ? ," +
				" CAN_REFUND_BALANCE=CAN_REFUND_BALANCE+?,CAN_TRANS_BALANCE=CAN_TRANS_BALANCE+? " +
				" where acct_id=? " +
				" and acctitem_id=? " +
				" and county_id=? " ;

		executeUpdate(sql, fee, fee, refundFee, transFee, acctId, acctItemId, countyId);
	}
	
	/**
	 * 保存账目冻结余额变化
	 * @param acctId
	 * @param acctItemId
	 * @param fee
	 * @param refundFee
	 * @param transFee
	 * @param countyId
	 * @throws Exception
	 */
	public void updateInActiveBanlance(String acctId,String acctItemId,String countyId,Integer num) throws Exception{
		String sql = "update c_acct_acctitem set inactive_balance = ?" +
				" where acct_id=? and acctitem_id=? and county_id=? " ;
		executeUpdate(sql, num, acctId, acctItemId, countyId);
	}
	/**
	 * 更新实时费用
	 * @param acctId
	 * @param acctItemId
	 * @param fee
	 * @param refundFee
	 * @param transFee
	 * @param countyId
	 * @throws Exception
	 */
	public void updateRealFee(String acctId,String acctItemId,int fee) throws Exception{
		String sql = "update c_acct_acctitem set real_fee=real_fee+?,real_bill = real_bill + ? " +
				" where acct_id=? " +
				" and acctitem_id=? "  ;

		executeUpdate(sql, fee,fee, acctId, acctItemId);
	}


	/**
	 * 查询账户的所有账目信息
	 * @param acctId
	 * @return
	 */
	public List<CAcctAcctitem> queryByAcctId(String acctId) throws JDBCException {
		String sql = "select c.* " +
				" from "+this.getAcctItemTableByAcct(acctId)+" c" +
				" where  c.acct_id=?";
		return createQuery(sql, acctId).list();
	}
	
	/**
	 * 获取账目信息
	 * @param acctId
	 * @param acctItemId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctAcctitem queryByAcctItemId(String acctId,String acctItemId) throws JDBCException {
		String sql = "select acct_id, acctitem_id, active_balance, owe_fee, real_fee," +
				" real_bill, order_balance,can_refund_balance can_refund_balance_norule,can_trans_balance can_trans_atod, decode(p.trans,'F',0,can_trans_balance) can_trans_balance," +
				" decode(p.refund,'F',0,can_refund_balance)  can_refund_balance, inactive_balance," +
				" open_time, c.area_id, c.county_id " +
				" from "+this.getAcctItemTableByAcctItem(acctId, acctItemId)+" c,p_prod p " +
				" where c.acct_id=?  " +
				" and c.acctitem_id=? " +
				" and c.acctitem_id=p.prod_id(+)";
		return createQuery(sql, acctId,acctItemId).first();
	}


	/**
	 * 查询客户的所有账目信息
	 * @param custId
	 * @return
	 */
	public List <AcctitemDto> queryByCustId(String custId,String countyId) throws JDBCException {
		String sql = "select c.*,t.acctitem_name,t.acctitem_type ,a.acct_type,p.serv_id"
				+ " from "+this.getAcctItemTableByCust(custId, countyId)+" c,t_public_acctitem t ,C_ACCT a,p_prod p"
				+ " where c.acctitem_id = t.acctitem_id(+) and C.ACCT_ID=a.acct_id "
				+ " and a.cust_id=? AND a.county_id=? AND c.county_id=?  and c.acctitem_id=p.prod_id(+) " 
				+ " and (c.acct_id,c.acctitem_id) not in (select acct_id,acctitem_id from j_cust_writeoff_acct) order by p.is_base desc";
		return createQuery(AcctitemDto.class, sql, custId,countyId,countyId).list();
	}
	
	/**
	 * 查询用户的所有账目信息
	 * @param custId
	 * @return
	 */
	public List <AcctitemDto> queryByUserId(String userId,String countyId) throws JDBCException {
		String sql = "select c.*,t.acctitem_name,t.acctitem_type ,a.acct_type"
				+ " from "+this.getAcctItemTableByUser(userId)+" c,t_public_acctitem t ,C_ACCT a,p_prod p"
				+ " where c.acctitem_id = t.acctitem_id(+) and C.ACCT_ID=a.acct_id "
				+ " and a.user_id=? AND a.county_id=? AND c.county_id=?  and c.acctitem_id=p.prod_id(+) " 
				+ " and (c.acct_id,c.acctitem_id) not in (select acct_id,acctitem_id from j_cust_writeoff_acct) order by p.is_base desc";
		
		return createQuery(AcctitemDto.class, sql, userId,countyId,countyId).list();
	}

	/**
	 * 保存账目余额变化
	 * @param acctId
	 * @param acctItemId
	 * @param fee
	 * @param inactiveFee
	 * @param refundFee
	 * @param transFee
	 * @param countyId
	 * @throws Exception
	 */
	public void updateActiveBanlance(String acctId,String acctItemId,int fee,int inactiveFee,int refundFee,int transFee,String countyId) throws Exception{
		String sql = "update c_acct_acctitem set REAL_BALANCE=REAL_BALANCE+?,ACTIVE_BALANCE = ACTIVE_BALANCE + ? ,INACTIVE_BALANCE = INACTIVE_BALANCE+?," +
				" CAN_REFUND_BALANCE=CAN_REFUND_BALANCE+?,CAN_TRANS_BALANCE=CAN_TRANS_BALANCE+? " +
				" where acct_id=? " +
				" and acctitem_id=? " +
				" and county_id=? " ;

		executeUpdate(sql, fee, fee,inactiveFee, refundFee, transFee, acctId, acctItemId, countyId);
	}
	/**
	 * 修改账目欠费金额并清空实时费用
	 * @param clearRealFee 
	 * @param acctId
	 * @param acctItemId
	 * @param oweFee
	 * @param countyId 
	 * @throws JDBCException 
	 */
	public void changeOwefee(boolean clearRealFee,String acctId, String acctItemId, int oweFee,String countyId) throws JDBCException{
		String sql = "update c_acct_acctitem set  owe_fee=owe_fee+? " ;
		if (clearRealFee)
			sql += " , real_fee=0,real_bill=0";
		sql += " where acct_id=? " ;
		sql += " and acctitem_id=? " ;
		sql += " and county_id=? " ;
		executeUpdate(sql, oweFee, acctId, acctItemId, countyId);
	}


	public CAcctAcctitem getOweFee(String custId,String acctitemId) throws JDBCException {
		String sql ="SELECT nvl(sum(owe_fee),0) owe_fee FROM c_acct_acctitem a, c_acct b where a.acct_id = b.acct_id and b.cust_id = ?  and a.acctitem_id =? ";
		return createQuery(sql,custId,acctitemId).first();
	}

	
	/**
	 * 查询基本包的账户信息
	 * @param custIds
	 * @return
	 * @throws JDBCException
	 */
	public List<UnitPayDto> queryBaseProdAcctItems(String[] custIds,String prodId)
			throws JDBCException {
		List<UnitPayDto> upList = new ArrayList<UnitPayDto>();
		String sql = "";
		if(custIds.length>0){
		sql = StringHelper.append("SELECT c.cust_id,c.cust_name,cu.card_id,P.PROD_NAME,pt.tariff_name,"
				," pt2.tariff_name next_tariff_name,cp.invalid_date,ai.active_balance,ai.owe_fee,ai.real_bill,"
				," pt.rent tariff_rent,ai.acct_id,ai.acctitem_id,a.user_id,cp.prod_sn,cp.prod_id,"
				," cp.tariff_id, cp.next_tariff_id"
				," FROM c_acct_acctitem ai, c_acct a,c_prod cp,p_prod p,"
				," c_cust  c,p_prod_tariff pt,p_prod_tariff   pt2,c_user cu"
				," WHERE a.acct_id = ai.acct_id and cp.prod_id = p.prod_id and p.prod_id = '"+prodId+"'"
				," and c.cust_id = a.cust_id and a.cust_id = cp.cust_id and a.user_id = cp.user_id "
				," and cu.user_id = a.user_id and cp.status != 'REQSTOP'"
				," AND p.prod_id = ai.acctitem_id  and pt.tariff_id = cp.tariff_id "
				," and pt2.tariff_id(+) = cp.next_tariff_id  and("+getSqlGenerator().setWhereInArray("a.cust_id",custIds)+")  ");
		}
		upList.addAll(this.createQuery(UnitPayDto.class, sql).list());
		return upList;
	}

	public List<String> queryCanAdjust(String dataRight) throws JDBCException {
		String sql ="select acctitem_id from vew_acctitem where "+dataRight;
		return this.findUniques(sql);
	}
	
	public String queryPrintItemId(String acctItemId) throws JDBCException {
		String sql = "select printitem_id from vew_acctitem where acctitem_id=?";
		return this.findUnique(sql, acctItemId);
	}

	public List<CProd> querySelectableProds(String[] custIds) throws Exception {
		List<CProd> prodList = new ArrayList<CProd>();
		String sql = "";
		if (custIds.length > 0) {
			sql = StringHelper.append(
							" SELECT distinct p.PROD_NAME, p.prod_id  FROM c_prod cp, p_prod p, c_cust c",
							" WHERE c.cust_id = cp.cust_id and p.prod_id = cp.prod_id and cp.status != 'REQSTOP' and p.status = 'ACTIVE' and("
									+ getSqlGenerator().setWhereInArray("c.cust_id", custIds) + ") ");
		}
		prodList.addAll(this.createQuery(CProd.class, sql).list());
		return prodList;
	}

	/**
	 * 查询用户的所有账目，和客户的公用账目
	 * @param custId
	 * @param userId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<AcctitemDto> queryAcctAndAcctItemByUserId(String custId,
			String userId, String countyId) throws JDBCException {
		String sql = "select c.*,t.acctitem_name,t.acctitem_type ,a.acct_type"
				+ " from "+this.getAcctItemTableByCust(custId, countyId)+" c,t_public_acctitem t ,C_ACCT a"
				+ " where c.acctitem_id = t.acctitem_id(+) and C.ACCT_ID=a.acct_id "
				+ " and a.user_id=? AND a.county_id=? AND c.county_id=?  "
				+ " and (c.acct_id,c.acctitem_id) not in (select acct_id,acctitem_id from j_cust_writeoff_acct) "
				+ " union all select c.*,t.acctitem_name,t.acctitem_type ,a.acct_type"
				+ " from c_acct_acctitem c,t_public_acctitem t ,C_ACCT a"
				+ " where c.acctitem_id = t.acctitem_id(+) and C.ACCT_ID=a.acct_id "
				+ " and a.cust_id=? AND a.acct_type=? AND a.county_id=? AND c.county_id=?  "
				+ " and (c.acct_id,c.acctitem_id) not in (select acct_id,acctitem_id from j_cust_writeoff_acct) ";
		return createQuery(AcctitemDto.class, sql, userId, countyId, countyId,
				custId, SystemConstants.ACCT_TYPE_PUBLIC, countyId, countyId)
				.list();
	}
	
	/**
	 * 更新所有账目的欠费金额，每月出账后调用
	 * @throws Exception
	 */
//	public void updateOweFeeByBill() throws Exception {
//		String sql ="update (select /*+bypass_ujvc*/ "+
//				    "     a.acct_id, a.acctitem_id, a.owe_fee, b.owe_fee new_owe_fee "+
//				    "      from c_acct_acctitem a, "+
//				    "           (select acct_id, acctitem_id, sum(owe_fee) owe_fee "+
//				    "              from b_bill "+
//				    "             group by acct_id, acctitem_id) b "+
//				    "     where a.acct_id = b.acct_id "+
//				    "       and a.acctitem_id = b.acctitem_id) "+
//				    "     set owe_fee = new_owe_fee;";
//		this.executeUpdate(sql);
//	}
	
	/**
	 * 更新指定账目的欠费金额，实时出账后调用
	 * @throws Exception
	 */
//	public void updateOweFeeByBill(String acctId,String acctItemId) throws Exception {
//		String sql ="update (select /\\*\\+bypass_ujvc\\*/ "+
//				    "     a.acct_id, a.acctitem_id, a.owe_fee, b.owe_fee new_owe_fee "+
//				    "      from c_acct_acctitem a, "+
//				    "           (select acct_id, acctitem_id, sum(owe_fee) owe_fee "+
//				    "              from b_bill " +
//				    "              where acct_id='"+acctId+"' and acctitem_id='"+acctItemId+"'"+
//				    "             group by acct_id, acctitem_id) b "+
//				    "     where a.acct_id = b.acct_id "+
//				    "       and a.acctitem_id = b.acctitem_id" +
//				    "       and a.acct_id='"+acctId+"' and a.acctitem_id='"+acctItemId+"' ) "+
//				    "     set owe_fee = new_owe_fee;";
//		this.executeUpdate(sql);
//	}
	
	/**
	 * 查询客户公用账目
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctAcctitemActive queryPublicByCustId(String custId,String feeType) throws JDBCException {
		String sql = "select ca.* from c_acct_acctitem_active ca,c_acct c where ca.acct_id=c.acct_id and ca.acctitem_id=? "+
			" AND c.cust_id=? and c.acct_type=? AND ca.fee_type=?";
		return createQuery(CAcctAcctitemActive.class,sql, SystemConstants.ACCTITEM_PUBLIC_ID,custId,
				SystemConstants.ACCT_TYPE_PUBLIC,feeType).first();
	}

	public CAcctAcctitem queryPublicByCustId(String custId) throws JDBCException {
		String sql = "select ca.* from c_acct_acctitem ca,c_acct c where ca.acct_id=c.acct_id and ca.acctitem_id=? "+
		" AND c.cust_id=? and c.acct_type=? ";
		return createQuery(CAcctAcctitem.class,sql, SystemConstants.ACCTITEM_PUBLIC_ID,custId,
				SystemConstants.ACCT_TYPE_PUBLIC).first();
	}
	/**
	 * 查询一个客户下的所有公用账目
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitem> queryPubAcctItemsByCustId(String custId,String countyId) throws JDBCException {
		String sql =StringHelper.append("select ca.* from c_acct_acctitem ca,c_acct c where ca.acct_id=c.acct_id  ",
		" AND c.cust_id=? and c.acct_type=? and ca.county_id=c.county_id and ca.county_id=? and c.county_id=? ");
		return createQuery(CAcctAcctitem.class,sql,custId,SystemConstants.ACCT_TYPE_PUBLIC,countyId,countyId).list();
	}
	
	public List<AcctitemDto> queryAcctitemToCallCenter(Map<String,Object> params,String countyId) throws Exception{
		String acctType = params.get("ACCT_TYPE").toString(), sql = "";
		if(acctType.equals(SystemConstants.ACCT_TYPE_PUBLIC)){
			sql = "select pa.acctitem_name,a.acct_type,a.pay_type,aa.*"
				+" from c_acct a,c_acct_acctitem aa,t_public_acctitem pa"
				+" where a.acct_id=aa.acct_id and aa.acctitem_id=pa.acctitem_id"
				+" and a.acct_type=? and a.county_id=? and aa.county_id=?";
		}else if(acctType.equals(SystemConstants.ACCT_TYPE_SPEC)){
			sql = "select pa.prod_name acctitem_name,a.acct_type,a.pay_type,aa.*"
				+" from c_acct a,c_acct_acctitem aa,p_prod pa"
				+" where a.acct_id=aa.acct_id and aa.acctitem_id=pa.prod_id"
				+" and a.acct_type=? and a.county_id=? and aa.county_id=?";
		}
		
		boolean flag = false,flag2 = false;
		String cond = "";
		Iterator<String> it = params.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = params.get(key).toString();
			if("CUST_ID".equals(key)){
				cond += " and a.cust_id='"+value+"'";
			}
			if("CUST_NO".equals(key)){
				cond += " and a.cust_id=(select cust_id from c_cust where cust_no='"+value+"')";
			}
			if("CARD_ID".equals(key)){
				cond += " and u.card_id='"+value+"'";
				flag = true;
			}
			if("STB_ID".equals(key)){
				cond += " and u.stb_id='"+value+"'";
				flag = true;
			}
			if("BROAD_NAME".equals(key)){
				cond += " and ub.login_name='"+value+"'";
				flag2 = true;
			}
			if("USER_ID".equals(key)){
				cond += " and a.user_id='"+value+"'";
			}
			if("ACCTITEM_ID".equals(key)){
				cond += " and aa.acctitem_id='"+value+"'";
			}
		}
		
		if(flag){
			sql = "select pa.acctitem_name,a.acct_type,a.pay_type,aa.*"
				+" from c_acct a,c_acct_acctitem aa,t_public_acctitem pa,c_user u"
				+" where a.acct_id=aa.acct_id and aa.acctitem_id=pa.acctitem_id and a.cust_id=u.cust_id"
				+" and a.acct_type=?" + cond +" and a.county_id=? and aa.county_id=? and u.county_id='"+countyId+"'";
		}
		
		if(flag2){
			sql = "select pa.acctitem_name,a.acct_type,a.pay_type,aa.*"
				+" from c_acct a,c_acct_acctitem aa,t_public_acctitem pa,c_user u,c_user_broadband ub"
				+" where a.acct_id=aa.acct_id and aa.acctitem_id=pa.acctitem_id and a.cust_id=u.cust_id and u.user_id=ub.user_id"
				+" and a.acct_type=?" + cond +" and a.county_id=? and aa.county_id=? and u.county_id='"+countyId+"'";
		}
		
		if(!flag && !flag) sql += cond;
		return this.createQuery(AcctitemDto.class, sql, acctType, countyId, countyId).list();
	}
	
	public CAcctAcctitem queryAcctItem(String acctId,String acctItemId) throws JDBCException{
		return createQuery("SELECT * FROM c_acct_acctitem t where t.acct_id=? and t.acctitem_id=?",acctId,acctItemId).first();
	}
}
