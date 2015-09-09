package com.yaochen.boss.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactiveHis;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.bill.BTaskScheduleList;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.job.JCustAcctmodeCal;
import com.ycsoft.beans.core.job.JCustInvalidCal;
import com.ycsoft.beans.core.job.JCustWriteoffAcct;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JProdPreopen;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.DataHandler;
import com.ycsoft.daos.core.JDBCException;

@Component
public class JobDao extends BaseEntityDao<JBusiCmd> {
	/**
	 *
	 */
	private static final long serialVersionUID = 3125100011171802396L;

	/**
	 * 查找需要执行的资费变更任务
	 * @return
	 * @throws Exception
	 */
	public List<JProdNextTariff> queryTariffJob() throws Exception{
		String sql ="select * from j_prod_next_tariff " +
				" where  to_char(eff_date,'yyyymmdd')<=to_char(sysdate,'yyyymmdd')";
		return this.createQuery(JProdNextTariff.class,sql).list();
	}

	/**
	 * 查找需要执行的报停任务
	 * @return
	 * @throws Exception
	 */
	public List<JUserStop> queryUserStopJob() throws Exception{
		String sql ="select * from j_user_stop " +
				" where to_char(STOP_DATE,'yyyymmdd')<=to_char(sysdate,'yyyymmdd')";
		return this.createQuery(JUserStop.class,sql).list();
	}
	
	
	/**
	 * 查询自动退订配置
	 * @return
	 * @throws Exception
	 */
	public List<BTaskScheduleList> qureyProdStopNum() throws Exception{
		String sql ="select * from b_task_schedule_list where task_code =? and status = '1' ";
		return this.createQuery(BTaskScheduleList.class,sql,SystemConstants.TASK_CODE_TD).list();
	}
	
