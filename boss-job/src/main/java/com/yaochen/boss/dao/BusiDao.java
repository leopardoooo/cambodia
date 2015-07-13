package com.yaochen.boss.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.yaochen.boss.commons.BankConstants;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.bank.CBankGotodisk;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdInclude;
import com.ycsoft.beans.core.promotion.CPromotion;
import com.ycsoft.beans.core.user.BandSimpleInfo;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.business.dao.core.bank.CBankReturnDao;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.DataHandler;
import com.ycsoft.daos.core.JDBCException;
/**
 * @author liujiaqi
 *
 */
@Component
public class BusiDao extends BaseEntityDao<BBill> {
	/**
	 *
	 */
	private static final long serialVersionUID = 922972498260962526L;
	
	/**
	 * 当天到期包月产品
	 * @return
	 * @throws Exception
	 */
	public void queryInvalidProd(Date invalidDate,DataHandler<CProdDto> dataHandler) throws Exception {
		String sql = "select cp.*,a.owe_fee,a.real_bill,a.active_balance all_balance,a.inactive_balance,"
			+" ppt.billing_cycle,ppt.billing_type,p.is_base,ppt.rent tariff_rent"
			+" from c_prod cp,c_acct_acctitem a,p_prod p,p_prod_tariff ppt"
			+" where cp.acct_id=a.acct_id and cp.prod_id=a.acctitem_id"
			+" and cp.prod_id=p.prod_id and cp.tariff_id=ppt.tariff_id"
			+" and cp.stop_by_invalid_date=? and ppt.rent>0 and ppt.billing_cycle=1 and ppt.billing_type='BY'"
			+" and (cp.invalid_date=to_date(?,'yyyymmdd') or cp.invalid_date=to_date(?,'yyyymmdd'))"
			+" and cp.package_sn is null and cp.status=?";
		
		this.queryForResult(CProdDto.class, dataHandler, sql, SystemConstants.BOOLEAN_TRUE, 
				DateHelper.dateToStrYMD(invalidDate), DateHelper.dateToStrYMD(DateHelper.addDate(invalidDate, 1)), StatusConstants.ACTIVE);
		
	}
	
	public List<CProdDto> queryInvalidProd(Date invalidDate) throws Exception {
		String sql = "select * from ("
			+" select cp.*,a.owe_fee,a.real_bill,a.active_balance all_balance,a.inactive_balance,"
			+" ppt.billing_cycle,ppt.billing_type,p.is_base,ppt.rent tariff_rent"
			+" from c_prod cp,c_acct_acctitem a,p_prod p,p_prod_tariff ppt"
			+" where cp.acct_id=a.acct_id and cp.prod_id=a.acctitem_id"
			+" and cp.prod_id=p.prod_id and cp.tariff_id=ppt.tariff_id"
			+" and cp.stop_by_invalid_date=? and ppt.rent>0 and ppt.billing_cycle=1 and ppt.billing_type='BY'"
			+" and (cp.invalid_date=to_date(?,'yyyymmdd') or cp.invalid_date=to_date(?,'yyyymmdd'))"
			+" and cp.package_sn is null and cp.status=?"
			+" and not exists (select 1 from J_PROD_INVALID_CAL j where j.prod_sn=cp.prod_sn)"
			+" ) where rownum<=4000";
		
		return this.createQuery(CProdDto.class, sql, SystemConstants.BOOLEAN_TRUE, 
				DateHelper.dateToStrYMD(invalidDate), DateHelper.dateToStrYMD(DateHelper.addDate(invalidDate, 1)), StatusConstants.ACTIVE).list();
		
	}
	
	public List<CProd> queryStopProd() throws Exception {
		String sql = "select cp.* from c_prod cp,p_prod_tariff ppt"
			+" where cp.tariff_id=ppt.tariff_id"
			+" and cp.stop_by_invalid_date=?"
			+" and ppt.billing_cycle=1 and ppt.billing_type='BY' and ppt.rent>0"
			+" and cp.status <> ?";
		return this.createQuery(CProd.class, sql, SystemConstants.BOOLEAN_TRUE, StatusConstants.ACTIVE).list();
	}
	
