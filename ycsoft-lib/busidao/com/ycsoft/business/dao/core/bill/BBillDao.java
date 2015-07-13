/**
 * BBillDao.java	2010/11/03
 */

package com.ycsoft.business.dao.core.bill;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * BBillDao -> B_BILL table's operator
 */
@Component
public class BBillDao extends BaseEntityDao<BBill> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6868341203194423296L;

	/**
	 * default empty constructor
	 */
	public BBillDao() {}

	/**
	 * 包多月的开始生效日期发生了变化，修改已出账账单的账期
	 */
	public void updateMuchProdBillByChangeBillinfoEffDate(String prodSn,String billingCycle,String countyId,int changeMonths )throws Exception{
		String sql=StringHelper.append("update bill.b_bill b " ,
				" set b.billing_cycle_id=  to_char(add_months(to_date(b.billing_cycle_id,'yyyymm'),?),'yyyymm') ",
                " where b.prod_sn=?  and b.county_id=? and b.come_from in ('5','6') and b.status='1' and b.owe_fee>0 and b.billing_cycle_id>=? ");
		
		this.executeUpdate(sql,changeMonths, prodSn,countyId,billingCycle);
	}
	/**
	 * 查询包多月的账单
	 * @param custId
	 * @param doneCode
	 * @param billmonth
	 * @return
	 * @throws Exception
	 */
	public List<BBill> queryMuchProdBill(String prodSn,int doneCode,String billingCycle,String comeFrom,String countyId) throws Exception{
		String sql=StringHelper.append("select * from b_bill where come_from=? and prod_sn=? ",
				 "and bill_done_code=? and billing_cycle_id>=? and status ='1' and county_id=? ");
		
		return this.createQuery(sql,comeFrom, prodSn,doneCode,billingCycle,countyId).list();
	}
	
	/**
	 * 查询套餐缴费的额外账单
	 * @param custId
	 * @param doneCode
	 * @param billmonth
	 * @return
	 * @throws Exception
	 */
	public List<BBill> queryPromFeeBill(String custId,int doneCode,String billmonth, String comeFrom) throws Exception{
		String sql="select * from b_bill where come_from=? and cust_id=? and bill_done_code=? and billing_cycle_id>=? and status ='1' ";
		
		return this.createQuery(sql,comeFrom, custId,doneCode,billmonth).list();
	}
	
	public List<BBill> queryPromOweFeeBill(String prodSn) throws Exception {
		String sql = "select * from b_bill t where t.prod_sn=? and t.come_from='5' and t.status='1' and t.owe_fee>0";
		return this.createQuery(sql, prodSn).list();
	}
	
	public List<BBill> queryOweFeeBill(String prodSn) throws Exception {
		String sql = "select * from b_bill t where t.prod_sn=? and t.owe_fee<>0";
		return this.createQuery(sql, prodSn).list();
	}
	
	public Integer queryPromFeeOweFeeSumByProdSn(String custId,int doneCode,String prodSn) throws Exception{
		String sql="select nvl(sum(owe_fee),0) from b_bill where come_from='5' and cust_id=? and bill_done_code=? and prod_sn=?";
		return Integer.parseInt(this.findUnique(sql, custId,doneCode,prodSn));
	}
	/**
	 * 出帐
	 * @param prodSn
	 * @param doneCode
	 * @throws Exception
	 */
	public BBill confirmBill(String prodSn,int doneCode) throws Exception{
		BBill bill= this.createQuery("select * from b_bill where prod_sn=? and status='0'",prodSn).first();
		String sql = "update b_bill set status='1' ,bill_type='1',bill_date=sysdate,owe_fee=final_bill_fee,bill_done_code=? " +
				" where prod_sn =? and status='0' ";
		executeUpdate(sql, doneCode,prodSn);
		
		return bill;
	}

	public int updateBill(String prodSn,int billFee) throws Exception{
		String sql = "update b_bill set FINAL_BILL_FEE=? ,owe_fee=?" +
		" where prod_sn =? and status='0'";
		return executeUpdate(sql, billFee,billFee,prodSn);
	}
	
	public void updateMuchBill(String prodSn, int fee, String billingCycleId, String countyId) throws Exception {
		String sql = "update b_bill set FINAL_BILL_FEE=FINAL_BILL_FEE+? ,owe_fee=owe_fee+?" +
		" where prod_sn =? and county_id=? and billing_cycle_id=? and come_from='6' and status='1' and owe_fee<>0";
		this.executeUpdate(sql, fee, fee, prodSn, countyId, billingCycleId);
	}
	
	public void updateBillInfo(String oldProdSn, String newProdSn, String newTariffId, 
			String newAcctId, String newProdId, String countyId) throws Exception {
		String sql = "update b_bill set prod_sn=?,tariff_id=?,acct_id=?,acctitem_id=?,prod_id=? where prod_sn=? and county_id=?";
		this.executeUpdate(sql, newProdSn, newTariffId, newAcctId, newProdId, newProdId, oldProdSn, countyId);
	}
	
	public void save(BBill bill) throws Exception{
		String sql = " insert into b_bill "+
			" (bill_sn, " +
			" cust_id," +
			" acct_id," +
			" acctitem_id," +
			" user_id," +
			" serv_id," +
			" prod_sn," +
			" prod_id," +
			" tariff_id," +
			" billing_cycle_id," +
			" come_from," +
			" status," +
			" fee_flag, " +
			" bill_type," +
			" bill_done_code," +
			" bill_date," +
			" final_bill_fee," +
			" owe_fee, " +
			" area_id," +
			" county_id,prod_type) "+
			" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.executeUpdate(sql, bill.getBill_sn(),bill.getCust_id(),bill.getAcct_id(),
				bill.getProd_id(),bill.getUser_id(),bill.getServ_id(),bill.getProd_sn(),bill.getProd_id(),
				bill.getTariff_id(),bill.getBilling_cycle_id(),bill.getCome_from(),bill.getStatus(),
				bill.getFee_flag(),bill.getBill_type(),bill.getBill_done_code(),bill.getBill_date(),bill.getFinal_bill_fee(),
				bill.getOwe_fee(),bill.getArea_id(),bill.getCounty_id(),bill.getProd_type());
	}

	public void deleteBill(int doneCode) throws Exception{
		String sql = "delete b_bill where bill_done_code=?";
		this.executeUpdate(sql, doneCode);
	}
	
	/**
	 * 根据客户和月份查询客户月账单
	 * @param custId
	 * @param month 格式 YYYYMM
	 * @return
	 * @throws Exception
	 */
	public List<BillDto> queryBillByCustId(String custId,String month) throws Exception{
		String sql = "select pt.tariff_name,v.acctitem_name ,b.*"
				+ " from b_bill b,vew_acctitem v , p_prod_tariff pt where b.cust_id=? "
				+ " and b.billing_cycle_id=? and b.acctitem_id =v.acctitem_id(+)"
				+ " and b.tariff_id=pt.tariff_id(+) order by b.BILLING_CYCLE_ID	 desc";
		return createQuery(BillDto.class,sql, custId,month).list();
	}
	/**
	 * 根据客户查询账单.
	 * @param custId
	 * @param queryFeeInfo
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<BillDto> queryCustBill(String custId, QueryFeeInfo queryFeeInfo, 
			Integer start, Integer limit) throws Exception{
		String sql = "select pt.tariff_name,v.acctitem_name ,c.card_id,b.*,"
			+ " decode(b.come_from,'1','正常计费','2','前台手工','3','调账不退','4','调账可退','5','套餐缴费','6','包多月','其他') come_from_text"
			+ " from b_bill b,vew_acctitem v , p_prod_tariff pt ,c_user c"
			+ " where b.cust_id=? "
//			+ " and ( b.billing_cycle_id=to_char(sysdate,'yyyymm') or (b.billing_cycle_id>to_char(sysdate,'yyyymm') and  b.come_from in ('5','6') ))"
			+ "and b.acctitem_id =v.acctitem_id(+) and b.tariff_id=pt.tariff_id(+)  and b.user_id = c.user_id(+) and b.county_id= c.county_id(+)";
		if(queryFeeInfo != null){
			if(queryFeeInfo.isOweFee()){
				sql += " and b.OWE_FEE > 0 and b.status in ('0','1') ";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getBill_done_code())){
				sql += " and b.OWE_FEE > 0 and b.bill_done_code = '" + queryFeeInfo.getBill_done_code() + "' ";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getCard_id())){
				sql += " and c.card_id =  '" + queryFeeInfo.getCard_id() + "' ";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getBilling_cycle_id())){
				sql += " and b.billing_cycle_id = '" + queryFeeInfo.getBilling_cycle_id() + "'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getAcctitem_name())){
				sql += " and v.acctitem_name like '%" + queryFeeInfo.getAcctitem_name() + "%'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getTariff_name())){
				sql += " and pt.tariff_name like '%" + queryFeeInfo.getTariff_name() + "%'";
			}
			String startBillCycle = queryFeeInfo.getBill_date1();
			if(StringHelper.isNotEmpty(startBillCycle)){//开始，结束的改为账期的范围而不是bill_date的范围
				startBillCycle = startBillCycle.substring(0, 6);//只取年月
				sql += " and to_number(b.billing_cycle_id) >= " + startBillCycle + " ";
			}
			String endBillCycle = queryFeeInfo.getBill_date2();
			if(StringHelper.isNotEmpty(endBillCycle)){
				endBillCycle = endBillCycle.substring(0, 6);//只取年月
				sql += " and to_number(b.billing_cycle_id) <= " + endBillCycle + " ";
			}
		}
		sql += " order by b.BILLING_CYCLE_ID desc,card_id,bill_sn";
		return createQuery(BillDto.class,sql, custId).setStart(start).setLimit(limit).page();
	}

	public void updateStatus(String billSn, String status) throws Exception{
		String sql="update b_bill set status=?,bill_type=1 where bill_sn=?";
		this.executeUpdate(sql, status,billSn);
	}
	
	public void updateFeeStatus(String billSn,String feeStatus) throws Exception{
		String sql="update b_bill set fee_flag = ? ,bill_type=1 where bill_sn=?";
		this.executeUpdate(sql,feeStatus,billSn);
	}
	
	public void cancelBill(String prodSn, String billCycleId, String status) throws JDBCException {
		String sql="update b_bill set status=?,bill_type=1 where prod_sn=? and owe_fee<>0 and billing_cycle_id >= ?";
		this.executeUpdate(sql,status, prodSn,billCycleId);
	} 
	
	public List<BBill> queryMuchBill(String prodSn, String billCycleId, String comeFrom) throws Exception {
		String sql = "select * from b_bill t where prod_sn=? and t.owe_fee<>0 and come_from=? and billing_cycle_id >= ?";
		return this.createQuery(sql, prodSn, comeFrom, billCycleId).list();
	}
	
	public void cancalMuchBill(String prodSn, String billCycleId, String status) throws Exception {
		String sql="update b_bill set status=?,bill_type=1 where prod_sn=? and owe_fee<>0 and billing_cycle_id >= ? and come_from in (?,?)";
		this.executeUpdate(sql,status, prodSn,billCycleId, SystemConstants.BILL_COME_FROM_MUCH, SystemConstants.BILL_COME_FROM_PROM);
	}
	
	public void cancelTerminateBill(String prodSn, String billCycleId, String status) throws Exception {
		String sql="update b_bill set status=?,bill_type=1 where prod_sn=? and owe_fee<>0 and billing_cycle_id > ? and come_from not in (?,?)";
		this.executeUpdate(sql,status, prodSn,billCycleId, SystemConstants.BILL_COME_FROM_MUCH, SystemConstants.BILL_COME_FROM_PROM);
	}
	/**
	 * 取消指定账期之后的所有的账单
	 * @param prodSn
	 * @param billCycleId
	 * @param status
	 * @throws JDBCException
	 */
	public void cancelOweActiveBill(String prodSn, String billCycleId, String status)throws JDBCException {
		String sql="update b_bill set status=?,bill_type=1 where prod_sn=?  and billing_cycle_id >= ?";
		this.executeUpdate(sql,status, prodSn,billCycleId);
	} 
	
	public void recoverBill(String prodSn, String billCycleId, String status) throws JDBCException {
		String sql="update b_bill set status=? where prod_sn=? and status='4' and billing_cycle_id >= ?";
		this.executeUpdate(sql,status, prodSn,billCycleId);
	} 
	
	public void updateBillCycle(String billSn, String billCycleId) throws JDBCException {
		String sql = "update b_bill set billing_cycle_id=? where bill_sn=?";
		this.executeUpdate(sql, billCycleId, billSn);
	}

	/**
	 * 查找产品最后一个月的未出账账单
	 * @param prodSn
	 * @return
	 * @throws JDBCException
	 */
	public BBill queryLatsBillByProdSn(String prodSn) throws JDBCException {
		String sql = "select * from b_bill b where b.prod_sn=?  and status='0' order by b.billing_cycle_id desc";
		return createQuery(sql, prodSn).first();
	}

	public List<BBill> queryOweBillByProdSn(String prodSn) throws Exception {
		String sql = "select * from b_bill t where t.status='0' and t.prod_sn=?" +
				" union all " +
				" select * from b_bill t where t.status='1' and t.prod_sn=? and t.owe_fee>0";
		return this.createQuery(sql, prodSn, prodSn).list();
	}
	
	public List<BBill> queryNotBillByProdSn(String prodSn) throws Exception {
		String sql = "select * from b_bill t where t.prod_sn=? and t.status='0'";
		return this.createQuery(sql, prodSn).list();
	}
	
	public List<BBill> queryByAcctId(String acctId) throws Exception {
		String sql = "select * from B_bill t where t.acct_id=?";
		return this.createQuery(sql, acctId).list();
	}
	
	public void updateBillByAcctId(String acctId, String newCustId, 
			String newUserId,String newAcctId) throws Exception{
		
		String sql = "update b_bill set cust_id=?,user_id=?,acct_id=?" +
		" where bill_sn in (" +
			"select bill_sn from b_bill where acct_id=? and status='0'" +
			" union all " +
			"select bill_sn from b_bill where acct_id=? and status='1' and owe_fee > 0" +
		")";
		this.executeUpdate(sql, newCustId, newUserId, newAcctId, acctId, acctId);
		
		sql = "update b_bill_detail set cust_id=?,user_id=?,acct_id=?" +
				" where bill_sn in (" +
					"select bill_sn from b_bill where acct_id=? and status='0'" +
					" union all " +
					"select bill_sn from b_bill where acct_id=? and status='1' and owe_fee > 0" +
				")";
		this.executeUpdate(sql, newCustId, newUserId, newAcctId, acctId, acctId);
		
		sql = "update B_RENTFEE_DAY set cust_id=?,user_id=?,acct_id=? where acct_id=?";
		this.executeUpdate(sql, newCustId, newUserId, newAcctId, acctId);
		
		sql = "update B_RENTFEE set cust_id=?,user_id=?,acct_id=? where prod_sn in (" +
				"select prod_sn from c_prod where acct_id=?)";
		this.executeUpdate(sql, newCustId, newUserId, newAcctId, acctId);
		
		
	}
	
	public List<BillDto> queryCustOweBill(String custId)throws Exception{
		final String sql = "select t.*, t1.prod_name "
							+" FROM b_bill t, p_prod t1"
							+" WHERE t.prod_id=t1.prod_id AND t.cust_id =?"
							+" AND t.status='1' AND t.owe_fee>0"
							+" AND t.billing_cycle_id<to_char(SYSDATE,'yyyymm')";
		return this.createQuery(BillDto.class, sql, custId).list();
	}
	
	//TODO 有问题
	public void updateStatusByDoneCode(Integer doneCode, String status,String billMonth, String comeFrom) throws Exception{
		String sql="update b_bill set status=?,bill_type=1 where status='1' and comeFrom=? and billing_cycle_id>=? and bill_done_code=?";
		this.executeUpdate(sql, status, comeFrom, billMonth, doneCode);
	}

	/**
	 * 更新出帐金额
	 * @param billSn
	 * @param amount
	 * @throws JDBCException
	 */
	public void updateBillFee(String billSn,Integer addFee)
			throws JDBCException {
		String sql = "update b_bill set final_bill_fee=final_bill_fee+?,owe_fee=owe_fee+? where bill_sn=?";
		executeUpdate(sql, addFee,addFee, billSn);
	}

}