	/**
	 * 根据地区查找需要执行的自动退订的产品
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryProdStopJob(String prodCancelDays) throws Exception{			
			String sql =" select * from (select cp.user_id,cp.prod_sn,cp.prod_id,cp.area_id,cp.county_id," +
					" caa.owe_fee, caa.real_bill,caa.active_balance+caa.order_balance all_balance" +
					" from  c_prod cp, c_acct_acctitem caa ,p_prod pp " +
					" where  caa.acctitem_id=cp.prod_id and caa.county_id=cp.county_id " +
					" and cp.acct_id = caa.acct_id and pp.prod_id = cp.prod_id   and cp.package_sn is null " +
					" and cp.status=?  and cp.status_date<=trunc(sysdate)-?"+
					" and cp.stop_by_invalid_date=?" +
					" and pp.is_base = ?  " +
					" and caa.active_balance+caa.order_balance -caa.owe_fee-caa.real_bill>=0" +
					" and not exists (select 1 from c_acct_acctitem_inactive caai " +
					" where caai.acct_id=caa.acct_id and caai.acctitem_id=caa.acctitem_id  and caai.balance <> 0 )  " +
					" ) where rownum<=1000" ;
				return this.createQuery(CProdDto.class,sql,StatusConstants.OWESTOP,prodCancelDays,"F","F").list();
	}
	
	
	/**
	 * 查找
	 * 基本包：当前到期日在在上次发送时间13个月之后，且到期日在当前日期2个月之后；
	 * 月包/季包/半年包 ：当前到期日在在上次发送时间5个月之后，且到期日在当前日期2个月之后；
	 * 年包：到期日不为当天，且授权到期时间不等于到期日的，需要重新发授权。
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryAutoBusiCmd() throws Exception{
		String sql ="select cu.card_id,cu.stb_id,cu.modem_mac,cp.* from C_PROD cp,p_prod pp,c_user cu " +
				" where cp.prod_type = 'BASE' and cp.last_send_time is not null and  ceil(trunc(months_between(cp.invalid_date,sysdate)))>=2 " +
				" and ceil(trunc(months_between(sysdate,cp.last_send_time)))>=13 and cp.prod_id=pp.prod_id and pp.is_base= ? and cu.user_id=cp.user_id  " +
				" and cp.status in ( select status_id from t_prod_status_openstop where open_or_stop='1' ) and pp.serv_id = 'DTV'" +
				" union all select cu.card_id,cu.stb_id,cu.modem_mac, cp.* from C_PROD cp,p_prod_tariff pt,c_user cu,p_prod pp " +
				" where  cp.prod_type = 'BASE' and  cp.last_send_time is not null and ceil(trunc(months_between(cp.invalid_date,sysdate)))>=2 " +
				" and ceil(trunc(months_between(sysdate,cp.last_send_time)))>=5 and cp.tariff_id=pt.tariff_id and pt.billing_cycle<12 " +
				" and cu.user_id=cp.user_id and pp.prod_id = cp.prod_id  and pp.is_base = ?  " +
				" and cp.status in ( select status_id from t_prod_status_openstop where open_or_stop='1' ) and pp.serv_id = 'DTV'" +
				" union all select cu.card_id,cu.stb_id,cu.modem_mac, cp.* from C_PROD cp,p_prod_tariff pt,c_user cu,p_prod pp " +
				" where  cp.prod_type = 'BASE' and  cp.ca_end_time is not null and cp.tariff_id=pt.tariff_id and pt.billing_cycle>=12 " +
				" and  to_char(cp.invalid_date,'yyyymmdd')<>to_char(sysdate,'yyyymmdd') and to_char(cp.invalid_date,'yyyymmdd')<>to_char(cp.ca_end_time,'yyyymmdd') " +
				" and cu.user_id=cp.user_id and pp.prod_id = cp.prod_id  and pp.is_base = ?  "+
				" and cp.status in ( select status_id from t_prod_status_openstop where open_or_stop='1' ) and pp.serv_id = 'DTV'";
		
		return this.createQuery(CProdDto.class,sql,"T","F","F").list();
	}
	
	/**
	 * 亚信宽带巡查
	 * abs(ca_end_time-invalid_date)>30,发修改用户失效日期指令
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryBandAutoBusiCmd() throws Exception {
		String sql = "select t.* From c_Prod t,p_prod p"
			+ " where t.prod_id=p.prod_id"
			+ " and p.serv_id='BAND' and p.prod_type='BASE' and t.status in "
			+ "(select ps.status_id from t_prod_status_openstop ps where ps.open_or_stop='1')"
			+ " and abs(trunc(t.ca_end_time)-(trunc(t.invalid_date)+30)) > 2"
			+ " and t.county_id in ("
			+ " select sc.county_id from t_server t,t_server_county sc"
			+ " where t.server_id=sc.server_id and t.supplier_id='YX')";
		return this.createQuery(CProdDto.class, sql).list();
	}
	
	/**
	 * 查找到期的客户套餐下，还有钱的子产品
	 * @return
	 * @throws Exception
	 */
	public List<CProdDto> queryPkgProds() throws Exception{
		String sql = StringHelper.append("select cp.* from c_prod cp,c_prod pkg,p_prod_tariff pkg_tar",
				" where cp.package_sn=pkg.prod_sn and cp.cust_id=pkg.cust_id and cp.county_id=pkg.county_id",
				" and pkg.prod_type='CPKG' and pkg_tar.tariff_id=pkg.tariff_id ",
				" and (( pkg.status in ('OUNSTOP','OWESTOP') and pkg_tar.billing_cycle=1 and pkg_tar.rent>0 ) or",
				" ( (pkg_tar.billing_cycle>1 or pkg_tar.rent=0) and pkg.invalid_date<=trunc(sysdate) and pkg.status in ('ACTIVE','OWESTOP')) )");
		return this.createQuery(CProdDto.class,sql).list();
	}
	