	public void updateStopProd(CProd prod) throws Exception {
		String sql = "update c_prod set stop_by_invalid_date=? where prod_sn=?";
		this.executeUpdate(sql, SystemConstants.BOOLEAN_FALSE, prod.getProd_sn());
		
		sql = "insert into c_prod_prop_change(prod_sn,column_name,old_value,new_value,done_code,county_id,area_id) values(?,?,?,?,?,?,?)";
		this.executeUpdate(sql, prod.getProd_sn(),"stop_by_invalid_date", 
				SystemConstants.BOOLEAN_TRUE, SystemConstants.BOOLEAN_FALSE,
				"-222",prod.getCounty_id(),prod.getArea_id()
		);
	}
	
	public void saveNoDelProd(CProdDto prod) throws Exception {
		String sql = "insert into bak.C_PROD_NODEL(prod_sn,invalid_date,active_balance,owe_fee,real_bill,inactive_balance) values(?,?,?,?,?,?)";
		this.executeUpdate(sql, prod.getProd_sn(), prod.getInvalid_date(), prod.getAll_balance(), prod.getOwe_fee(), prod.getReal_bill(), prod.getInactive_balance());
	}
	

	//查找用户名下的产品
	public List<CProd> queryUserProd(String userId,String countyId) throws JDBCException{
		String sql ="SELECT c.* "+
					"  FROM c_prod c, p_prod p "+
					" where c.package_id = p.prod_id(+) "+
					"   and (c.package_sn is null or p.prod_type = ?) "+
					"   and c.prod_type<>? "+
					"   and c.user_id=?  "+
					"   and c.county_id=?";
		return this.createQuery(CProd.class,sql, SystemConstants.PROD_TYPE_CUSTPKG, SystemConstants.PROD_TYPE_CUSTPKG,userId,countyId).list();
	}
	//查找用户名下开通状态的产品
	public List<CProd> queryUserOpenProd(String userId,String countyId) throws JDBCException{
		String sql ="SELECT c.* "+
					"  FROM c_prod c, p_prod p  "+
					" where c.package_id = p.prod_id(+) and c.status in " +
					" (select status_id from  t_prod_status_openstop tpso where  tpso.open_or_stop='1' ) "+
					"   and (c.package_sn is null or p.prod_type = ?) "+
					"   and c.prod_type<>? "+
					"   and c.user_id=?  "+
					"   and c.county_id=?";
		return this.createQuery(CProd.class,sql, SystemConstants.PROD_TYPE_CUSTPKG, SystemConstants.PROD_TYPE_CUSTPKG,userId,countyId).list();
	}

	//根据套餐编号查找套餐对应的产品
	public List<CProd> queryChildProd(String packageSn,String countyId) throws JDBCException{
		String sql ="String * from c_prod where package_sn=? and county_id=? ";
		return this.createQuery(CProd.class,sql,packageSn,countyId).list();
	}

	//设置产品之间的关系
	public void saveProdInclude(String userId,List<CProdInclude> includeList) throws Exception{
		String sql="delete c_prod_include where user_id=?";
		executeUpdate(sql, userId);
		for (CProdInclude include :includeList){
			sql = "insert into c_prod_include (cust_id,user_id,prod_sn,include_prod_sn) " +
					" values (?,?,?,?)";
			executeUpdate(sql,include.getCust_id(),include.getUser_id(),include.getProd_sn(),include.getInclude_prod_sn());
		}
	}

	//查找客户基本信息
	public CCust queryCust(String custId) throws Exception{
		String sql = "select * from c_cust where cust_id=? ";
		return this.createQuery(CCust.class,sql, custId).first();
	}

	//查找用户基本信息
	public CUser queryUser(String userId) throws Exception{
		String sql = "select * from c_user where user_id=? ";
		List<CUser> userList = this.createQuery(CUser.class,sql, userId).list();
		if (userList != null && userList.size()>0)
			return userList.get(0);
		else
			return null;
	}
	
