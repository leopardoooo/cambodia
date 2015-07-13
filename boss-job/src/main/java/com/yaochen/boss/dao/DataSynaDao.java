package com.yaochen.boss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
@Component
public class DataSynaDao extends BaseEntityDao<BBill> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4284990256914446064L;

	/**
	 * 清除关系对应表，并重新生成
	 * 
	 * @throws JDBCException
	 */
	public void synaTable() throws JDBCException {
		executeUpdate("{call proc_billdadasyna_czly() }");
//		executeUpdate("drop table acct_czly");
//		executeUpdate("create table acct_czly as SELECT * FROM qry.acct_czly@czly ");
//		System.out.println("acct_czly over");
//		executeUpdate("drop table ins_prod_res_czly");
//		executeUpdate("create table ins_prod_res_czly as SELECT * FROM qry.ins_prod_res_czly@czly where res_type='45'");
//		System.out.println("ins_prod_res_czly over");
//		executeUpdate("drop table acct_item_czly");
//		executeUpdate("create table acct_item_czly as SELECT * FROM qry.acct_item_czly@czly where acct_item_type_id in (select czly_acct_item_type_id from czly_pk_acctitem) union all SELECT * FROM qry.acct_item_2014_czly@czly where acct_item_type_id in (select czly_acct_item_type_id from czly_pk_acctitem)");
//		executeUpdate("drop table acct_balance_czly");
//		executeUpdate("create table acct_balance_czly as SELECT * FROM qry.acct_balance_czly@czly ");
	}

	public List<Map<String, Object>> queryAllCzlyAcctItem() throws JDBCException {
		return findToList("SELECT T.*,pai.boss_acctitem_id,a.acct_id boss_acct_id,c.cust_id,b.boss_bill_sn FROM CZLY_PK_BILL_SN B,c_cust c ,c_acct a, acct_item_czly t,czly_pk_acctitem pai where b.czly_bill_sn(+)=t.src_bill_id and c.str1=t.acct_id and t.acct_item_type_id=pai.czly_acct_item_type_id and a.cust_id=c.cust_id and a.acct_type='PUBLIC'");
//		return findToList("select ai.*,u.acct_id boss_acct_id,pai.boss_acctitem_id,a.acct_id,u.cust_id,u.user_id from acct_item_czly ai,ins_prod_res_czly t,c_user u,acct_czly a ,czly_pk_acctitem pai where ai.billing_cycle_id=? and u.stb_id=t.res_equ_no and a.cust_id=t.cust_id and a.acct_id=ai.acct_id and ai.acct_item_type_id=pai.czly_acct_item_type_id ",billingCycleId);
	}

//	public void insert2Bill(String prodSn, String czlyAcctId,
//			String czlyAcctitemId) throws JDBCException {
//		String sql = "delete b_bill where bill_sn=(select 'A'||src_bill_id from acct_item_czly where  acct_id=? and acct_item_type_id=?)";
//		executeUpdate(sql, czlyAcctId, czlyAcctitemId);
//		sql = "insert into b_bill  (bill_sn, cust_id, acct_id, acctitem_id, user_id, serv_id, prod_sn, prod_id,   tariff_id, billing_cycle_id, come_from, status, fee_flag,   bill_type, bill_done_code, bill_date,  final_bill_fee, owe_fee, area_id, county_id, prod_type)"
//				+ " SELECT 'A'||cai.src_bill_id,p.cust_id,p.acct_id,p.prod_id,p.user_id,'DTV',p.prod_sn,p.prod_id,p.tariff_id,cai.billing_cycle_id,'7','1','ZC','2',null,cai.created_date,cai.ppy_amount,cai.amount,p.area_id,p.county_id,'BASE'"
//				+ " FROM acct_item_czly cai ,c_prod p where p.prod_sn=? and cai.acct_id=? and cai.acct_item_type_id=?";
//		executeUpdate(sql, prodSn, czlyAcctId, czlyAcctitemId);
//	}

	public BBill queryBill(String acctId, String acctitemId,
			String billingCycleId) throws JDBCException {
		return createQuery("SELECT * FROM b_bill b where b.acctitem_id =? and b.acct_id=? and b.billing_cycle_id=? ",acctitemId,acctId,billingCycleId).first();
		
	}
	
	public BBill queryBill(String billSn) throws JDBCException {
		return createQuery("SELECT * FROM b_bill b where bill_sn=? ",billSn).first();
		
	}


	public String queryTariffId(String acctId, String acctitemId)
			throws JDBCException {
		return findUnique(
				"SELECT t.tariff_id FROM c_prod t where t.acct_id=? and t.prod_id=?",
				acctId, acctitemId);
	}

	public List<CAcctAcctitemActive> queryAcctActive(String acctId, String acctitemId) throws JDBCException {
		return createQuery(CAcctAcctitemActive.class,"SELECT * FROM t_acct_fee_type t,c_acct_acctitem_active a where t.fee_type=a.fee_type " +
				"and a.acctitem_id=? and acct_id=? order by t.priority", acctitemId,acctId).list();
	}
	
	public void updateOwnFee(String acctId,String acctitemId,Integer ownFee) throws JDBCException{
		executeUpdate("update c_acct_acctitem set owe_fee=owe_fee+? where acct_id=? and acctitem_id=?",ownFee,acctId,acctitemId);
	}
	
	public void saveCzlyPkBillSn(String bossBillSN,String czlyBillsn) throws JDBCException{
		executeUpdate("insert into czly_pk_bill_sn values(?,?)",bossBillSN,czlyBillsn);
	}

}