	public int queryAcctUnfreezeJobCount() throws Exception{
		String sql = "select count(1) from c_acct_acctitem_inactive " +
				" where to_char(NEXT_ACTIVE_TIME,'yyyymmdd')<=to_char(sysdate,'yyyymmdd')" +
				" and to_char(create_time,'yyyymmdd')<>to_char(sysdate,'yyyymmdd') and balance>0";
		return this.count(sql);
	}
	
	
	/**
	 * 查找需要执行的资金解冻任务(非首次返回)
	 * @return
	 * @throws Exception
	 */
	public void queryAcctUnfreezeJob(DataHandler<CAcctAcctitemInactive> dataHandler) throws Exception{
		/**String sql ="select * from (select t.*,rownum num from (select c.* from c_acct_acctitem_inactive c " +
				" where to_char(NEXT_ACTIVE_TIME,'yyyymmdd')<=to_char(sysdate,'yyyymmdd')" +
				" and to_char(create_time,'yyyymmdd')<>to_char(sysdate,'yyyymmdd')" +
				" and balance>0 order by rowid)t where rownum <= ? ) where num > ?";**/
		String sql = "SELECT C.* "
                 +" FROM C_ACCT_ACCTITEM_INACTIVE C "
                 +"WHERE TO_CHAR(NEXT_ACTIVE_TIME, 'yyyymmdd') <= "
                 +"      TO_CHAR(SYSDATE, 'yyyymmdd') "
                 +" AND TO_CHAR(CREATE_TIME, 'yyyymmdd') <> "
                 +"     TO_CHAR(SYSDATE, 'yyyymmdd') "
                 +" AND BALANCE > 0";
		this.queryForResult(CAcctAcctitemInactive.class, dataHandler, sql);
	}
	
	/**
	 * 查找未处理过的冲正记录
	 * @return
	 * @throws Exception
	 */
	public List<CDoneCode> queryReversalJob() throws Exception{
		/*String sql ="select c.* from c_done_code c " +
				" where c.busi_code=? and c.status=? and c.flag is null rownum < 500";
		return this.createQuery(CDoneCode.class, sql, BusiCodeConstants.ACCT_PAY,
						StatusConstants.INVALID).list();*/
		String sql = "select * from c_done_code t where t.done_code='42167671' and t.flag is null";
		return this.createQuery(CDoneCode.class, sql).list();
	}
	
	public List<CAcctAcctitemInactiveHis> queryInactiveHisByDoneCode(Integer doneCode) throws Exception {
		String sql = "select * from  c_acct_acctitem_inactive_his t where t.done_code=?";
		return this.createQuery(CAcctAcctitemInactiveHis.class, sql, doneCode).list();
	}
	
	public CAcctAcctitemChange queryAcctitemChangeByDoneCode(Integer doneCode,
			String acctId, String acctitemId, String feeType, String changeType)
			throws Exception {
		String sql = "select * from c_acct_acctitem_change t"
				+ " where t.inactive_done_code=? and t.acct_id=? and acctitem_id=? and t.fee_type=? and t.change_type=?";
		return this.createQuery(CAcctAcctitemChange.class, sql, doneCode,
				acctId, acctitemId, feeType, changeType).first();
	}
	
	public CAcctAcctitemActive queryActiveAcctitem(String acctId,
			String acctItemId, String feeType, String countyId) throws Exception {
		String sql = "select * from c_acct_acctitem_active t where t.acct_id=? and t.acctitem_id=?"
				+ " and t.fee_type=? and t.county_id=?";
		return this.createQuery(CAcctAcctitemActive.class, sql, acctId,
				acctItemId, feeType, countyId).first();
	}
	
	public void updateActiveAcctitemBalance(int balance,String acctId,
			String acctItemId, String feeType, String countyId) throws Exception {
		String sql = "update c_acct_acctitem_active set balance=balance+? where acct_id=? and acctitem_id=?"
				+ " and fee_type=? and county_id=?";
		this.executeUpdate(sql, balance, acctId, acctItemId, feeType, countyId);
	}
	