	//查找用户基本信息
	public CAcct queryAcct(String acctId) throws Exception{
		String sql = "select * from c_acct where acct_id=? ";
		List<CAcct> acctList = this.createQuery(CAcct.class,sql, acctId).list();
		if (acctList != null && acctList.size()>0)
			return acctList.get(0);
		else
			return null;
	}
	
	//查找宽带用户信息
	public BandSimpleInfo queryUserBand(String userId) throws Exception{
		String sql = "select * from c_user_broadband where user_id=? ";
		List<BandSimpleInfo> userList = this.createQuery(BandSimpleInfo.class,sql, userId).list();
		if (userList != null && userList.size()>0)
			return userList.get(0);
		else{
			sql = "select * from c_user_broadband_his where user_id=? ";
			return createQuery(BandSimpleInfo.class,sql, userId).first();
		}
	}

	/**
	 * 查找用户当天已经参加过的促销主题
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<String> queryUserPromotionTheme(String userId) throws Exception{
		String sql = "select distinct theme_id from p_promotion pp,c_promotion cp " +
				" where pp.promotion_id= cp.promotion_id " +
				" and cp.create_time>=to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd') " +
				" and cp.user_id=? " +
				" and cp.status=?";
		return findUniques(sql, userId,StatusConstants.ACTIVE);
	}

	/**
	 * 查找客户当天已经参加过的促销主题
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<CPromotion> queryCustPromotion(String custId) throws Exception{
		String sql = "select * from c_promotion cp " +
				" where cp.create_time>=to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd') " +
				" and cp.cust_id=? " +
				" and cp.status=? ";
		return this.createQuery(CPromotion.class,sql, custId,StatusConstants.ACTIVE).list();
	}
	
	/**
	 * 查找用户订购过的宽带产品历史数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int queryUserBandProdHis (String userId) throws Exception {
		String sql ="select count(1) from c_prod_his a,p_prod b where a.prod_id=b.prod_id and " +
				" a.user_id=? and b.serv_id=? ";
		return Integer.parseInt(this.findUnique(sql, userId,SystemConstants.PROD_SERV_ID_BAND));
	}
	
	/**
	 * 删除产品的当月账单
	 */
	
	public void delNotConfirmBill (String prodSn,String countyId) throws Exception{
		String sql="delete b_bill where status='0' and prod_sn=? and county_id=?";
		this.executeUpdate(sql,prodSn,countyId);
	}
	
	/**
	 * 更新本月账单的最后出账金额
	 */
	
	public void updateNotConfirmBill (String prodSn,int realOweFee,String countyId) throws Exception{
		String sql="update b_bill set final_bill_fee= ?,owe_fee=? where status='0' and prod_sn=? and county_id=?";
		this.executeUpdate(sql,realOweFee,realOweFee,prodSn,countyId);
	}
	
	/**
	 * 更新历史账单的final_bill_fee=final_bill_fee-owe_fee where owe_fee>0
	 */
	
	public void updateConfirmedBill (String prodSn,String countyId) throws Exception{
		String sql="update b_bill set final_bill_fee= final_bill_fee - owe_fee,owe_fee=0 where owe_fee>0 and prod_sn=? and county_id=?";
		this.executeUpdate(sql,prodSn,countyId);
	}
	
	public int sumOweBill(String prodSn,String county_id) throws Exception {
		String sql = "select nvl(sum(owe_fee),0) from b_bill where prod_sn=? and county_id=? and status='1' and owe_fee>0";
		return Integer.parseInt(this.findUnique(sql,prodSn,county_id));
	}
	
	public void cancelOweBill(String prodSn, int fee) throws Exception {
		BBill bill= this.createQuery(BBill.class, "select * from b_bill where prod_sn=? and status='1'", prodSn).first();
		if(bill != null){
			String sql = " insert into b_bill (bill_sn, cust_id, acct_id, acctitem_id, user_id, serv_id, prod_sn, prod_id, tariff_id, billing_cycle_id,"
				+" come_from, status, fee_flag, bill_type, bill_done_code, bill_date, final_bill_fee, owe_fee, area_id, county_id,prod_type)"
				+" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?)";
			this.executeUpdate(sql, this.findSequence("SEQ_BILL_SN").toString(), bill.getCust_id(), bill.getAcct_id(),
				bill.getProd_id(), bill.getUser_id(), bill.getServ_id(), bill.getProd_sn(), bill.getProd_id(), bill.getTariff_id(),
				DateHelper.format(new Date(), DateHelper.FORMAT_YM), "3","1","ZC","1","-111",
				fee, fee, bill.getArea_id(), bill.getCounty_id(), bill.getProd_type());
		}
	}
	
	public void cancelRealBill(String prodSn) throws Exception {
		String sql = "update b_bill set status='4',bill_type='1',bill_date=sysdate,owe_fee=0,bill_done_code='-111' where prod_sn=? and status='0'";
		this.executeUpdate(sql, prodSn);
	}
	
	public void cancelRealBill(String prodSn, int fee) throws Exception {
		String sql = "select * from b_bill where prod_sn=? and status='0'";
		BBill bill = this.createQuery(BBill.class, sql, prodSn).first();
		//销掉本月费用
		sql = "update b_bill set status='1',bill_type='1',bill_date=sysdate,bill_done_code='-111' where prod_sn=? and status='0'";
		this.executeUpdate(sql, prodSn);
		fee = (bill != null) ? (fee - bill.getOwe_fee().intValue()) : fee;
		
		//账单有可能不存在
		sql = "select cp.*,p.serv_id,p.prod_type" +
				" from c_prod cp,p_prod p" +
				" where cp.prod_id=p.prod_id and cp.prod_sn=?";
		CProd prod = this.createQuery(CProd.class,sql, prodSn).first();
		//调掉剩余余额
		sql = " insert into b_bill "+
		" (bill_sn,cust_id,acct_id,acctitem_id,user_id,serv_id,prod_sn,prod_id,tariff_id,billing_cycle_id,come_from,status," +
		" fee_flag,bill_type,bill_done_code,bill_date,final_bill_fee,owe_fee,area_id, county_id,prod_type) "+
		" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?)";
		this.executeUpdate(sql, this.findSequence().toString(),prod.getCust_id(),prod.getAcct_id(),
			prod.getProd_id(),prod.getUser_id(),prod.getServ_id(),prod.getProd_sn(),prod.getProd_id(),
			prod.getTariff_id(),DateHelper.nowYearMonth(),"3","1","ZC","1","-111",
			fee,fee,prod.getArea_id(),prod.getCounty_id(),prod.getProd_type());
	}
	
	public void delRentFee(String prodSn) throws Exception {
		String sql = "delete from b_rentfee where prod_sn=?";
		this.executeUpdate(sql, prodSn);
	}
	
	public void saveProdProp(String prodSn, String newStopByInvalidDate, String newStatus) throws Exception {
		CProd prod = this.createQuery(CProd.class, "select * from c_prod where prod_sn=?", prodSn).first();

		String sql = "insert into c_prod_prop_change(prod_sn,column_name,old_value,new_value,done_code,county_id,area_id)"
			+" select ?,'stop_by_invalid_date' column_name,?,?,-100,?,? from dual"
			+" union all"
			+" select ?,'status' column_name,?,?,-100,?,? from dual"
			+" union all"
			+" select ?,'status_date' column_name,?,?,-100,?,? from dual";
		this.executeUpdate(sql, 
				prodSn, prod.getStop_by_invalid_date(), newStopByInvalidDate, prod.getCounty_id(), prod.getArea_id(),
				prodSn, prod.getStatus(), newStatus, prod.getCounty_id(), prod.getArea_id(),
				prodSn, DateHelper.dateToStr(prod.getStatus_date()), DateHelper.formatNow(), prod.getCounty_id(), prod.getArea_id());
		
		sql = "update c_prod set stop_by_invalid_date=?,status=? where prod_sn=?";
		this.executeUpdate(sql, newStopByInvalidDate, newStatus, prodSn);
		
	}