	/**
	 * doneDate为止赠送金额之和
	 * @param acctId
	 * @param acctItemId
	 * @param doneDate
	 * @return
	 * @throws Exception
	 */
	public int queryPresentBalance(String acctId, String acctItemId,
			String doneDate) throws Exception {
		String sql = "select sum(t.change_fee) from c_acct_acctitem_change t"
				+ " where t.acct_id=? and t.acctitem_id=? and t.fee_type=? and t.done_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		return Integer.parseInt(this.findUnique(sql, acctId, acctItemId,
				SystemConstants.ACCT_FEETYPE_PRESENT, doneDate));
	}
	
	/**
	 * 查找需要执行的资金解冻任务(首次返回)
	 * @return
	 * @throws Exception
	 */
	public List<CAcctAcctitemInactive> queryAcctFirstUnfreezeJob() throws Exception{
		String sql ="select * from c_acct_acctitem_inactive " +
				" where balance>0 and to_char(create_time,'yyyymmdd')=to_char(sysdate,'yyyymmdd')" +
				" and to_char(next_active_time,'yyyymmdd')<=to_char(sysdate,'yyyymmdd')";
		return this.createQuery(CAcctAcctitemInactive.class,sql).list();
	}

	/**
	 * 查找需要处理的流水
	 */

	public List<CDoneCode> queryDoneCode() throws Exception {
		String sql = "select nvl(param_value,0) from s_param where param_name='MAX_DONE_CODE'";
		int maxDoneCode = Integer.parseInt(this.findUnique(sql));
		sql = " select a.done_code,a.busi_code,'ACTIVE' status,a.county_id from c_done_code a,t_busi_code b " +
			" where done_code>? " +
			" and a.status=? " +
			" and a.busi_code=b.busi_code and b.busi_type='1' " +
			" union all " +
			" select done_code,busi_code,'INVALID' status,'' county_id from c_done_code_cancel where flag=? "+
			" order by done_code " ;
		sql = " select * from ("+sql+") where rownum<1000";
		return this.createQuery(CDoneCode.class,sql,maxDoneCode,StatusConstants.ACTIVE,SystemConstants.BOOLEAN_FALSE).list();
	}
	
	public List<JProdNextTariff> queryNextTariffByJobId() throws Exception {
		String sql = "select t.* from j_prod_next_tariff t,c_prod cp"
			+ " where t.prod_sn=cp.prod_sn and cp.package_sn is null"
			+ " and cp.status <> ?";
		return this.createQuery(JProdNextTariff.class, sql, StatusConstants.TMPPAUSE).list();
	}
	
	/**
	 * 查找需要删除账目或者账户的任务
	 * @return
	 * @throws Exception
	 */
	public List<JCustWriteoffAcct> queryWriteOffAcct() throws Exception{
		String sql ="select * from j_cust_writeoff_acct";
		return this.createQuery(JCustWriteoffAcct.class,sql).list();
	}
	
	/**
	 * 删除账目任务
	 * @param jobId
	 * @throws Exception
	 */
	public void removeAcctJob(int jobId) throws Exception{
		String sql ="delete j_cust_writeoff_acct where job_id=?";
		this.executeUpdate(sql, jobId);
	}

	/**
	 * 根据doneCode 查找业务流水明细
	 * @param doneCode
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CDoneCodeDetail> queryDoneCodeDetail (long doneCode) throws Exception {
		String sql ="select cd.done_code,cd.cust_id,cd.area_id,cd.county_id,cd.user_id" +
				" from c_done_code c,c_done_code_detail cd "+
				" where c.done_code=cd.done_code	"+
				" and c.busi_code <> ?			"+
				" and c.done_code = ?		"+
				" union all "+
				" select cd.done_code,cd.cust_id,cd.area_id,cd.county_id, fu.user_id"+
				" from c_done_code_detail cd,	"+
				" c_done_code        c,			"+
				" c_user             u,			"+
				" c_user_dtv         ud,		"+
				" c_user             fu,		"+
				" c_user_dtv         fud		"+
				" where cd.user_id = u.user_id	"+
				" and cd.done_code = c.done_code"+
				" and c.busi_code = ?		"+
				" and cd.done_code = ?	"+
				" and u.user_id = ud.user_id	"+
				" and ud.terminal_type = 'ZZD'	"+
				" and cd.cust_id = fu.cust_id	"+
				" and fu.user_id = fud.user_id	"+
				" and fud.terminal_type in ('EZD', 'FZD')";

		return this.createQuery(CDoneCodeDetail.class, sql,
				BusiCodeConstants.SYNC_ZZD_PROD, doneCode,
				BusiCodeConstants.SYNC_ZZD_PROD, doneCode).list();
	}

	/**
	 * 当天订购的所有流水明细记录
	 * @return
	 * @throws Exception
	 */
	public List<CDoneCodeDetail> queryDoneCodeDetailByOrder () throws Exception {
		String sql ="select t.done_code,t.cust_id,t.area_id,t.county_id,t.user_id" +
				" from c_prod t where  trunc(t.order_date) = trunc(sysdate) ";
		return this.createQuery(CDoneCodeDetail.class, sql).list();
	}
	
	/**
	 * 查找1小时前状态为正常没有处理过的缴费feesn
	 * @return
	 * @throws Exception
	 */
	public String queryMaxFeeSn() throws Exception {
		String sql = "select nvl(max(fee_sn),0) from c_fee " +
				" where status=? " +
				" and CREATE_TIME>sysdate -1/24";
		return findUnique(sql, StatusConstants.PAY);
	}

	/**
	 * 查找促销未执行的用户
	 * @param maxFeeSn
	 * @return
	 * @throws Exception
	 */
	public List<CUser> queryPromotionUsers() throws Exception{
		String sql ="select distinct user_id,cust_id,county_id from c_fee " +
				" where fee_type=? and status=? and auto_promotion=? and user_id is not null and CREATE_TIME>sysdate -1" +
				" union SELECT user_id,cust_id,county_id FROM c_user where auto_promotion='F' " ;
		return this.createQuery(CUser.class,sql, SystemConstants.FEE_TYPE_ACCT,StatusConstants.PAY,SystemConstants.BOOLEAN_FALSE).list();
	}
	
	public void updateFeeAutoPromotion(String userId,String custId,String countyId) throws Exception{
		String sql ="update c_fee set auto_promotion='T' WHERE user_id=? and cust_id=? and county_id=?";
		executeUpdate(sql,userId,custId,countyId);
		
		sql ="update c_user set auto_promotion='T' WHERE user_id=? and cust_id=? and county_id=?";
		executeUpdate(sql,userId,custId,countyId);
	}
	/**
	 * 更新处理过的最大流水号
	 */

	public void updateMaxDoneCode(long doneCode) throws Exception {
		String sql = "update s_param set param_value = ? where param_name= ?";
		executeUpdate(sql, doneCode,"MAX_DONE_CODE");
	}

	public void updateCancelDoneCode(long doneCode) throws Exception{
		String sql = "update c_done_code_cancel set flag = ? where done_code= ?";
		executeUpdate(sql, SystemConstants.BOOLEAN_TRUE,doneCode);
	}
	

	/**
	 * 更新处理过的最大FEESN
	 */
	public void updateMaxFeeSn(String feeSn) throws Exception {
		String sql = "update s_param set param_value = ? where param_name= ?";
		executeUpdate(sql, feeSn,"MAX_FEE_SN");
	}
	
	public void updateMaxJobId(Integer jobId) throws Exception {
		String sql = "update s_param set param_value = ? where param_name= ?";
		executeUpdate(sql, jobId,"MAX_JOB_ID");
	}

	public int qureyJobCount(int jobId)throws Exception {
		String sql ="select count(1) from j_cust_writeoff where job_id=?";
		return Integer.parseInt(this.findUnique(sql, jobId));
	}
	
	public int qureyJobCount(String custId)throws Exception {
		String sql ="select count(1) from j_cust_writeoff where cust_id=?";
		return Integer.parseInt(this.findUnique(sql, custId));
	}

	public void deleteTariffJob(int jobId) throws Exception {
		String sql ="delete j_prod_next_tariff where job_id=?";
		executeUpdate(sql, jobId);
		
	}
	
	public void deleteUserStopJob(int jobId) throws Exception {
		String sql="insert into j_user_stop_his select * from j_user_stop where job_id=?";
		executeUpdate(sql, jobId);
		sql ="delete j_user_stop where job_id=?";
		executeUpdate(sql, jobId);
		
	}
	
	public void deleteInvalidCal() throws Exception{
		String sql = "delete from J_PROD_INVALID_CAL where trunc(create_time)<trunc(sysdate)";
		this.executeUpdate(sql);
	}
	
	public void saveInvalidCal(String prodSn,String areaId,String countyId) throws Exception{
		String sql = "insert into J_PROD_INVALID_CAL(prod_sn,area_id,county_id)" +
				" values (?,?,?)";
		this.executeUpdate(sql, prodSn, areaId, countyId);
	}
	
	public void createCustWriteOffJob(String custId,String writeOff,String areaId,String countyId)throws Exception{
		String sql = "insert into J_CUST_WRITEOFF(job_id,done_code,cust_id,area_id,county_id,writeoff)" +
				" values (-3,-3,?,?,?,?)";
		this.executeUpdate(sql, custId, areaId, countyId, writeOff);
	}
	
	public void createCustAcctModeJob(Integer doneCode, String custId,String areaId,String countyId)throws Exception{
		String sql = "insert into j_cust_acctmode_cal(job_id,done_code,cust_id,area_id,county_id)" +
				" values (-1,?,?,?,?)";
		this.executeUpdate(sql, doneCode, custId, areaId, countyId);
	}

	/**
	 * 查找需要取消的VOD预扣费记录
	 * @return
	 * @throws JDBCException 
	 */
	public List<CAcctPreFee> queryNeedCancelPreFee() throws JDBCException {
		String sql = StringHelper.append("select *  from c_acct_pre_fee c",
				" where c.status = 'T' and c.is_valid = 'F' and c.process_flag = 2 and c.original_sn is not null");
		return createQuery(CAcctPreFee.class,sql).list();
	}

	/**
	 * 查找资费失效且公用账目使用类型不为NONE的产品
	 * @return
	 * @throws Exception
	 */
	public List<CProd> queryProdWithInvalidTariff()  throws JDBCException {
		String sql = StringHelper.append("select C.Prod_Sn,c.public_acctitem_type,c.county_id,c.area_id from c_prod c,p_prod_tariff p where c.tariff_id=p.tariff_id",
				" and p.status=? and p.rent > 0 and  C.PUBLIC_ACCTITEM_TYPE <> ? and rownum < 1000");
		return createQuery(CProd.class,sql, StatusConstants.INVALID,SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE).list();
	}

	/**
	 * 查找武汉直属，副终端基本包资费为24元每月（资费ID为4357)，且满3年的产品
	 * @return
	 * @throws JDBCException
	 */
	public List<CProd> queryFzdProdTariff() throws JDBCException{
		String sql = "select c.* from c_prod c,c_user_dtv d where  c.user_id=d.user_id and d.terminal_type='FZD' AND c.next_tariff_id is null and"
			+ " c.status='ACTIVE' AND c.prod_id='2728' and c.tariff_id='4357' and"
			+ " c.order_date < (sysdate - 365*3) and (c.county_id='0102' or c.county_id='0101')";
		return createQuery(CProd.class, sql).list();
	}

	public List<String> queryUserGDDevices(String userId) throws JDBCException {
		String sql = "select distinct r.device_id from c_user t,c_cust_device t2,r_device r where (t.stb_id=t2.device_code or t.card_id=t2.device_code"
			+ " or t.modem_mac = t2.device_code) and t2.device_id=r.device_id and t.user_id=? and r.ownership=?";
		return findUniques(sql, userId,SystemConstants.OWNERSHIP_GD);
	}

	/**
	 * 查找资费计算任务
	 * @return
	 * @throws JDBCException 
	 */
	public List<JCustInvalidCal> queryInvalidCal(int count,int jobId) throws JDBCException {
		String sql = StringHelper.append("select * from ( select * from j_cust_invalid_cal j ",
				" where not exists (select 1 from j_cust_writeoff t where t.cust_id=j.cust_id)",
				" and not exists (select 1 from j_cust_credit_exec t1 where t1.cust_id=j.cust_id)",
				" order by j.job_id ) t where  t.job_id > ?  and rownum <= ?");
		return createQuery(JCustInvalidCal.class, sql,jobId,count).list();
	}
	
	/**
	 * 查找到期日计算任务按起止地区
	 *  (area_id > start and  area_id <= end)
	 * @return
	 * @throws JDBCException 
	 */
	public List<JCustInvalidCal> queryInvalidCalByAreaid(int count,String start,String end) throws JDBCException {
		String sql = StringHelper.append("select * from ( select * from j_cust_invalid_cal j ",
				" where not exists (select 1 from j_cust_writeoff t where t.cust_id=j.cust_id)",
				" and not exists (select 1 from j_cust_credit_exec t1 where t1.cust_id=j.cust_id)",
				" and area_id> ? and area_id<= ?",
				" order by j.job_id ) t where   rownum <= ?");
		return createQuery(JCustInvalidCal.class, sql,start,end,count).list();
	}
	
	/**
	 * 查找账务模式判断任务按起止地区
	 *  (area_id > start and  area_id <= end)
	 * @return
	 * @throws JDBCException 
	 */
	public List<JCustAcctmodeCal> queryAcctmodeCalByAreaid(int count,String start,String end) throws JDBCException {
		String sql = StringHelper.append("select * from ( select * from j_cust_acctmode_cal j ",
				" where not exists (select 1 from j_cust_writeoff t where t.cust_id=j.cust_id)",
				"  and  not exists (select 1 from j_cust_credit_exec t1 where t1.cust_id=j.cust_id)",
				" and not exists (select 1 from j_cust_invalid_cal t1 where t1.cust_id=j.cust_id)",
				" and  area_id> ? and area_id<= ?  order by j.job_id ) t where   rownum <= ?");
		return createQuery(JCustAcctmodeCal.class, sql,start,end,count).list();
	}
	public CProdDto queryProdBySn(String prodSn,String countyId) throws JDBCException{
		return this.createQuery(CProdDto.class, 
				"select cu.card_id,cu.stb_id,cu.modem_mac,cp.* from c_prod cp,c_user cu where cp.user_id=cu.user_id and cp.county_id=cu.county_id and cp.prod_sn=? and cp.county_id=?"
				, prodSn,countyId).first();
	}
	/**
	 * 查找预开通计算任务
	 * @param jobstep
	 * @return
	 * @throws JDBCException
	 */
	public List<JProdPreopen> queryProdPreopen(Integer jobstep) throws JDBCException{
		
		String sql=StringHelper.append("select * from j_prod_preopen t ",
		" where t.pre_open_time>=trunc(sysdate) and t.pre_open_time<trunc(sysdate)+1 ",
		" and nvl(t.jobstep,0) <? ");
		return createQuery(JProdPreopen.class,sql,jobstep).list();
	}
	/**
	 * 更新预开通的执行步骤
	 * @param jobId
	 * @param jobstep
	 * @throws JDBCException
	 */
	public void updateProdPreopenStep(String jobId,Integer jobstep) throws JDBCException{
		String sql="update j_prod_preopen set jobstep=? where job_id=?";
		this.executeUpdate(sql, jobstep,jobId);
	}
	/**
	 * 保存预开通历史
	 * @param job
	 * @throws JDBCException
	 */
	public void removeProdPreopenToHis(JProdPreopen job) throws JDBCException{
		String sql=StringHelper.append(" insert into j_prod_preopen_his ",
		" (job_id,done_code,prod_sn,pre_open_time,area_id,county_id,jobstep,is_success,remark) ",
		" values(?,?       ,?      ,?            ,?      ,?        ,?      ,?         ,?) ");
		
		this.executeUpdate(sql, job.getJob_id(),job.getDone_code(),job.getProd_sn(),job.getPre_open_time(),job.getArea_id(),job.getCounty_id()
				,job.getJobstep(),job.getIs_success(),job.getRemark());
	}
	/**
	 * 删除预开通
	 * @param job
	 * @throws JDBCException
	 */
	public void deleteProdPreopen(JProdPreopen job) throws JDBCException{
		this.executeUpdate("delete j_prod_preopen where job_id=? ", job.getJob_id());
	}
	
	/**
	 * 查找资费计算任务
	 * @return
	 * @throws JDBCException 
	 */
	public List<JCustInvalidCal> queryInvalidCalForPatch(int count,int jobId) throws JDBCException {
		String sql = StringHelper.append("select * from ( select * from j_cust_invalid_cal j ",
				" where not exists (select 1 from j_cust_writeoff t where t.cust_id=j.cust_id)",
				" and not exists (select 1 from j_cust_credit_exec t1 where t1.cust_id=j.cust_id)",
				" order by j.job_id ) t where  t.job_id < ?  and rownum <= ?");
		return createQuery(JCustInvalidCal.class, sql,jobId,count).list();
	}

	public void removeInvalidCalJob(Integer jobId) throws JDBCException {
		String sql ="delete j_cust_invalid_cal where job_id=?";
		this.executeUpdate(sql, jobId);
	}
	
	public void removeAcctmodeCalJob(Integer jobId) throws JDBCException {
		String sql ="delete j_cust_acctmode_cal where job_id=?";
		this.executeUpdate(sql, jobId);
	}
	public void updateInvoiceByDoneCode(Integer doneCode) throws JDBCException {
		String sql = "update c_fee c set c.invoice_book_id= "+
			" (select r.invoice_book_id from r_invoice r where r.invoice_id=c.invoice_id and r.invoice_code=c.invoice_code)"+
			" where c.create_time > sysdate -1 and c.invoice_book_id is null";
		this.executeUpdate(sql);	
	}

	public void dealProdStatusError() throws JDBCException {
		String sql = StringHelper.append("insert into j_busi_cmd ",
				" select  seq_job_id.nextval,-1,'PST_PROD',c.cust_id,c.user_id,c.stb_id,c.card_id,c.modem_mac,p.prod_sn, ",
				" sysdate,c.area_id,c.county_id,p.prod_id,'',10 from c_user c,c_prod p ",
				" where c.user_id=p.user_id and c.status='REQSTOP' and c.county_id=p.county_id and p.status <> 'REQSTOP'");
		executeUpdate(sql);
		
		sql = StringHelper.append("update (select /*+bypass_ujvc*/  p.prod_sn,p.status from c_user c,c_prod p ",
				" where c.user_id=p.user_id and c.status='REQSTOP' and p.status <> 'REQSTOP' and c.county_id=p.county_id ) ",
				" set status='REQSTOP'");
		executeUpdate(sql);
		
	}

	public List<CUser> queryProdIncludeUser(String countyId) throws JDBCException {
		String sql = StringHelper.append("select distinct b.cust_id,b.user_id,b.county_id from(select  distinct t2.prod_id big_prod_id,t3.prod_id small_prod_id,t3.county_id ",
				" from c_prod_include t1, c_prod t2,c_prod t3 where t1.cust_id = t2.cust_id and t1.cust_id=t3.cust_id ",
				" and t1.user_id = t2.user_id and t1.user_id=t3.user_id ",
				" and t1.prod_sn=t2.prod_sn and t1.include_prod_sn=t3.prod_sn) a,c_prod b,c_prod c ",
				" where b.cust_id=c.cust_id and b.user_id=c.user_id and b.county_id=c.county_id and a.county_id=b.county_id",
				" and b.prod_id=a.big_prod_id and c.prod_id=a.small_prod_id and b.prod_id<>c.prod_id ",
				" and b.package_sn is null and c.package_sn is null and b.order_date < sysdate -0.003 and c.order_date < sysdate -0.003",
				" and not exists (select 1 from c_prod_include p where c.user_id=p.user_id and c.prod_sn=p.include_prod_sn) ",
				" and b.status='ACTIVE' and c.status='ACTIVE' and b.county_id=? and rownum < 500");
		return createQuery(CUser.class, sql, countyId).list();
	}
	
	public List<CCust> queryCustWithInvalidCustClass()throws JDBCException{
		String sql = "SELECT t.* FROM c_cust t WHERE to_date(t.cust_class_date, 'yyyy-MM-dd') <= SYSDATE";
		return createQuery(CCust.class, sql).list();
	}
}