	/**
	 * 查找该账号是否为宜昌老系统用户,是返回 true
	 * @param userId
	 * @return
	 * @throws JDBCException 
	 * @throws NumberFormatException 
	 */
	public boolean queryYcOldBand(String userId)
			throws NumberFormatException, JDBCException {
		String sql = "select count(1) from L_YC_OLD_BAND a,c_user_broadband b" +
				"  where a.login_name=b.login_name and b.user_id=?";
		int count = Integer.parseInt(this.findUnique(sql, userId));
		return count == 0 ? false : true;
	}

	public PProdTariff queryProdTariff(String prodSn,String countId) throws JDBCException {
		String sql = "select p.* from p_prod_tariff p ,c_prod c where p.tariff_id=c.tariff_id and c.prod_sn=? and c.county_id=?";
		PProdTariff tariff = this.createQuery(PProdTariff.class, sql, prodSn,countId).first();
		if(tariff == null){
			sql = "select p.* from p_prod_tariff p ,c_prod_his c where p.tariff_id=c.tariff_id and c.prod_sn=? and c.county_id=?";
			tariff = this.createQuery(PProdTariff.class, sql, prodSn,countId).first();
		}
		return tariff;
	}

	public void saveProdIncludeRecord(Integer doneCode, String custId,
			String userId, String countyId, String isSuccess, String errorInfo) throws JDBCException {
		String sql = StringHelper.append("insert into prod_include_record ",
				" (done_code, cust_id, user_id,  is_success, error_info, county_id) ",
				" values (?, ?, ?, ?, ?, ?)");
		this.executeUpdate(sql, doneCode,custId,userId,isSuccess,errorInfo,countyId);
	}
	
	public boolean isExistsUnBillByProdSn(String prodSn) throws Exception {
		//查询欠费大于0 的未出帐 和 已出账的账单信息
		String sql = "select count(1) from b_bill t where t.prod_sn=? and t.owe_fee>0 and t.status in ('1','0')";
		int count = this.count(sql, prodSn);
		if(count == 0){
			sql = "select invalid_date from c_prod where prod_sn=?";
			String dateStr = this.findUnique(sql, prodSn);
			if(StringHelper.isNotEmpty(dateStr)){
				Date nextBillDate = DateHelper.strToDate(dateStr);
				return DateHelper.compareDate(DateHelper.dateToStr(nextBillDate), 
						DateHelper.dateToStr(DateHelper.now())) <= 0;
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 查询银行扣款记录
	 * @return
	 * @throws Exception
	 */
	public void queryBankGotoDisk(String fileNo, DataHandler<CBankGotodisk> handler)throws Exception{
		final String sql = "SELECT * FROM c_bank_gotodisk t WHERE t.file_no=?  order by bank_fee_name,cust_no,start_date";
		this.queryForResult(CBankGotodisk.class, handler, sql, fileNo);
	}
	
	/**
	 * 保存扣费记录，从三户资料中获取信息
	 * @throws Exception
	 */
	public void saveBankGotodisk(String fileNo)throws Exception{
		
		//数字电视基本包
		String SQL = "insert into c_bank_gotodisk "
					+" (trans_sn, file_no, cust_id, cust_no, cust_name, bank_code, bank_account, acct_id,   acctitem_id,"
					+"  busi_type, bill_sn, start_date, end_date, bank_trans_sn, "
					+"  bank_fee_name,    fee, create_time, county_id, area_id,user_id,prod_sn)"
					+" select "
					+" seq_bank_trans_sn.nextval,?,a.cust_id,b.cust_no,b.cust_name,c.bank_code,c.bank_account,a.acct_id,a.acctitem_id,"
					+" '"+ BankConstants.BUSI_TYPE_KK +"',a.bill_sn,to_date(a.billing_cycle_id||'01','yyyymmdd'),add_months(to_date(a.billing_cycle_id||'01','yyyymmdd'),1),null,"
					+" e.prod_name,a.owe_fee,sysdate,a.county_id,a.area_id, a.user_id,a.prod_sn"
					+" from   b_bill a,c_cust b,c_acct_bank c,p_prod e,c_prod p"
					+" where a.status='1' and a.owe_fee>0 and a.billing_cycle_id<to_char(sysdate,'yyyymm')"
					+" and p.status<>'REQSTOP' and a.cust_id=b.cust_id and a.cust_id=c.cust_id and c.status=? "
					+" and p.user_id=a.user_id and p.prod_id=a.prod_id "
					+" and a.prod_id=e.prod_id and e.serv_id='DTV' and e.is_base='T' and p.prod_id='1'";
		executeUpdate(SQL, fileNo, StatusConstants.ACTIVE);
		
		//有银行扣费标记的增值产品
		SQL = "insert into c_bank_gotodisk "
				+" (trans_sn, file_no, cust_id, cust_no, cust_name, bank_code, bank_account, acct_id,   acctitem_id,"
				+"  busi_type, bill_sn, start_date, end_date, bank_trans_sn, "
				+"  bank_fee_name,    fee, create_time, county_id, area_id,user_id,prod_sn)"
				+" select "
				+" seq_bank_trans_sn.nextval,?,a.cust_id,b.cust_no,b.cust_name,c.bank_code,c.bank_account,a.acct_id,a.acctitem_id,"
				+" '"+ BankConstants.BUSI_TYPE_KK +"',a.bill_sn,to_date(a.billing_cycle_id||'01','yyyymmdd'),add_months(to_date(a.billing_cycle_id||'01','yyyymmdd'),1),null,"
				+" e.prod_name,a.owe_fee,sysdate,a.county_id,a.area_id, a.user_id,a.prod_sn"
				+" from b_bill a,c_cust b,c_acct_bank c,p_prod e,c_prod p"
				+" where a.status='1' and a.owe_fee>0 and a.billing_cycle_id<to_char(sysdate,'yyyymm')"
				+" and p.status<>'REQSTOP' and a.cust_id=b.cust_id and a.cust_id=c.cust_id and c.status=? "
				+" and p.user_id=a.user_id and p.prod_id=a.prod_id and p.is_bank_pay='T' and e.is_bank_pay='T'"
				+" and a.prod_id=e.prod_id and e.is_base='F'";
		executeUpdate(SQL, fileNo, StatusConstants.ACTIVE);
		
		//vod扣费产品
		SQL="insert into c_bank_gotodisk "
				+" (trans_sn, file_no, cust_id, cust_no, cust_name, bank_code, bank_account, acct_id,   acctitem_id,"
				+"  busi_type, bill_sn, start_date, end_date, bank_trans_sn, "
				+"  bank_fee_name,    fee, create_time, county_id, area_id,user_id,prod_sn)"
				+" SELECT seq_bank_trans_sn.nextval,?,a.cust_id, b.cust_no, b.cust_name, c.bank_code, c.bank_account, a.acct_id, a.acctitem_id, '"+ BankConstants.BUSI_TYPE_KK +"',a.bill_sn, " +
				" to_date(a.billing_cycle_id || '01', 'yyyymmdd'), add_months(to_date(a.billing_cycle_id || '01', 'yyyymmdd'), 1), null, null, a.owe_fee, sysdate, a.county_id, a.area_id, a.user_id, a.prod_sn" +
				" FROM b_bill a, c_cust b, c_acct_bank c where a.come_from='7' and a.status='1' and a.owe_fee>0 " +
				" and a.billing_cycle_id < to_char(sysdate, 'yyyymm')and a.cust_id = b.cust_id and " +
				" a.cust_id = c.cust_id and c.status = 'ACTIVE'";
//		executeUpdate(SQL,fileNo)	;
	}
	
	/**
	 * 保存退款记录，从三户资料中获取信息
	 * @throws Exception
	 */
	public void saveBankRefundtodisk(String fileNo) throws Exception {
		String SQL = "insert into c_bank_refundtodisk "
				+ " (trans_sn, file_no, cust_id, cust_no, cust_name, bank_code, bank_account, acct_id,   acctitem_id,"
				+ "  busi_type, bill_sn, start_date, end_date, bank_trans_sn, "
				+ "  bank_fee_name,    fee, create_time, county_id, area_id)"
				+ " select seq_bank_trans_sn.nextval,?,c1.cust_id,c1.cust_no,c1.cust_name,t.bank_code,t.bank_account,null,null,"
				+ " '"
				+ BankConstants.BUSI_TYPE_TK
				+ "',null,null,null,t.bank_trans_sn,"
				+ " t.bank_fee_name,t.real_fee,sysdate,c1.county_id,c1.area_id"
				+ " from c_bank_return_payerror t,c_cust c1"
				+ " where t.cust_no=c1.cust_no and t.refund_status is null and c1.status=?"
				+ " and not exists(select 1 from c_bank_refundtodisk d where d.bank_trans_sn=t.bank_trans_sn)";
		executeUpdate(SQL, fileNo, StatusConstants.ACTIVE);

		SQL = "insert into c_bank_refundtodisk "
				+ " (trans_sn, file_no, cust_id, cust_no, cust_name, bank_code, bank_account, acct_id,   acctitem_id,"
				+ "  busi_type, bill_sn, start_date, end_date, bank_trans_sn, "
				+ "  bank_fee_name,    fee, create_time, county_id, area_id)"
				+ " select seq_bank_trans_sn.nextval,?,c1.cust_id,c1.cust_no,c1.cust_name,t.bank_code,t.bank_account,null,null,"
				+ " '"
				+ BankConstants.BUSI_TYPE_TK
				+ "',null,null,null,t.bank_trans_sn,"
				+ " t.bank_fee_name,t.real_fee,sysdate,c1.county_id,c1.area_id"
				+ " from c_bank_return_payerror t,c_cust_his c1"
				+ " where t.cust_no=c1.cust_no and t.refund_status is null "
				+ " and not exists(select 1 from c_bank_refundtodisk d where d.bank_trans_sn=t.bank_trans_sn)";
		executeUpdate(SQL, fileNo);
	}
	
	/**
	 * 查询银行退款记录
	 * @return
	 * @throws Exception
	 */
	public void queryBankRefundtoDisk(String fileNo, DataHandler<CBankGotodisk> handler)throws Exception{
		final String sql = "SELECT * FROM c_bank_refundtodisk t WHERE t.file_no=?";
		this.queryForResult(CBankGotodisk.class, handler, sql, fileNo);
	}
	
	/**
	 * 保存银行回盘记录
	 * @return
	 * @throws Exception
	 */
	public void saveBankReturn(final List<CBankReturn> list)throws Exception{
		final String SQL = "INSERT INTO C_BANK_RETURN "
							+"  (BANK_TRANS_SN,TRANS_SN,BUSI_TYPE,COMPANY_CODE,CUST_NO,BANK_CODE,"
							+"   BANK_ACCOUNT,QC,XJFG,NEED_FEE,REAL_FEE,IS_SUCCESS,FAILURE_REASON,"
							+"   PAY_STATUS,PAY_FAILURE_REASON,TRANS_TIME,CREATE_TIME)"
							+" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		this.getJdbcTemplate().batchUpdate(SQL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int row) throws SQLException {
				CBankReturn b = list.get(row);
				ps.setString(1, b.getBank_trans_sn());
				ps.setString(2, b.getTrans_sn());
				ps.setString(3, b.getBusi_type());
				ps.setString(4, b.getCompany_code());
				ps.setString(5, b.getCust_no());
				ps.setString(6, b.getBank_code());
				ps.setString(7, b.getBank_account());
				ps.setString(8, b.getQc());
				ps.setString(9, b.getXjfg());
				ps.setInt(10, b.getNeed_fee());
				ps.setInt(11, b.getReal_fee());
				ps.setString(12, b.getIs_success());
				ps.setString(13, b.getFailure_reason());
				ps.setString(14, b.getPay_status());
				ps.setString(15, b.getPay_failure_reason());
				ps.setDate(16, new java.sql.Date(b.getTrans_time().getTime()));
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
}
